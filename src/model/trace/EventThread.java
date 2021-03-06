/*
 * Copyright (c) 2001, 2011, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 * This source code is provided to illustrate the usage of a given feature
 * or technique and has been deliberately simplified. Additional steps
 * required for a production-quality application, such as security checks,
 * input validation and proper error handling, might not be present in
 * this sample code.
 */

package model.trace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ExceptionRequest;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.MethodExitRequest;
import com.sun.jdi.request.ModificationWatchpointRequest;
import com.sun.jdi.request.StepRequest;
import com.sun.jdi.request.ThreadDeathRequest;

import model.Method;
import model.ToolVisitor;

/**
 * This class processes incoming JDI events and displays them
 *
 * @author Robert Field
 */
public class EventThread extends Thread {

	private final VirtualMachine vm; // Running VM
	private final String[] excludes; // Packages to exclude
	// private final PrintWriter writer; // Where output goes

	static String nextBaseIndent = ""; // Starting indent for next thread

	private boolean connected = true; // Connected to VM
	private boolean vmDied = true; // VMDeath occurred

	ArrayList<Method> methodList = ToolVisitor.getMethodList();
	static ArrayList<CallerAndCallee> traceList = new ArrayList<CallerAndCallee>();
	ArrayList<Method> stack = new ArrayList<Method>();
	Method caller = null;

	// Maps ThreadReference to ThreadTrace instances
	// ThreadReferenceをThreadTraceインスタンスにマップする
	private Map<ThreadReference, ThreadTrace> traceMap = new HashMap<>();

	EventThread(VirtualMachine vm, String[] excludes) {
		super("event-handler");
		this.vm = vm;
		this.excludes = excludes;
	}

	public static ArrayList<CallerAndCallee> getTraceList() {
		return traceList;
	}

	/**
	 * Run the event handling thread. As long as we are connected, get event
	 * sets off the queue and dispatch the events within them.
	 * 
	 * イベント処理スレッドを実行します。 接続されている限り、イベントセットをキューから取り出して、それらのイベントをディスパッチします。
	 */
	@Override
	public void run() {
		EventQueue queue = vm.eventQueue(); // この仮想マシンのイベントキューを返す
		while (connected) {
			try {
				EventSet eventSet = queue.remove(); // 次に発生するイベントを無期限に待機
				EventIterator it = eventSet.eventIterator();
				while (it.hasNext()) {
					handleEvent(it.nextEvent());
				}
				eventSet.resume(); // このイベントセットによって中断されたスレッドを再開する

				// for (CallerAndCallee cac : traceList) {
				// if (cac.caller == null) {
				// System.out.println("start " + " --> " +
				// cac.getCallee().getMethodName());
				// } else if (cac.callee == null) {
				// System.out.println(cac.getCaller().getMethodName() + " --> "
				// + "end");
				// } else {
				// System.out.println(cac.getCaller().getMethodName() + " --> "
				// + cac.getCallee().getMethodName());
				// }
				// }
			} catch (InterruptedException exc) {
				// Ignore
			} catch (VMDisconnectedException discExc) {
				handleDisconnectedException();
				break;
			}
		}
	}

	/**
	 * Create the desired event requests, and enable them so that we will get
	 * events.
	 * 
	 * @param excludes
	 *            Class patterns for which we don't want events
	 * @param watchFields
	 *            Do we want to watch assignments to fields
	 */
	void setEventRequests(boolean watchFields) {
		EventRequestManager mgr = vm.eventRequestManager();

		// want all exceptions
		ExceptionRequest excReq = mgr.createExceptionRequest(null, true, true);
		// suspend so we can step
		excReq.setSuspendPolicy(EventRequest.SUSPEND_ALL);
		excReq.enable();

		MethodEntryRequest menr = mgr.createMethodEntryRequest();
		for (int i = 0; i < excludes.length; ++i) {
			menr.addClassExclusionFilter(excludes[i]);
		}
		menr.setSuspendPolicy(EventRequest.SUSPEND_NONE);
		menr.enable();

		MethodExitRequest mexr = mgr.createMethodExitRequest();
		for (int i = 0; i < excludes.length; ++i) {
			mexr.addClassExclusionFilter(excludes[i]);
		}
		mexr.setSuspendPolicy(EventRequest.SUSPEND_NONE);
		mexr.enable();

		ThreadDeathRequest tdr = mgr.createThreadDeathRequest();
		// Make sure we sync on thread death
		tdr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
		tdr.enable();

		if (watchFields) {
			ClassPrepareRequest cpr = mgr.createClassPrepareRequest();
			for (int i = 0; i < excludes.length; ++i) {
				cpr.addClassExclusionFilter(excludes[i]);
			}
			cpr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
			cpr.enable();
		}
	}

	/**
	 * This class keeps context on events in one thread. In this implementation,
	 * context is the indentation prefix.
	 */
	class ThreadTrace {
		final ThreadReference thread;
		final String baseIndent;
		static final String threadDelta = "                     ";
		StringBuffer indent;

		ThreadTrace(ThreadReference thread) {
			this.thread = thread;
			this.baseIndent = nextBaseIndent;
			indent = new StringBuffer(baseIndent);
			nextBaseIndent += threadDelta;
			println("====== " + thread.name() + " ======");
		}

		private void println(String str) {
			System.out.println(str);
		}

		// メソッドに入るところ
		void methodEntryEvent(MethodEntryEvent event) {
			String methodName = event.method().name();
			String className = event.method().declaringType().name();
			ArrayList<String> paraList = new ArrayList<String>();

			for (String s : event.method().argumentTypeNames()) {
				paraList.add(s);
			}
			// println(event.method().name() + " -- " +
			// event.method().declaringType().name());
			// indent.append("| ");
			// println(methodName + "\t" + className);
			// for (String s : paraList) {
			// println("\t" + s);
			// }

			// 呼び出し元はスタックの一番上のメソッド
			if (!stack.isEmpty()) {
				caller = stack.get(stack.size() - 1);
			}

			// println("method " + methodName);

			if (methodName.equals("<init>")) {
				methodName = className.substring(className.lastIndexOf(".") + 1);
			}

			for (Method m : methodList) {
				if (m.getMethodName().equals(methodName) && m.getDeclaringClassName().equals(className)
						&& m.getParametersList().equals(paraList)) {

					traceList.add(new CallerAndCallee(caller, m, true));
					// if (!stack.isEmpty()) {
					// println("\t" + caller.getMethodName() + "--" +
					// m.getMethodName());
					// } else {
					// println("\t\t--" + m.getMethodName());
					// }
					stack.add(m);
				}
			}

		}

		// メソッドから抜け出すところ
		void methodExitEvent(MethodExitEvent event) {
			// indent.setLength(indent.length() - 2);
			if (stack.size() > 1) {
				traceList.add(new CallerAndCallee(stack.get(stack.size() - 1), stack.get(stack.size() - 2), false));
			}
			stack.remove(stack.size() - 1);
		}

		void fieldWatchEvent(ModificationWatchpointEvent event) {
			// Field field = event.field();
			// Value value = event.valueToBe();
			// println(" " + field.name() + " = " + value);
		}

		void exceptionEvent(ExceptionEvent event) {
			// println("Exception: " + event.exception() + " catch: " +
			// event.catchLocation());

			// Step to the catch
			EventRequestManager mgr = vm.eventRequestManager();
			StepRequest req = mgr.createStepRequest(thread, StepRequest.STEP_MIN, StepRequest.STEP_INTO);
			req.addCountFilter(1); // next step only
			req.setSuspendPolicy(EventRequest.SUSPEND_ALL);
			req.enable();
		}

		// Step to exception catch
		void stepEvent(StepEvent event) {
			// Adjust call depth
			int cnt = 0;
			indent = new StringBuffer(baseIndent);
			try {
				cnt = thread.frameCount();
			} catch (IncompatibleThreadStateException exc) {
			}
			while (cnt-- > 0) {
				indent.append("| ");
			}

			EventRequestManager mgr = vm.eventRequestManager();
			mgr.deleteEventRequest(event.request());
		}

		void threadDeathEvent(ThreadDeathEvent event) {
			// indent = new StringBuffer(baseIndent);
			// println("====== " + thread.name() + " end ======");
		}

	}

	/**
	 * Returns the ThreadTrace instance for the specified thread, creating one
	 * if needed.
	 */
	ThreadTrace threadTrace(ThreadReference thread) {
		ThreadTrace trace = traceMap.get(thread);
		if (trace == null) {
			trace = new ThreadTrace(thread);
			traceMap.put(thread, trace);
		}
		return trace;
	}

	/**
	 * Dispatch incoming events
	 */
	private void handleEvent(Event event) {
		if (event instanceof ExceptionEvent) { // ターゲット VM の例外通知
			exceptionEvent((ExceptionEvent) event);
		} else if (event instanceof ModificationWatchpointEvent) { // ターゲットVMのフィールド変更の通知
			fieldWatchEvent((ModificationWatchpointEvent) event);
		} else if (event instanceof MethodEntryEvent) { // ターゲット VM のメソッド呼び出しの通知
			methodEntryEvent((MethodEntryEvent) event);
		} else if (event instanceof MethodExitEvent) { // ターゲット VM 内でのメソッドの復帰の通知
			methodExitEvent((MethodExitEvent) event);
		} else if (event instanceof StepEvent) {
			stepEvent((StepEvent) event);
		} else if (event instanceof ThreadDeathEvent) {
			threadDeathEvent((ThreadDeathEvent) event);
		} else if (event instanceof ClassPrepareEvent) {
			classPrepareEvent((ClassPrepareEvent) event);
		} else if (event instanceof VMStartEvent) {
			vmStartEvent((VMStartEvent) event);
		} else if (event instanceof VMDeathEvent) {
			vmDeathEvent((VMDeathEvent) event);
		} else if (event instanceof VMDisconnectEvent) {
			vmDisconnectEvent((VMDisconnectEvent) event);
		} else {
			throw new Error("Unexpected event type");
		}
	}

	/***
	 * A VMDisconnectedException has happened while dealing with another event.
	 * We need to flush the event queue, dealing only with exit events (VMDeath,
	 * VMDisconnect) so that we terminate correctly.
	 */
	synchronized void handleDisconnectedException() {
		EventQueue queue = vm.eventQueue();
		while (connected) {
			try {
				EventSet eventSet = queue.remove();
				EventIterator iter = eventSet.eventIterator();
				while (iter.hasNext()) {
					Event event = iter.nextEvent();
					if (event instanceof VMDeathEvent) {
						vmDeathEvent((VMDeathEvent) event);
					} else if (event instanceof VMDisconnectEvent) {
						vmDisconnectEvent((VMDisconnectEvent) event);
					}
				}
				eventSet.resume(); // Resume the VM
			} catch (InterruptedException exc) {
				// ignore
			}
		}
	}

	private void vmStartEvent(VMStartEvent event) {
		System.out.println("-- VM Started --");
	}

	// Forward event for thread specific processing
	private void methodEntryEvent(MethodEntryEvent event) {
		threadTrace(event.thread()).methodEntryEvent(event);
	}

	// Forward event for thread specific processing
	private void methodExitEvent(MethodExitEvent event) {
		threadTrace(event.thread()).methodExitEvent(event);
	}

	// Forward event for thread specific processing
	private void stepEvent(StepEvent event) {
		threadTrace(event.thread()).stepEvent(event);
	}

	// Forward event for thread specific processing
	private void fieldWatchEvent(ModificationWatchpointEvent event) {
		threadTrace(event.thread()).fieldWatchEvent(event);
	}

	void threadDeathEvent(ThreadDeathEvent event) {
		ThreadTrace trace = traceMap.get(event.thread());
		if (trace != null) { // only want threads we care about
			trace.threadDeathEvent(event); // Forward event
		}
	}

	/**
	 * A new class has been loaded. Set watchpoints on each of its fields
	 */
	private void classPrepareEvent(ClassPrepareEvent event) {
		EventRequestManager mgr = vm.eventRequestManager();
		List<Field> fields = event.referenceType().visibleFields();
		for (Field field : fields) {
			ModificationWatchpointRequest req = mgr.createModificationWatchpointRequest(field);
			for (int i = 0; i < excludes.length; ++i) {
				req.addClassExclusionFilter(excludes[i]);
			}
			req.setSuspendPolicy(EventRequest.SUSPEND_NONE);
			req.enable();
		}
	}

	private void exceptionEvent(ExceptionEvent event) {
		ThreadTrace trace = traceMap.get(event.thread());
		if (trace != null) { // only want threads we care about
			trace.exceptionEvent(event); // Forward event
		}
	}

	public void vmDeathEvent(VMDeathEvent event) {
		vmDied = true;
		System.out.println("-- The application exited --");
	}

	public void vmDisconnectEvent(VMDisconnectEvent event) {
		connected = false;
		if (!vmDied) {
			System.out.println("-- The application has been disconnected --");
		}
	}
}

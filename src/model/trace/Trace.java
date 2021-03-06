package model.trace;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;

/**
 * This program traces the execution of another program. See "java Trace -help".
 * It is a simple example of the use of the Java Debug Interface.
 *
 * @author Robert Field
 */
public class Trace {

	// Running remote VM
	private final VirtualMachine vm;

	// Thread transferring remote error stream to our error stream
	private Thread errThread = null;

	// Thread transferring remote output stream to our output stream
	private Thread outThread = null;

	// Mode for tracing the Trace program (default= 0 off)
	private int debugTraceMode = 0;

	// Do we want to watch assignments to fields
	private boolean watchFields = false;

	// Class patterns for which we don't want events
	private String[] excludes = { "java.*", "javax.*", "sun.*", "com.sun.*" };

	/**
	 * Parse the command line arguments. Launch target VM. Generate the trace.
	 */

	public Trace(String option, String args) {

		vm = launchTarget(option, args);
		generateTrace();

	}

	/**
	 * Generate the trace. Enable events, start thread to display events, start
	 * threads to forward remote error and output streams, resume the remote VM,
	 * wait for the final event, and shutdown.
	 * 
	 * トレースを生成します。 イベントを有効にし、イベントを表示するスレッドを開始し、リモートエラーを転送してストリームを出力するスレッドを開始し、
	 * リモートVMを再開し、最終イベントを待ってからシャットダウンします。
	 */
	void generateTrace() {
		vm.setDebugTraceMode(debugTraceMode);
		EventThread eventThread = new EventThread(vm, excludes);
		eventThread.setEventRequests(watchFields);
		eventThread.start();
		redirectOutput();
		vm.resume();

		// Shutdown begins when event thread terminates
		try {
			eventThread.join();
			errThread.join(); // Make sure output is forwarded
			outThread.join(); // before we exit
		} catch (InterruptedException exc) {
			// we don't interrupt
		}
	}

	/**
	 * Launch target VM. Forward target's output and error. ターゲットVMの起動
	 */
	VirtualMachine launchTarget(String option, String mainArgs) { // 「クラス名 引数」
		// ターゲット VM に接続する前に、ターゲット VM を起動できるコネクタ
		LaunchingConnector connector = findLaunchingConnector();
		Map<String, Connector.Argument> arguments = connectorArguments(connector, option, mainArgs);
		try {
			// アプリケーションを起動し、その VM に接続
			return connector.launch(arguments);
		} catch (IOException exc) {
			throw new Error("Unable to launch target VM: " + exc);
		} catch (IllegalConnectorArgumentsException exc) {
			throw new Error("Internal error: " + exc);
		} catch (VMStartException exc) {
			throw new Error("Target VM failed to initialize: " + exc.getMessage());
		}
	}

	void redirectOutput() {
		Process process = vm.process();

		// Copy target's output and error to our output and error.
		errThread = new StreamRedirectThread("error reader", process.getErrorStream(), System.err);
		outThread = new StreamRedirectThread("output reader", process.getInputStream(), System.out);
		errThread.start();
		outThread.start();
	}

	/**
	 * Find a com.sun.jdi.CommandLineLaunch connector
	 */
	LaunchingConnector findLaunchingConnector() {
		// JDI インタフェースのデフォルト実装へのアクセスを提供する初期クラス
		// allConnectors() 既知のすべての Connector オブジェクトのリストを返す
		List<Connector> connectors = Bootstrap.virtualMachineManager().allConnectors();
		for (Connector connector : connectors) {
			if (connector.name().equals("com.sun.jdi.CommandLineLaunch")) {
				return (LaunchingConnector) connector;
			}
		}
		throw new Error("No launching connector");
	}

	/**
	 * Return the launching connector's arguments. 起動しているコネクタの引数を返す
	 */
	Map<String, Connector.Argument> connectorArguments(LaunchingConnector connector, String option, String mainArgs) {
		// この
		// Connectorが受け入れる引数とそのデフォルト値を返す。返されるマップのキーは、文字列引数、値は、引数とそのデフォルト値の情報を含む
		// Connector.Argument
		Map<String, Connector.Argument> arguments = connector.defaultArguments();
		Connector.Argument options = (Connector.Argument) arguments.get("options");
		Connector.Argument mainArg = (Connector.Argument) arguments.get("main");

		if (mainArg == null) {
			throw new Error("main .. Bad launching connector");
		}
		if (options == null) {
			throw new Error("options .. Bad launching connector");
		}

		options.setValue(option);
		mainArg.setValue(mainArgs);

		// for (String key : arguments.keySet()) {
		// System.out.println("key : " + key + " val : " + arguments.get(key));
		// }

		if (watchFields) {
			// We need a VM that supports watchpoints
			Connector.Argument optionArg = (Connector.Argument) arguments.get("options");
			if (optionArg == null) {
				throw new Error("Bad launching connector");
			}
			optionArg.setValue("-classic");
		}
		return arguments;
	}

	/**
	 * Print command line usage help
	 */
	void usage() {
		System.err.println("Usage: java Trace <options> <class> <args>");
		System.err.println("<options> are:");
		System.err.println("  -output <filename>   Output trace to <filename>");
		System.err.println("  -all                 Include system classes in output");
		System.err.println("  -help                Print this help message");
		System.err.println("<class> is the program to trace");
		System.err.println("<args> are the arguments to <class>");
	}
}

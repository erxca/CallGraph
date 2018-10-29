package view;

import java.awt.Color;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.eclipse.ui.IWorkbenchPage;

import controller.GraphPanelButtonListener;
import model.CalledLinelSet;
import model.Method;
import model.trace.CallerAndCallee;

public class TraceGraphPanel extends MyGraphPanel {
	ArrayList<CallerAndCallee> traceList = model.trace.EventThread.getTraceList();
	Border border;
	Method caller, callee;

	public TraceGraphPanel(IWorkbenchPage page, Frame f) {
		// TODO Auto-generated constructor stub
		super(page, f);
	}

	public int searchBreakPoint() {
		String bpMethodName = ConfigurationView.getMethodTf().getText();
		String bpClassName = ConfigurationView.getClassTf().getText();
		String bpPara = ConfigurationView.getmParaTf().getText();
		ArrayList<String> paraList = ConfigurationView.getParaList();

		// boolean isOk = true;

		for (int j = 0; j < traceList.size(); j++) {
			// if (isOk) {
			Method callee = traceList.get(j).getCallee();
			if (callee.getMethodName().equals(bpMethodName)) {
				System.out.println("methodName");
				if (callee.getDeclaringClassName().indexOf(bpClassName) > -1) {
					System.out.println("className");
					if (bpPara.length() > 0) {
						if (paraList.equals(callee.getParametersList())) {
							proceedVisualize(j);
							return j;
						}
						// int idx = 0;
						// String bPara = bpPara;
						// for (int i = 0; i <
						// callee.getParametersList().size(); i++) {
						// String cPara = callee.getParametersList().get(i);
						// idx = bPara.indexOf(" ");
						// if (idx < 0) {
						// // isOk = false;
						// break;
						// }
						// bPara = bPara.substring(0, idx);
						// if (cPara.indexOf(bPara) < -1) {
						// // isOk = false;
						// break;
						// } else {
						// bPara.substring(idx + 1);
						// }
						// }

					} else {
						proceedVisualize(j);
						return j;
					}
				}
			}
			// }
		}
		return -1;
	}

	private void proceedVisualize(int bpNum) {
		for (int i = 0; i <= bpNum; i++) {
			checkMethod(i);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void checkMethod(int num) {
		CallerAndCallee set = traceList.get(num);
		caller = set.getCaller();
		callee = set.getCallee();

		if (num == 0) {
			initList();

			for (MyMethodButton btn : nodeList) {

				if (btn.getM().equals(callee) && (btn.getLevel() == this.level)) {
					btn.isFocused = true;
					btn.setBorder(new LineBorder(Color.red, 3, true));
					btn.setBorderPainted(true);
				}
			}
		} else if (num > 0) {
			if (set.isDown()) {
				// if (caller != null) {
				// System.out.println(caller.getMethodName() + " --> " +
				// callee.getMethodName() + " (down)");

				for (MyMethodButton btn : nodeList) {
					// System.out.println(btn.getM().getMethodName());
					if (btn.getM().equals(caller) && (btn.getLevel() == this.level)) {
						// System.out.println(btn.getM().getMethodName());
						btn.isFocused = false;
						btn.setBorder(border);

						if (btn.getLevel() == 0) {
							makeCalledMethod(caller,
									(int) (btn.getBounds().getCenterX() + btn.getParent().getBounds().getX()),
									btn.getBounds().getMaxY() + btn.getParent().getBounds().getY());
						} else {
							makeCalledMethod(caller, (int) (btn.getBounds().getCenterX()), btn.getBounds().getMaxY());
						}
						break;
					}
				}

			} else {
				// System.out.println(callee.getMethodName() + " <-- " +
				// caller.getMethodName() + " (up)");
				int callerLevel = 0;
				for (MyMethodButton btn : nodeList) {
					if (btn.getM().equals(caller)) {
						// removeBtn(btn.level);
						callerLevel = btn.level;
						// System.out.println("up caller " + callerLevel);
					}
					if (btn.getM().equals(callee)) {
						btn.isFocused = true;
						btn.setBorder(new LineBorder(Color.red, 3, true));
						btn.setBorderPainted(true);
					}
				}
				removeBtn(callerLevel - 1);

				invalidate();
				f.validate();
				repaint();
			}

		}

	}

	public void makeCalledMethod(Method method, int centerX, double pY) {
		level++;
		int mWidth = 20;
		Set<Method> mSet = method.getMethodCallSet();

		for (Method m : mSet) {
			MyMethodButton btn;

			if (m.isConstructor()) {
				btn = new RoundedCornerButton(this, m, true, level);
			} else {
				btn = new TraceButton(this, m, true, level);
			}

			if (m.equals(callee)) {
				// System.out.println("callee " + callee.getMethodName());
				btn.isFocused = true;
				btn.setBorder(new LineBorder(Color.red, 3, true));
				btn.setBorderPainted(true);
			} else if (m.getMethodName().equals(callee.getMethodName())
					&& m.getParametersList().equals(callee.getParametersList())) {
				btn = new TraceButton(this, callee, true, level);
				btn.isFocused = true;
				btn.setBorder(new LineBorder(Color.red, 3, true));
				btn.setBorderPainted(true);
			}

			for (CalledLinelSet set : method.getCallLineList()) {
				if (set.getMethodName().equals(m.getMethodName())) {
					btn.calledLineList.add(set.getLineNum());
				}
			}
			checkSameMethod(btn);
			nodeList.add(btn);

			btn.addMouseListener(new GraphPanelButtonListener(method, m, btn.calledLineList));

			mWidth += btn.fm.stringWidth(m.getMethodName()) + 50;

		}

		checkLeftEnd(centerX, pY, mWidth);
		checkRightEnd(centerX, pY, mWidth);

		invalidate();
		f.validate();
		repaint();
	}

	public void setBorder(Border border) {
		this.border = border;
	}

}

package controller;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class MyClassHandler extends AbstractHandler {
	Shell shell = Display.getDefault().getActiveShell();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		ISelection selection = HandlerUtil.getActiveMenuSelectionChecked(event);
		// System.out.println(selection);
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		String sel = selection.toString();
		int lineEnd = sel.indexOf("\n");
		sel = sel.substring(0, lineEnd);

		String className = getClassName(sel);
		String fileName = className + ".java";
		String projectName = getProjectName(sel);

		System.out.println(className + "\t" + fileName + "\t" + projectName);

		cmpProjectName(projectName, window);
		return null;
	}

	private void cmpProjectName(String projectName, IWorkbenchWindow window) {
		if (projectName.equals(MyProjectHandler.pjtName)) {
			System.out.println("一致！");
		} else {
			// System.err.println("構文解析をしてください。");
			MessageDialog.openInformation(shell, "error", "構文解析結果がありません。\n構文解析を行ってください");
		}
	}

	private String getClassName(String sel) {
		int end = sel.indexOf(".java");
		String temp = sel.substring(0, end);

		int i, start = 0;
		if ((i = temp.lastIndexOf(" ")) >= 0) {
			start = i + 1;
		} else if ((i = temp.lastIndexOf("[")) >= 0) {
			start = i + 1;
		} else {
			System.err.println("error!!");
		}
		String className = temp.substring(start, temp.length());

		return className;
	}

	private String getProjectName(String sel) {
		String projectName = "";
		int start = sel.lastIndexOf("[in ") + 4;
		// System.out.println(start);
		int end = sel.indexOf("]]");
		// System.out.println(end);

		if (start > 3 && end > -1) {
			projectName = sel.substring(start, end);
		} else {
			System.err.println("プロジェクト名取得失敗");
		}

		return projectName;
	}

}

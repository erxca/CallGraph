package controller;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import model.trace.Trace;

public class TraceHandler extends AbstractHandler {

	Shell shell = Display.getDefault().getActiveShell();
	private String classpath, classname, mainclass, projectname, projectpath;
	private String args = "";
	private String option, mainargs; // -classpath "メインクラスファイルのある場所のパス" 引数

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveMenuSelectionChecked(event);
		String sel = selection.toString();
		// System.out.println(sel);

		initialSetting(sel);

		MyTraceDialog dialog = new MyTraceDialog(shell);
		int ret = dialog.open();

		// System.out.println(pjtName);
		if (ret == IDialogConstants.OK_ID) {
			// [OK]ボタン押下
			// args = dialog.getArgs();
			// System.out.println(args);
			//
			// Trace呼ぶ
			new Trace(option, mainargs);
			// } else if (ret == IDialogConstants.CANCEL_ID) {
			// // [Cancel]ボタン押下
		}

		return null;
	}

	private void initialSetting(String sel) {
		setProjectName(sel);
		// System.out.println("projectname : " + projectname);

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();

		IProject project = root.getProject(projectname);
		projectpath = project.getLocation().toOSString();
		// System.out.println("path : " + project.getLocation().toOSString());

		String packagename = setPackageName(sel);

		setMainClass(sel, packagename);
		// System.out.println("mainclass : " + mainclass);
		setClassPath(sel, packagename);
		// System.out.println("classpath : " + classpath);

		setOption();
		// System.out.println("option : " + option);

		setMainArgs();
		// System.out.println(mainargs);
	}

	private void setProjectName(String sel) {
		int idx = sel.indexOf("]");
		projectname = sel.substring(1, idx);

		if ((idx = projectname.lastIndexOf(" ")) > 0) {
			projectname = projectname.substring(idx + 1, projectname.length());
		}

	}

	private String setPackageName(String sel) {
		sel = sel.substring(sel.indexOf("package") + 8);

		return sel.substring(0, sel.indexOf("\n"));
	}

	private void setMainClass(String sel, String packagename) {
		int end = sel.indexOf(".java");
		sel = sel.substring(0, end);

		int i, start = 0;
		if ((i = sel.lastIndexOf(" ")) >= 0) {
			start = i + 1;
		} else if ((i = sel.lastIndexOf("[")) >= 0) {
			start = i + 1;
			// } else {
			// System.err.println("error!!");
		}
		classname = sel.substring(start, sel.length());

		StringBuffer mainsb = new StringBuffer();
		mainsb.append(packagename);
		mainsb.append(".");
		mainsb.append(classname);

		mainclass = mainsb.toString();
	}

	private void setClassPath(String sel, String packagename) {
		sel = sel.substring(sel.indexOf(packagename) + packagename.length(), sel.indexOf("\n"));
		sel = sel.substring(0, sel.lastIndexOf("[in "));
		// System.out.println(sel);

		StringBuffer tempPath = new StringBuffer();

		int idx = 0;
		while ((idx = sel.lastIndexOf("[in ")) > 0) {
			tempPath.append("\\");
			int end = sel.lastIndexOf(" ");
			String dir = sel.substring(idx + 4, end);

			if (dir.equals("src")) {
				tempPath.append("bin");
			} else {
				tempPath.append(dir);
			}

			sel = sel.substring(0, idx);
		}

		StringBuffer pathsb = new StringBuffer();
		pathsb.append(projectpath);
		pathsb.append(tempPath.toString());
		classpath = pathsb.toString();

	}

	private void setOption() {
		StringBuffer opsb = new StringBuffer();

		opsb.append("-classpath \"");
		opsb.append(classpath);
		opsb.append("\"");

		option = opsb.toString();
	}

	private void setMainArgs() {
		StringBuffer masb = new StringBuffer();

		masb.append(mainclass);
		masb.append(" ");
		masb.append(args);

		mainargs = masb.toString();
	}
}

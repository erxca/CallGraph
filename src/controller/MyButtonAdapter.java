package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class MyButtonAdapter extends MouseAdapter {
	String classPath;
	IWorkbenchPage page;

	public MyButtonAdapter(String classPath) {
		this.classPath = classPath;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		Display display = workbench.getDisplay();

		display.syncExec(new Runnable() {
			@Override
			public void run() {

				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				page = window.getActivePage();
				openSorceCode(classPath);

			}
		});
	}

	public void openSorceCode(String filePath) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		IFile file = root.getFile(new Path(filePath));
		// System.out.println(file.toString());

		try {
			IDE.openEditor(page, file);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

package controller;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class MyButtonHandler {
	IWorkbench workbench = PlatformUI.getWorkbench();
	IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
	IWorkbenchPage page = window.getActivePage();

	public void openSorceCode() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		// IFile file = ~;
		// IEditorPart editorPart = page.openEditor(file);
	}
}

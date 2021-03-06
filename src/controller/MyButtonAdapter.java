package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.MarkerUtilities;

import model.Method;
import model.Parsing;

public class MyButtonAdapter extends MouseAdapter {
	Method m;
	IWorkbenchPage page;

	public MyButtonAdapter(Method m) {
		this.m = m;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		Display display = workbench.getDisplay();

		display.syncExec(new Runnable() {
			@Override
			public void run() {

				// System.out.println(path + " " + lineNum);
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				page = window.getActivePage();
				openSorceCode(m.getPath());
				markLine(m.getStartLine(), page);

				Parsing.olf.write("SOURCE\tdeclaration\t\t", m.getMethodName());
			}
		});
	}

	protected void openSorceCode(String filePath) {
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

	protected void markLine(int line, IWorkbenchPage page) {
		IEditorInput editorInput = page.getActiveEditor().getEditorInput();
		IResource resource = (IResource) editorInput.getAdapter(IResource.class);

		Map<String, Object> attributes = new HashMap<>();
		attributes.put(IMarker.LINE_NUMBER, new Integer(line));

		attributes.put(IMarker.TRANSIENT, true); // マーカーの永続化:true
		attributes.put(IMarker.PRIORITY, Integer.valueOf(IMarker.PRIORITY_NORMAL)); // マーカーの優先度:中
		attributes.put(IMarker.SEVERITY, Integer.valueOf(IMarker.SEVERITY_WARNING)); // マーカーの重要度:警告
		attributes.put(IMarker.LINE_NUMBER, line); // マーカーを表示させる行番号
		attributes.put(IMarker.MESSAGE, "hogehoge"); // マーカーに表示するメッセージ

		final String ID = "metricsplugin.views" + ".mymarker";

		try {
			IMarker marker = resource.createMarker(IMarker.TEXT);
			marker.setAttributes(attributes);
			IDE.gotoMarker(page.getActiveEditor(), marker);

			MarkerUtilities.createMarker(resource, attributes, ID); // マーカーを作成
			// marker.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

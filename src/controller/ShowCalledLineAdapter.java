package controller;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import model.Method;
import model.Parsing;

public class ShowCalledLineAdapter extends MyButtonAdapter {
	// Method m;
	ArrayList<Integer> calledLineList = new ArrayList<Integer>();
	String path;
	int listSize;
	int now = 0; // 今見てる行のリストでの番号
	// IWorkbenchPage page;

	public ShowCalledLineAdapter(Method m, ArrayList<Integer> calledLineList, String path) {
		// TODO Auto-generated constructor stub
		super(m);
		// this.m = m;
		this.calledLineList = calledLineList;
		listSize = calledLineList.size();

		this.path = path;
		// System.out.println(path);
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
				// openSorceCode(m.getPath());
				openSorceCode(path);
				// markLine(m.getStartLine(), page);
				if (now == listSize) {
					now = 0;
				}
				markLine(calledLineList.get(now), page);

				Parsing.olf.write("SOURCE\tcalled line  " + now + "/" + listSize + "\t", m.getMethodName());
				now++;
			}
		});
	}

	// public void openSorceCode(String filePath) {
	// IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	//
	// IFile file = root.getFile(new Path(filePath));
	// // System.out.println(file.toString());
	//
	// try {
	// IDE.openEditor(page, file);
	// } catch (PartInitException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// private void markLine(int line, IWorkbenchPage page) {
	// IEditorInput editorInput = page.getActiveEditor().getEditorInput();
	// IResource resource = (IResource) editorInput.getAdapter(IResource.class);
	//
	// Map<String, Object> attributes = new HashMap<>();
	// attributes.put(IMarker.LINE_NUMBER, new Integer(line));
	//
	// attributes.put(IMarker.TRANSIENT, true); // マーカーの永続化:true
	// attributes.put(IMarker.PRIORITY,
	// Integer.valueOf(IMarker.PRIORITY_NORMAL)); // マーカーの優先度:中
	// attributes.put(IMarker.SEVERITY,
	// Integer.valueOf(IMarker.SEVERITY_WARNING)); // マーカーの重要度:警告
	// attributes.put(IMarker.LINE_NUMBER, line); // マーカーを表示させる行番号
	// attributes.put(IMarker.MESSAGE, "hogehoge"); // マーカーに表示するメッセージ
	//
	// final String ID = "metricsplugin.views" + ".mymarker";
	//
	// try {
	// IMarker marker = resource.createMarker(IMarker.TEXT);
	// marker.setAttributes(attributes);
	// IDE.gotoMarker(page.getActiveEditor(), marker);
	//
	// MarkerUtilities.createMarker(resource, attributes, ID); // マーカーを作成
	// // marker.delete();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
}

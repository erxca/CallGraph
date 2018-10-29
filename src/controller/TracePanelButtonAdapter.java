package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import org.eclipse.ui.IWorkbenchPage;

import model.Class;
import model.Method;
import model.ToolVisitor;
import view.PaintComponents;

public class TracePanelButtonAdapter extends MouseAdapter {
	ArrayList<Class> classList = ToolVisitor.classList;
	Method m;
	Class focusClass;
	ArrayList<Method> methodList;

	public TracePanelButtonAdapter(Method m) {
		this.m = m;
		focusClass = null;
		methodList = null;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

		IWorkbenchPage page = TraceHandler.page;

		// GetInformation info = new GetInformation();
		getClassInfo(m.getDeclaringClassName(), page);
	}

	public void getClassInfo(String className, IWorkbenchPage page) {
		for (Class cls : classList) {
			String cName = cls.getClassName();
			// String cName =
			// pNameDotCName.substring(pNameDotCName.lastIndexOf(".") + 1);

			if (cName.equals(className)) {
				System.out.println(cName);
				focusClass = cls;
				methodList = cls.getMethodList();

				PaintComponents pc = new PaintComponents(view.PluginView.getF(), view.PluginView.getScp(), page, cls);
				pc.makeClassCps();
				return;
			}
		}
		System.out.println("一致するクラスがありません。");
	}
}

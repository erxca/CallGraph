package model.trace;

import java.util.ArrayList;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import model.Class;
import model.Method;
import model.ToolVisitor;
import view.PaintTraceView;

public class TraceVisualize {
	ArrayList<Class> classList = ToolVisitor.classList;
	Class focusClass;
	ArrayList<Method> methodList;

	public void visualize(String className, IWorkbenchPage page) {
		for (Class cls : classList) {
			String pNameDotCName = cls.getClassName();
			String cName = pNameDotCName.substring(pNameDotCName.lastIndexOf(".") + 1);

			if (cName.equals(className)) {
				System.out.println(cName);
				focusClass = cls;
				methodList = cls.getMethodList();

				try {
					page.showView("tool.test.views.TraceView");
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				PaintTraceView pc = new PaintTraceView(view.TraceView.getF(), view.TraceView.getScp(), page, cls);
				pc.setSelectedClass(className);
				pc.makeClassCps();
				return;
			}
		}
		System.out.println("一致するクラスがありません。");
	}
}

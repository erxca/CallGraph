package model;

import java.util.ArrayList;

import org.eclipse.ui.IWorkbenchPage;

import view.PaintComponents;

public class GetInformation {
	ArrayList<Class> classList = ToolVisitor.classList;
	Class focusClass;
	ArrayList<Method> methodList;

	public GetInformation() {
		focusClass = null;
		methodList = null;
	}

	// 取得したクラス名がクラスリストにあるか判定
	public void getClassInfo(String className, IWorkbenchPage page) {
		for (Class cls : classList) {
			String pNameDotCName = cls.className;
			String cName = pNameDotCName.substring(pNameDotCName.lastIndexOf(".") + 1);

			if (cName.equals(className)) {
				System.out.println(cName);
				focusClass = cls;
				methodList = cls.getMethodList();

				PaintComponents pc = new PaintComponents(view.PluginView.getF(), view.PluginView.getP(),
						view.PluginView.getScp(), page, cls);
				pc.makeClassCps();
				return;
			}
		}
		System.out.println("一致するクラスがありません。");
	}
}

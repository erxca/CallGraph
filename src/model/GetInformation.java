package model;

import java.util.ArrayList;

public class GetInformation {
	ArrayList<Class> classList = ToolVisitor.classList;
	Class focusClass;
	ArrayList<Method> methodList;

	public GetInformation() {
		focusClass = null;
		methodList = null;
	}

	public void getClassInfo(String className) {
		for (Class cls : classList) {
			if (cls.className.equals(className)) {
				focusClass = cls;
				methodList = cls.getMethodList();
			} else {
				System.err.println("一致するクラスがありません。");
			}
		}
	}
}

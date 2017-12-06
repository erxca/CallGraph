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

	// 取得したクラス名がクラスリストにあるか判定
	public void getClassInfo(String className) {
		for (Class cls : classList) {
			String pNameDotCName = cls.className;
			String cName = pNameDotCName.substring(pNameDotCName.lastIndexOf(".") + 1);

			if (cName.equals(className)) {
				System.out.println(cName);
				focusClass = cls;
				methodList = cls.getMethodList();
				return;
			}
		}
		System.out.println("一致するクラスがありません。");
	}
}

package makeCallGraph;

import java.util.ArrayList;

public class Class {
	private String className;
	private String classPath;
	// private String extendsClassName;
	private ArrayList<String> extendsClassNames = new ArrayList<String>();// このクラスが継承しているクラスリスト

	public Class(String className, String classPath, String extendsClassName) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.className = className;
		this.classPath = classPath;
		System.out.println("classPath " + classPath);
		// this.extendsClassName = extendsClassName;
		if (extendsClassName != null) {
			addList(extendsClassName);
		}
	}

	public void addList(String extendsClassName) {
		extendsClassNames.add(extendsClassName);
	}

	public ArrayList<String> getExtendsClassNames() {
		return extendsClassNames;
	}

	public String getClassName() {
		return className;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	// public String getExtendsClassName() {
	// return extendsClassName;
	// }
	//
	// public void setExtendsClassName(String extendsClassName) {
	// this.extendsClassName = extendsClassName;
	// }
}

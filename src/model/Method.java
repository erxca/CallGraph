package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.ITypeBinding;

public class Method {
	String methodName;
	String declaringClassName;
	String modifier;
	String path;
	ArrayList<String> parametersList = new ArrayList<String>();
	ArrayList<Method> methodCallList = new ArrayList<Method>();
	Set<Method> methodCallSet = new HashSet<>();

	Method(String methodName, String declaringClassName, String modifier) {
		this.methodName = methodName;
		this.declaringClassName = declaringClassName;
		this.modifier = modifier;
		this.path = null;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String pkgPath) {
		String className = declaringClassName.substring(declaringClassName.lastIndexOf(".") + 1);
		StringBuffer sb = new StringBuffer();
		sb.append(pkgPath);
		sb.append("/");
		sb.append(className);
		sb.append(".java");
		this.path = sb.toString();
	}

	public void addParaList(ITypeBinding[] paras) {
		for (ITypeBinding itb : paras) {
			parametersList.add(itb.getName());
		}
	}

	public ArrayList<Method> getMethodCallList() {
		return methodCallList;
	}

	public String getDeclaringClassName() {
		return declaringClassName;
	}

	public void list2Set() {
		for (Method method : methodCallList) {
			methodCallSet.add(method);
		}
	}

	public Set<Method> getMethodCallSet() {
		return methodCallSet;
	}

	public void showMethod() {
		System.out.println("\n*** Show Method ***");
		System.out.println("ClassName\t\t: " + declaringClassName);
		System.out.println("MethodName\t\t: " + methodName);
		System.out.println("Modifier\t\t: " + modifier);
		// System.out.println("Parameters\t\t: " + parameters);
		if (parametersList.size() != 0) {
			System.out.print("Parameters\t\t: ");
			for (String parameter : parametersList) {
				System.out.print(parameter + ", ");
			}
			System.out.println();
		}
		System.out.println("***\t\t\t\t***");
	}

	public void showCallList() {
		System.out.print(declaringClassName + "." + methodName + " Called\t: ");
		for (Method method : methodCallList) {
			System.out.print(method.declaringClassName + "." + method.methodName + ", ");
		}
		System.out.println();
	}
}

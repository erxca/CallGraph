package getClassName;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ITypeBinding;

public class Class {
	String className;
	boolean isInterface;
	ArrayList<Method> methodList = new ArrayList<Method>();
	String modifiers;
	String superClass = null;
	ArrayList<String> interfaces = new ArrayList<String>();

	Class(String className, boolean isInterface, String modifiers, String superClass) {
		this.className = className;
		this.isInterface = isInterface;
		this.modifiers = modifiers;
		this.superClass = superClass;
	}

	public void addInterfaces(ITypeBinding[] interfaces) {
		for (int i = 0; i < interfaces.length; i++) {
			// System.out.println(interfaces[i].getBinaryName());
			this.interfaces.add(interfaces[i].getBinaryName());
		}
	}

	public ArrayList<Method> getMethodList() {
		return methodList;
	}

	public void addMethodList(Method method) {
		methodList.add(method);
	}

	public void showClass() {
		System.out.println("\n\n*** show class ***");
		System.out.println("ClassName\t\t: " + className);
		if (isInterface) {
			System.out.println("ClassType\t\t: Interface");
		} else {
			System.out.println("ClassType\t\t: Class");
		}
		System.out.println("Modifiers\t\t: " + modifiers);
		if (superClass != null) {
			System.out.println("SuperClass\t\t: " + superClass);
		}
		if (interfaces.size() != 0) {
			System.out.print("interfaces\t\t: ");
			for (String iF : interfaces) {
				System.out.print(iF + " ");
			}
		}
		System.out.println("\n");
	}

	public void showMethodList() {
		System.out.print("\n" + className + "'s Method\t: ");
		for (Method method : methodList) {
			System.out.print(method.methodName + ", ");
		}
		System.out.println();
	}
}

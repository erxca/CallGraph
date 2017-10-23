package model;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ToolVisitor extends ASTVisitor {
	ArrayList<Class> classList = new ArrayList<Class>(); // クラスをまとめたリスト
	// メソッド呼び出しをまとめたリスト
	ArrayList<ArrayList<String>> methodCallList = new ArrayList<ArrayList<String>>();
	ArrayList<Method> methodList = new ArrayList<Method>();
	Class focusClass = null; // 今見ているクラス。クラス宣言があるたびに変わる
	Method focusMethod = null; // 今見ているメソッド。メソッド宣言があるたびに変わる

	public boolean visit(TypeDeclaration node) {
		String superClassName = null;

		if (focusClass != null) {
			focusClass.showMethodList();
		}
		// Print.printTitle("クラス宣言");

		ITypeBinding typeBinding = node.resolveBinding();// 詳細な情報をITypeBindingインスタンスを使って取得したい
		ITypeBinding superClass = typeBinding.getSuperclass();// 親クラスの取得
		ITypeBinding[] interfaces = typeBinding.getInterfaces();// インターフェースの取得
		String className = typeBinding.getBinaryName();// クラス名の取得
		int modifiers = typeBinding.getModifiers();// "public static"とかの識別子
		boolean isInterface = typeBinding.isInterface();

		// if (isInterface) {
		// Print.printMessage("InterfaceName", className);
		// } else {
		// Print.printMessage("ClassName", className);
		// }
		// Print.printModifiers("Modifiers", modifiers);
		// System.out.println(node.getModifiers());
		// System.out.println("修飾子 : " +
		// Modifier.toString(node.getModifiers()));
		if (superClass != null) {
			// Print.printMessage("SuperClass", superClass.getBinaryName());
			superClassName = superClass.getBinaryName();
		}
		// if (interfaces.length != 0) {
		// Print.printMessage("Interfaces", interfaces);
		// }

		Class clasS = new Class(className, isInterface, Modifier.toString(modifiers), superClassName);
		if (interfaces.length != 0) {
			clasS.addInterfaces(interfaces);
		}
		clasS.showClass();

		classList.add(clasS);
		focusClass = clasS;

		return super.visit(node);
	}

	/**
	 * メソッド宣言が見つかると呼ばれるメソッド
	 */
	public boolean visit(MethodDeclaration node) {
		if (focusMethod != null) {
			focusMethod.showCallList();
		}
		// Print.printTitle("メソッド宣言");
		// System.out.println("クラス名 : " +
		// node.resolveBinding().getDeclaringClass().getBinaryName());
		// Print.printMessage("MethodName",
		// node.getName().getFullyQualifiedName());
		// Print.printModifiers("Modifiers", node.getModifiers());
		// System.out.println(node.getModifiers());
		// System.out.println("修飾子 : " +
		// Modifier.toString(node.getModifiers()));
		// Print.printMessage("ReturnType", node.getReturnType2() + "");
		// Print.printMessage("Parameters", node.parameters().toString());

		String methodName = node.getName().getFullyQualifiedName();
		String declaringClassName = node.resolveBinding().getDeclaringClass().getBinaryName();
		String modifier = Modifier.toString(node.getModifiers());

		Method method = new Method(methodName, declaringClassName, modifier);
		method.addParaList(node.resolveBinding().getParameterTypes());
		methodList.add(method);

		method.showMethod();
		focusClass.addMethodList(method);
		focusMethod = method;

		return super.visit(node);
	}

	public boolean visit(MethodInvocation node) {
		IMethodBinding methodBinding = node.resolveMethodBinding();
		// System.out.println(methodBinding.getDeclaringClass());
		String methodName = methodBinding.getName();
		String declaringClassName = methodBinding.getDeclaringClass().getBinaryName();

		Method method = new Method(methodName, declaringClassName, null);
		method.addParaList(methodBinding.getParameterTypes());

		focusMethod.methodCallList.add(method);
		// System.out.println("メソッドのクラスが知りたい : " +
		// methodBinding.getDeclaringClass().getBinaryName());
		// System.out.println("メソッド名が知りたい : " + methodBinding.getName());
		// System.out.println("メソッドの引数の数: " +
		// methodBinding.getParameterTypes().length);
		// for (ITypeBinding itb : methodBinding.getParameterTypes()) {
		// System.out.println("itbの値だよ : " + itb.getName().toString());
		// }
		return super.visit(node);

	}
}

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
	public static ArrayList<Class> classList = new ArrayList<Class>(); // クラスをまとめたリスト
	// メソッド呼び出しをまとめたリスト
	ArrayList<ArrayList<String>> methodCallList = new ArrayList<ArrayList<String>>();
	ArrayList<Method> methodList = new ArrayList<Method>();
	Class focusClass = null; // 今見ているクラス。クラス宣言があるたびに変わる
	Method focusMethod = null; // 今見ているメソッド。メソッド宣言があるたびに変わる

	public boolean visit(TypeDeclaration node) {
		String superClassName = null;

		// if (focusClass != null) {
		// focusClass.showMethodList();
		// }

		ITypeBinding typeBinding = node.resolveBinding();// 詳細な情報をITypeBindingインスタンスを使って取得したい
		ITypeBinding superClass = typeBinding.getSuperclass();// 親クラスの取得
		ITypeBinding[] interfaces = typeBinding.getInterfaces();// インターフェースの取得
		String className = typeBinding.getBinaryName();// クラス名の取得
		int modifiers = typeBinding.getModifiers();// "public static"とかの識別子
		boolean isInterface = typeBinding.isInterface();

		if (superClass != null) {
			superClassName = superClass.getBinaryName();
		}

		Class clasS = new Class(className, isInterface, Modifier.toString(modifiers), superClassName);
		if (interfaces.length != 0) {
			clasS.addInterfaces(interfaces);
		}
		// clasS.showClass();

		classList.add(clasS);
		focusClass = clasS;

		return super.visit(node);
	}

	/**
	 * メソッド宣言が見つかると呼ばれるメソッド
	 */
	public boolean visit(MethodDeclaration node) {
		// if (focusMethod != null) {
		// focusMethod.showCallList();
		// }

		String methodName = node.getName().getFullyQualifiedName();
		String declaringClassName = node.resolveBinding().getDeclaringClass().getBinaryName();
		String modifier = Modifier.toString(node.getModifiers());

		Method method = new Method(methodName, declaringClassName, modifier);
		method.addParaList(node.resolveBinding().getParameterTypes());
		methodList.add(method);

		// method.showMethod();
		focusClass.addMethodList(method);
		focusMethod = method;

		return super.visit(node);
	}

	public boolean visit(MethodInvocation node) {
		IMethodBinding methodBinding = node.resolveMethodBinding();
		String methodName = methodBinding.getName();
		String declaringClassName = methodBinding.getDeclaringClass().getBinaryName();

		Method method = new Method(methodName, declaringClassName, null);
		method.addParaList(methodBinding.getParameterTypes());

		focusMethod.methodCallList.add(method);
		return super.visit(node);

	}
}

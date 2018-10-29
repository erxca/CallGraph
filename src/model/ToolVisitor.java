package model;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ToolVisitor extends ASTVisitor {
	public static ArrayList<Class> classList = new ArrayList<Class>(); // クラスをまとめたリスト
	// メソッド呼び出しをまとめたリスト
	ArrayList<ArrayList<String>> methodCallList = new ArrayList<ArrayList<String>>();
	static ArrayList<Method> methodList = new ArrayList<Method>();
	Class focusClass = null; // 今見ているクラス。クラス宣言があるたびに変わる
	Method focusMethod = null; // 今見ているメソッド。メソッド宣言があるたびに変わる

	String packageName;

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean visit(TypeDeclaration node) {
		String superClassName = null;

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

		int start = node.getStartPosition();
		method.setStart(start);

		if (node.isConstructor()) {
			method.setConstructor(true);
		}

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

		// method.setCalledCharNum(node.getStartPosition());
		// System.out.println(methodName + " " + node.getStartPosition());

		focusMethod.callLineList.add(new CalledLinelSet(methodName, node.getStartPosition()));

		focusMethod.methodCallList.add(method);
		return super.visit(node);

	}

	public boolean visit(ClassInstanceCreation node) {

		IMethodBinding cnstBinding = node.resolveConstructorBinding();
		String cnstName = cnstBinding.getName();
		String parentClassName = cnstBinding.getDeclaringClass().getBinaryName();

		// System.out.println(cnstName + " " + parentClassName);
		Method method = new Method(cnstName, parentClassName, null);
		method.addParaList(cnstBinding.getParameterTypes());
		method.setConstructor(true);

		// System.out.println(packageName);
		// System.out.println("const : " + method.methodName);
		if (parentClassName.startsWith(packageName)) {
			focusMethod.methodCallList.add(method);
			focusMethod.callLineList.add(new CalledLinelSet(cnstName, node.getStartPosition()));
		}

		return super.visit(node);

	}

	public boolean visit(SuperMethodInvocation node) {

		IMethodBinding superBinding = node.resolveMethodBinding();
		String superName = superBinding.getName();
		String declaringClassName = superBinding.getDeclaringClass().getBinaryName();

		Method method = new Method(superName, declaringClassName, null);
		method.addParaList(superBinding.getParameterTypes());

		focusMethod.callLineList.add(new CalledLinelSet(superName, node.getStartPosition()));
		focusMethod.methodCallList.add(method);

		return super.visit(node);
	}

	public boolean visit(SuperConstructorInvocation node) {

		IMethodBinding superCnstBinding = node.resolveConstructorBinding();
		String superName = superCnstBinding.getName();
		String declaringClassName = superCnstBinding.getDeclaringClass().getBinaryName();
		// System.out.println(focusMethod.methodName);

		Method method = new Method(superName, declaringClassName, null);
		method.addParaList(superCnstBinding.getParameterTypes());

		focusMethod.callLineList.add(new CalledLinelSet(superName, node.getStartPosition()));
		// System.out.println("focusMethod : " + focusMethod.methodName + " " +
		// node.getStartPosition());
		focusMethod.methodCallList.add(method);

		return super.visit(node);
	}

	public static ArrayList<Method> getMethodList() {
		return methodList;
	}
}

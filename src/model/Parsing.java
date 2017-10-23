package model;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Parsing {
	private static ToolVisitor visitor = new ToolVisitor();
	static ArrayList<String> srcList = new ArrayList<String>();
	private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";

	public void getWorkspaceInfo(String packageName) throws JavaModelException {
		// ワークスペースの参照を取得する
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();

		// プロジェクトへの参照を取得する
		// IProject[] projects = root.getProjects();
		IProject project = root.getProject(packageName);

		// System.out.println(project.exists());
		// System.out.println(projects.length);
		// for (IProject project : projects) {
		try {
			if (project.isNatureEnabled(JDT_NATURE)) {
				System.out.println("Project Name : " + project.getName());

				analyseMethods(project);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		// }
		checkMethodCallList();
	}

	private void analyseMethods(IProject project) throws JavaModelException {
		IPackageFragment[] packages = JavaCore.create(project).getPackageFragments();
		// // parse(JavaCore.create(project));

		for (IPackageFragment mypackage : packages) {

			// パッケージの中身がソースコードのとき
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
				System.out.println("Package Name : " + mypackage.getElementName());
				createAST(mypackage);
			}

		}
	}

	private void createAST(IPackageFragment mypackage) throws JavaModelException {
		for (ICompilationUnit unit : mypackage.getCompilationUnits()) {

			// ICompilationUnits用のASTを作成
			CompilationUnit parse = parse(unit);

			parse.accept(visitor);

			System.out.println("***\t\tEND\t\t***");
		}
	}

	private void checkMethodCallList() {
		for (Method method : visitor.methodList) { // 各メソッドのメソッド呼び出しリストをチェック。メソッドリストに一致するものがあれば入れ替える
			java.util.Iterator<Method> itr = method.methodCallList.iterator();
			while (itr.hasNext()) {
				Method mthd = itr.next();

				boolean isExist = false;
				for (Method method2 : visitor.methodList) {
					if (mthd.methodName.equals(method2.methodName)
							&& mthd.declaringClassName.equals(method2.declaringClassName)
							&& mthd.parametersList.equals(method2.parametersList)) {

						method.methodCallList.set(method.methodCallList.indexOf(mthd), method2);
						isExist = true;
						break;
					}
				}

				if (!isExist) {
					System.out.println("delete this method : " + mthd.methodName);
					itr.remove();
				}
			}
			method.showCallList();
		}
	}

	private static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null); // パース
	}

}

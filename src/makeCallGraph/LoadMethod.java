package makeCallGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

public class LoadMethod {
	static ArrayList<ArrayList<String>> callList = new ArrayList<ArrayList<String>>(); // メソッド呼び出しをまとめたリスト
	static ArrayList<MethodDetail> methodList = new ArrayList<MethodDetail>(); // メソッド一覧
	static ArrayList<String> classPathList = new ArrayList<String>(); // 各クラスのパスをまとめたリスト
	static ArrayList<ClassOrInterface> classAndInterfaceList = new ArrayList<ClassOrInterface>(); // クラスとインターフェース両方のリスト
	// static ArrayList<ClassOrInterface> interfaceList = new
	// ArrayList<ClassOrInterface>(); // インターフェースのリスト
	// private ArrayList<ClassOrInterface> classList = new
	// ArrayList<ClassOrInterface>(); // クラスのリスト
	private String str;
	private final String BEFORE_NAME = "label=\"";
	private final String AFTER_NAME = "\",height";
	private final String HTML_DIR = ".\\workspace\\data\\html";
	private final String HTMLFILE_DIR = ".\\workspace\\data\\htmlfile";
	private final String DOT_DIR = ".\\workspace\\data\\dotfile";
	private File html = new File(HTML_DIR);
	private File htmlFile = new File(HTMLFILE_DIR);
	private File dotFile = new File(DOT_DIR);

	public LoadMethod() {
		if (!dotFile.exists()) {
			System.out.println("dotfile null");
			makeDotDir();
		}
		loadDir();
		if (!htmlFile.exists()) {
			makeHtmlFile();
		}
		try {
			setReturnValue();
		} catch (IOException e) {
			e.printStackTrace();
		}
		registClassPath(new File(".\\workspace\\data\\src\\"));
		checkSrc();
		regImpClassesHadChildIf();
		regImpClasses(classAndInterfaceList);

		for (ClassOrInterface interface1 : regImpClasses(classAndInterfaceList)) {
			classAndInterfaceList.add(interface1);
		}

		for (ClassOrInterface iF : classAndInterfaceList) {
			iF.setPath(iF.getFilePath());
			for (ClassOrInterface cOrI : iF.getImplementsList()) {
				cOrI.setPath(cOrI.getFilePath());
			}
			for (ClassOrInterface cOrI : iF.getExtendsList()) {
				cOrI.setPath(cOrI.getFilePath());
			}
		}
		for (ClassOrInterface iF : classAndInterfaceList) {
			if (!iF.isClass()) {
				System.out.println(iF.getName() + " " + iF.getFilePath());
				for (ClassOrInterface cOrI : iF.getImplementsList()) {
					System.out.println("\t" + cOrI.getName() + " " + cOrI.getFilePath());
				}
			}
		}
	}

	private void makeDotDir() { // Dotファイルをまとめたディレクトリを作成
		dotFile.mkdir();
		String[] dotFiles = html.list(new MyFilter());
		for (int i = 0; i < dotFiles.length; i++) {
			StringBuilder inFileName = new StringBuilder();
			inFileName.append(html);
			inFileName.append("\\");
			inFileName.append(dotFiles[i]);
			File inFile = new File(inFileName.toString());

			StringBuilder outFileName = new StringBuilder();
			outFileName.append(dotFile);
			outFileName.append("\\");
			outFileName.append(dotFiles[i]);
			File outFile = new File(outFileName.toString());

			inFile.renameTo(outFile);
		}
	}

	private void makeHtmlFile() { // メソッドの情報を得るのに必要なｈｔｍｌファイルをまとめたディレクトリを作成
		htmlFile.mkdir();
		String[] htmlFiles = html.list(new MyFilter2());
		for (int i = 0; i < htmlFiles.length; i++) {
			StringBuilder inFileName = new StringBuilder();
			inFileName.append(html);
			inFileName.append("\\");
			inFileName.append(htmlFiles[i]);
			File inFile = new File(inFileName.toString());

			StringBuilder outFileName = new StringBuilder();
			outFileName.append(htmlFile);
			outFileName.append("\\");
			outFileName.append(htmlFiles[i]);
			File outFile = new File(outFileName.toString());

			inFile.renameTo(outFile);
		}
	}

	public void loadDir() { // dotファイルから、各メソッドが呼び出しているメソッドの一段階分を取得してリストへ追加
		String firstNodeName = null;
		String calledNodeName = null;
		File[] dotFiles = dotFile.listFiles();

		for (File dFile : dotFiles) {
			ArrayList<String> haveMethodsList = new ArrayList<String>();
			boolean firstNode = false;
			try {
				BufferedReader br = new BufferedReader(new FileReader(dFile));
				while ((str = br.readLine()) != null) {
					if (str.matches("  Node[0-9]+ \\[.*") && firstNode == false) { // ファイル名が示しているメソッドである最初のノードを見つける
						firstNodeName = registNode(2, 8, firstNodeName);
						firstNode = true;
						String methodPath = extractMethodName(str);
						if (haveMethodsList.indexOf(methodPath) == -1) {
							haveMethodsList.add(methodPath);
						}
					} else if (firstNode == true && str.startsWith("  " + firstNodeName + " ")) { // 最初のノードのメソッド呼び出しを示す行を見つけ、呼び出されたメソッド名のノードを登録
						int start2 = str.indexOf("-> Node");
						calledNodeName = registNode(start2 + 3, start2 + 9, calledNodeName);
						BufferedReader br2 = new BufferedReader(new FileReader(dFile));
						boolean nodeFound = false;
						String str2;
						while ((str2 = br2.readLine()) != null && nodeFound == false) {// 呼び出されたノードのメソッド名を探してリストに登録
							if (str2.startsWith("  " + calledNodeName + " [")) {
								nodeFound = true;
								String methodName = extractMethodName(str2);
								haveMethodsList.add(methodName);
							}
						}
						br2.close();
					}
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			callList.add(haveMethodsList);
		}
	}

	private void registClassPath(File file) {
		File[] files = file.listFiles();
		if (files == null) {
			return;
		}
		for (File f : files) {
			if (!f.exists()) {
				continue;
			} else if (f.isDirectory()) {
				registClassPath(f);
			} else if (f.isFile()) {
				int index = f.toString().lastIndexOf(".");

				String ext = f.toString().substring(index + 1).toLowerCase();
				if (ext.equals("java") == true) {
					classPathList.add(f.toString());
				}
			}
		}
	}

	private String registNode(int beginIndex, int endIndex, String nodeName) { // ノード番号を登録
		if (str.substring(beginIndex, endIndex).matches("Node[0-9] ")) {
			nodeName = str.substring(beginIndex, endIndex - 1);
		} else if (str.substring(beginIndex, endIndex + 1).matches("Node[0-9][0-9] ")) {
			nodeName = str.substring(beginIndex, endIndex);
		} else if (str.substring(beginIndex, endIndex + 2).matches("Node[0-9][0-9][0-9] ")) {
			nodeName = str.substring(beginIndex, endIndex + 1);
		}
		return nodeName;
	}

	private String extractMethodName(String str) { // メソッド名を抜き出す
		int startName = str.indexOf(BEFORE_NAME) + 7;
		int endName = str.indexOf(AFTER_NAME);
		String methodName = str.substring(startName, endName);
		methodName = removeL(methodName);
		return methodName;
	}

	private String removeL(String tempMethodName) { // メソッド名中に\lがある場合除去
		int j = tempMethodName.indexOf("\\l");
		while (j != -1) {
			StringBuilder sbMethodName = new StringBuilder(tempMethodName);
			sbMethodName.delete(j, j + 2);
			tempMethodName = sbMethodName.toString();
			j = tempMethodName.indexOf("\\l");
		}
		return tempMethodName;
	}

	private void setReturnValue() throws IOException { // 返り値登録用
		String[] htmlFileNames = htmlFile.list();
		File[] htmlFiles = htmlFile.listFiles();

		for (int i = 0; i < htmlFileNames.length; i++) {
			checkLine(htmlFiles[i], makeMethodPath(htmlFileNames[i]));
		}
	}

	private StringBuilder makeMethodPath(String file) { // メソッドへのパスを生成
		StringBuilder sb = new StringBuilder(file);

		int j = sb.toString().indexOf("_1_1"); // 区切り記号を削除し、.を挿入
		while (j != -1) {
			sb.insert(j, ".");
			sb.delete(j + 1, j + 5);
			j = sb.toString().indexOf("_1_1");
		}

		j = sb.indexOf("_"); // もともと大文字だった小文字を大文字に直す
		while (j != -1) {
			String low = sb.substring(j + 1, j + 2);
			sb.delete(j, j + 2);
			sb.insert(j, low.toUpperCase());
			j = sb.indexOf("_");
		}

		if (file.startsWith("class")) { // 先頭、末尾の邪魔な文字を削除
			sb.delete(0, 5);
		} else if (file.startsWith("interface")) {
			sb.delete(0, 9);
		}
		sb.delete(sb.length() - 5, sb.length());
		return sb;
	}

	private void checkLine(File file, StringBuilder sb) throws IOException { // メソッド宣言の行を探す
		int cname;
		StringBuilder sb1 = new StringBuilder(sb);
		sb1.append(".");
		StringBuilder sb2 = new StringBuilder(sb);
		sb2.append("<");

		BufferedReader br = new BufferedReader(new FileReader(file));
		while ((str = br.readLine()) != null) {
			if ((cname = str.indexOf(sb1.toString())) != -1) {
				findReturnValue(cname);
			} else if ((cname = str.indexOf(sb2.toString())) != -1) {
				findReturnValue(cname);
			}
		}
		br.close();
	}

	private void findReturnValue(int cname) { // 返り値を探す
		if (str.indexOf(" </td>") != -1) {
			String methodPath = str.substring(cname, str.indexOf(" </td>"));
			if (methodPath.indexOf("</a>") != -1) {
				StringBuilder buf = new StringBuilder(methodPath);
				buf.delete(methodPath.indexOf("</a>"), methodPath.lastIndexOf("."));
				methodPath = buf.toString();
				cname = str.indexOf(" <a ");
			}

			MethodDetail metMod = new MethodDetail(methodPath);
			if ((cname - 1) > (str.indexOf("\">") + 2)) {
				String retValue = str.substring(str.indexOf("\">") + 2, cname - 1);
				if (retValue.indexOf("</a>") != -1) {
					retValue = retValue.substring(retValue.indexOf("\">") + 2, retValue.indexOf("</a>"));
				}
				metMod.setReturnValue(retValue);
			}
			methodList.add(metMod);
		}
	}

	private void checkSrc() {
		for (String classPath : classPathList) {
			try {
				checkClassNameLine(classPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void checkClassNameLine(String classPath) throws IOException {
		int count = 0;
		int cutStr = 0;
		BufferedReader br = new BufferedReader(new FileReader(classPath));
		String className = classPath.substring(classPath.lastIndexOf("\\") + 1, classPath.lastIndexOf("."));

		while (count == 0 && (str = br.readLine()) != null) {
			if ((cutStr = str.indexOf(className)) != -1) { // クラス名の行に注目
				if (str.indexOf("interface") != -1) { // インターフェースのとき
					checkInterfaceInfo(className, classPath, cutStr);
				} else if (str.indexOf("class") != -1) { // クラスのとき
					ClassOrInterface cls = new ClassOrInterface(className, classPath, true);

					cutStr += className.length();
					if (str.substring(cutStr, cutStr + 1).equals("<")) {
						cutStr += str.substring(cutStr).indexOf(">") + 1;
					}
					cutStr = skipBrank(str.substring(cutStr, cutStr + 1), cutStr);
					if (str.substring(cutStr).indexOf("extends") != -1) {
						cutStr += str.substring(cutStr).indexOf("extends");
						cutStr += 7;
						cutStr = skipBrank(str.substring(cutStr, cutStr + 1), cutStr);
						checkExtendsClassName(cutStr, cls);
					}

					if ((cutStr = str.indexOf("implements")) != -1) {
						checkInterfaceName(cutStr, cls);
					}
				}
				count++;
			}
		}
		br.close();
	}

	private void checkInterfaceInfo(String className, String classPath, int cutStr) {
		boolean hasRegisted = false; // インターフェース名がすでに登録されているか
		ClassOrInterface interF = null;
		for (ClassOrInterface iF : classAndInterfaceList) {
			if (!iF.isClass() && iF.getName().equals(className)) { // インターフェース登録済み
				interF = iF;
				hasRegisted = true;
				if (iF.getFilePath() == null) {
					iF.setFilePath(classPath);
				}
				break;
			}
		}
		if (!hasRegisted) { // インターフェース未登録
			ClassOrInterface iF = new ClassOrInterface(className, classPath, false);
			interF = iF;
			classAndInterfaceList.add(iF);
		}
		if (str.substring(cutStr).indexOf("extends") != -1) {
			cutStr += str.substring(cutStr).indexOf("extends");
			cutStr += 7;
			cutStr = skipBrank(str.substring(cutStr, cutStr + 1), cutStr);
			checkExtendsInterfaceName(cutStr, interF);
		}
	}

	private void checkExtendsInterfaceName(int cutStr, ClassOrInterface interF) {
		String interfaceName = null;
		boolean next = false;
		boolean isExist = false;
		ClassOrInterface iF = null;

		// System.out.println("LM 372");
		for (int i = 1; i < str.substring(cutStr).length(); i++) {
			if (!str.substring(cutStr, cutStr + i).matches("[a-zA-Z0-9]+")) {
				interfaceName = str.substring(cutStr, cutStr + i - 1);
				for (ClassOrInterface iF2 : classAndInterfaceList) {
					if (!iF2.isClass() && iF2.getName().equals(interfaceName)) {
						iF = iF2;
						isExist = true;
						break;
					}
				}
				if (!isExist) {
					iF = new ClassOrInterface(interfaceName, null, false);
					classAndInterfaceList.add(iF);
				}
				interF.addExtendsList(iF);
				iF.addImplementsList(interF); // これは必要なのか？
				// System.out.println("LM 357 " + interF.getName() + " "
				// + iF.getName());
				for (int i2 = 1; i2 < str.substring(cutStr + i).length(); i2++) {
					if (!str.substring(cutStr + i, cutStr + i + i2).matches("[,\\s]+")) {
						if (!next) {
							break;
						}
						checkExtendsInterfaceName(cutStr + i + i2 - 1, interF);
						break;
					} else if (str.substring(cutStr + i + i2 - 2, cutStr + i + i2 - 1).equals(",")) {
						next = true;
					}
				}
				break;
			}
		}
	}

	private void checkExtendsClassName(int cutStr, ClassOrInterface cls) {
		String extendsClass = null;
		boolean next = false;
		boolean hasRegised = false;

		for (int i = 1; i < str.substring(cutStr).length(); i++) {
			if (!str.substring(cutStr, cutStr + i).matches("[a-zA-Z0-9]+")) {
				extendsClass = str.substring(cutStr, cutStr + i - 1);
				ClassOrInterface clasS = new ClassOrInterface(extendsClass, null, true);
				cls.addExtendsList(clasS);
				for (int i2 = 0; i2 < str.substring(cutStr + i).length(); i++) {
					if (!str.substring(cutStr + i, cutStr + i + i2).matches("[, ]+")) {
						if (!next) {
							break;
						}
						checkExtendsClassName(cutStr + i + i2, cls);
					} else if (str.substring(cutStr + i + i2 - 1, cutStr + i + i2).equals(",")) {
						next = true;
					}
				}
				break;
			}
		}
		for (ClassOrInterface c : classAndInterfaceList) {
			if (c.isClass() && c.getName().equals(cls.getName())) {
				hasRegised = true;
			}
		}
		if (!hasRegised) {
			classAndInterfaceList.add(cls);
		}
	}

	private void checkInterfaceName(int cutStr, ClassOrInterface cls) {
		String interfaceName = null;
		boolean hasRegisted = false; // インターフェース名がすでに登録されているか
		cutStr += 10;
		cutStr = skipBrank(str.substring(cutStr, cutStr + 1), cutStr);
		while (true) {
			for (int i = 1; i < str.substring(cutStr).length(); i++) {
				if (!str.substring(cutStr, cutStr + i).matches("[a-zA-Z0-9.]+")) {
					interfaceName = str.substring(cutStr, cutStr + i - 1);
					if (interfaceName.lastIndexOf(".") != -1) {
						interfaceName = interfaceName.substring(interfaceName.lastIndexOf(".") + 1);
					}
					for (ClassOrInterface iF : classAndInterfaceList) {
						if (!iF.isClass() && iF.getName().equals(interfaceName)) {
							iF.addImplementsList(cls);
							hasRegisted = true;
							break;
						}
					}
					if (!hasRegisted) {
						// System.out.println("LM 450");
						ClassOrInterface iF = new ClassOrInterface(interfaceName, null, false);
						classAndInterfaceList.add(iF);
						iF.addImplementsList(cls);
					}
					cutStr += i - 1;
					if (str.substring(cutStr, cutStr + 1).equals("<")) {
						cutStr += str.substring(cutStr).indexOf(">") + 1;
					}
					break;
				}
			}
			cutStr = skipBrank(str.substring(cutStr, cutStr + 1), cutStr);
			if (str.substring(cutStr).indexOf(",") != -1) {
				cutStr += str.substring(cutStr).indexOf(",") + 2;
			} else {
				break;
			}
		}

	}

	private ArrayList<ClassOrInterface> regImpClasses(ArrayList<ClassOrInterface> iList) {
		ArrayList<ClassOrInterface> willBeInterfaceList = new ArrayList<ClassOrInterface>();
		for (ClassOrInterface iF : iList) {
			if (!iF.isClass()) {
				ArrayList<ClassOrInterface> willBeImplimentslist = new ArrayList<ClassOrInterface>();
				// System.out.println("LM 508 " + iF.getName() + " "
				// + iF.getFilePath() + " " + iF.getPath());
				for (ClassOrInterface impCls : iF.getImplementsList()) { // 今見ているインターフェースを実装しているクラスを順番に見る
					// System.out.println("LM 510 " + impCls.getName() + " "
					// + impCls.getFilePath() + " " + impCls.getPath());

					for (ClassOrInterface cls : classAndInterfaceList) {
						if (cls.isClass()) {
							for (ClassOrInterface extendsClass : cls.getExtendsList()) {
								// System.out.println("\tLM 524 " +
								// cls.getName()
								// + ":" + extendsClass.getName() + " "
								// + impCls.getName());
								if (extendsClass.getName().equals(impCls.getName())) { // インターフェース実装クラスと子クラスの親クラスが一致
									willBeImplimentslist.add(cls);
									addInterface(impCls, willBeInterfaceList);
									// System.out.println("\tLM 519 "
									// + cls.getName() + " "
									// + cls.getFilePath());
								}
							}
						}
					}
				}
				addImpList(iF, willBeImplimentslist);
			}
		}
		ArrayList<ClassOrInterface> temp = new ArrayList<ClassOrInterface>();
		if (willBeInterfaceList.size() > 0) {
			temp = regImpClasses(willBeInterfaceList);
		}
		for (ClassOrInterface i : temp) {
			boolean isExist = false;
			for (ClassOrInterface i2 : willBeInterfaceList) {
				if (i.getName().equals(i2.getName())) {
					isExist = true;
				}
			}
			if (!isExist) {
				willBeInterfaceList.add(i);
			}
		}
		return willBeInterfaceList;
	}

	private void addImpList(ClassOrInterface iF, ArrayList<ClassOrInterface> willBeImplimentslist) { // インターフェースを実装しているクラスリストに子クラスを登録する
		ArrayList<ClassOrInterface> removeSameClassList = new ArrayList<ClassOrInterface>();
		for (ClassOrInterface implementsClass : iF.getImplementsList()) {
			for (ClassOrInterface childClass : willBeImplimentslist) {
				if (implementsClass.equals(childClass)) {
					removeSameClassList.add(childClass);
				}
			}
		}
		for (ClassOrInterface childClass : removeSameClassList) {
			willBeImplimentslist.remove(childClass);
		}
		for (ClassOrInterface c : willBeImplimentslist) {
			iF.addImplementsList(c);
		}
		// for (ClassOrInterface c : iF.getImplementsList()) {
		// System.out.println(iF.getName() + c.getName());
		// }
		// 未実装：自分よりも上にあるクラス、インターフェースすべての実装しているクラスリストへ登録すること
	}

	private void addInterface(ClassOrInterface beInterface, ArrayList<ClassOrInterface> beIList) {
		boolean isExist = false;
		for (ClassOrInterface iF : beIList) {
			if (!iF.isClass()) {
				if (iF.getName().equals(beInterface.getName())) {
					isExist = true;
					break;
				}
			}
		}
		if (!isExist) {
			ClassOrInterface newIf = new ClassOrInterface(beInterface.getName(), beInterface.getFilePath(), false);
			for (ClassOrInterface c : classAndInterfaceList) {
				if (c.isClass()) {
					for (ClassOrInterface c2 : c.getExtendsList()) {
						if (beInterface.getName().equals(c2.getName())) {
							newIf.addImplementsList(c);
						}
					}
				}
			}
			beIList.add(newIf);
		}
	}

	private void regImpClassesHadChildIf() { // インターフェース1がインターフェース2を親クラスとして持つときに2の実装クラスリストに1の実装クラスリストを登録
		for (ClassOrInterface iF : classAndInterfaceList) {
			if (iF.getExtendsList().size() > 0) {
				// System.out.println("LM 564 " + iF.getName());
				for (ClassOrInterface iF2 : iF.getExtendsList()) {
					// System.out.println("LoadMethod 514 " + iF2.getName());
					for (ClassOrInterface iF3 : classAndInterfaceList) {
						if (iF2.getName().equals(iF3.getName())) {
							// System.out.println("LM 597 " + iF3.getName());
							for (ClassOrInterface cls : iF.getImplementsList()) {
								iF3.addImplementsList(cls);
							}
						}
					}
				}
			}

		}
	}

	private int skipBrank(String s, int cutStr) {
		while (s.equals(" ") || s.equals("\t")) {
			cutStr += 1;
			s = str.substring(cutStr, cutStr + 1);
		}
		return cutStr;
	}

	public ArrayList<String> showMethod(Object[] data, int count, MyPanel p) { // 選択されたクラスが持っているメソッド呼び出しのあるメソッドを表示
		StringBuilder buf2 = new StringBuilder();
		ArrayList<String> methodList = new ArrayList<String>();

		p.initwidth();
		for (int i = 1; i < count; i++) {
			buf2.append(data[i]);
			buf2.append(".");
		}
		buf2.deleteCharAt(buf2.lastIndexOf("."));
		methodList.add(buf2.toString());

		for (ArrayList<String> method : callList) {
			if (method.get(0).startsWith(buf2.toString() + ".")) {
				int i = buf2.length();
				int dotCount = 0;
				int j;

				// フォルダとファイルの区別をする
				while ((j = method.get(0).substring(i).indexOf(".")) != -1) {
					dotCount++;
					i += j + 1;
				}
				if (dotCount == 1) {
					// System.out.println("LoadMethod 483 " + method.get(0));
					p.addWidth(method.get(0));
					methodList.add(method.get(0));
				} else {
					methodList = null;
				}
			}
		}
		System.out.println("");
		return methodList;
	}

	public ArrayList<String> showNextMethod(String methodPath) { // 呼び出し先のメソッドの表示
		ArrayList<String> methodList = new ArrayList<String>();
		for (ArrayList<String> method : callList) {
			if (method.get(0).equals(methodPath)) {
				System.out.println("");
				for (int i = 1; i < method.size(); i++) {
					methodList.add(method.get(i));
					System.out.println(method.get(i));
				}
				System.out.println("");
				return methodList;
			}
		}
		return null;
	}
}

class MyFilter implements FilenameFilter { // dotファイル読み込み用フィルタ
	public boolean accept(File dir, String name) {
		int index = name.lastIndexOf(".");

		String ext = name.substring(index + 1).toLowerCase();
		if (ext.equals("dot") == true) {
			return true;
		}
		return false;
	}
}

class MyFilter2 implements FilenameFilter { // 修飾子判定のためのファイル読み込み用フィルタ
	public boolean accept(File dir, String name) {
		int index = name.lastIndexOf(".");
		String ext = name.substring(index + 1).toLowerCase();

		if (name.equals("classes.html")) {
			return false;
		}
		if (ext.equals("html") == true) {
			if (name.startsWith("class") == true || name.startsWith("interface") == true) {
				String last = name.substring(index - 7, index);
				if (last.equals("members") == false) {
					return true;
				}
			}
		}
		return false;
	}
}

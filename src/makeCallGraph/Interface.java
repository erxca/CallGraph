package makeCallGraph;

import java.util.ArrayList;

public class Interface {
	private String interfaceName;
	private String interfacePath;
	private String path;
	private ArrayList<Class> implementClassList = new ArrayList<Class>(); // このインターフェースを実装しているクラスリスト
	private ArrayList<String> extendsInterfaceList = new ArrayList<String>(); // このインターフェースが継承しているインターフェース

	public Interface(String interfaceName, String interfacePath, Class cls) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.interfaceName = interfaceName;
		System.out.println("interfacePath " + interfacePath);
		this.interfacePath = interfacePath;
		if (cls != null) {
			addImplList(cls);
		}
	}

	public ArrayList<String> getExtendsInterfaceList() {
		return extendsInterfaceList;
	}

	public void addImplList(Class cls) {
		implementClassList.add(cls);
	}

	public void addExtList(String iF) {
		extendsInterfaceList.add(iF);
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getInterfacePath() {
		return interfacePath;
	}

	public void setInterfacePath(String interfacePath) {
		this.interfacePath = interfacePath;
	}

	public ArrayList<Class> getImplementClassList() {
		return implementClassList;
	}

	public void setPath(String filePath) {
		if (filePath != null) {
			// System.out.println("Interface 46 " + filePath);
			StringBuilder sb = new StringBuilder();
			sb.append(filePath);
			sb.delete(0, 10);
			sb.delete(sb.lastIndexOf("."), sb.lastIndexOf(".") + 5);
			int j = sb.indexOf("\\");
			while (j != -1) {
				sb.delete(j, j + 1);
				sb.insert(j, ".");
				j = sb.indexOf("\\");
			}
			this.path = sb.toString();
			// System.out.println(path);
		}
	}

	public String getPath() {
		return path;
	}
}

package makeCallGraph;

import java.util.ArrayList;

public class ClassOrInterface {
	private String name;
	private String path; // **.**.**
	private String filePath; // .\src\src\**\**.java
	private boolean isClass;

	private ArrayList<ClassOrInterface> implementsList = new ArrayList<ClassOrInterface>(); // これを実装しているリスト
	private ArrayList<ClassOrInterface> extendsList = new ArrayList<ClassOrInterface>(); // これが継承しているリスト

	public ClassOrInterface(String name, String filePath, boolean isClass) {
		this.name = name;
		this.filePath = filePath;
		this.isClass = isClass;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		if (path != null) {
			// System.out.println("Interface 46 " + filePath);
			StringBuilder sb = new StringBuilder();
			sb.append(path);
			sb.delete(0, 22);
			sb.delete(sb.lastIndexOf("."), sb.lastIndexOf(".") + 5);
			int j = sb.indexOf("\\");
			while (j != -1) {
				sb.delete(j, j + 1);
				sb.insert(j, ".");
				j = sb.indexOf("\\");
			}
			this.path = sb.toString();
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getName() {
		return name;
	}

	public void addImplementsList(ClassOrInterface cOrI) {
		implementsList.add(cOrI);
	}

	public ArrayList<ClassOrInterface> getImplementsList() {
		return implementsList;
	}

	public void addExtendsList(ClassOrInterface cOrI) {
		extendsList.add(cOrI);
	}

	public ArrayList<ClassOrInterface> getExtendsList() {
		return extendsList;
	}

	public boolean isClass() {
		return isClass;
	}
}

package makeCallGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;

public class LoadClassName {
	private DefaultMutableTreeNode treedata = new DefaultMutableTreeNode();
	private DefaultMutableTreeNode parentNode = treedata;
	private String name;
	private boolean beforeIsFolder = true;
	private boolean isDir;

	private int before = 0;
	private String str;
	private String dir = "<span class=\"icon\">N</span>";
	private String clasS = "<span class=\"icon\">C</span>";
	private String pre = "self\">";
	private String post = "</a>";
	private String row = "row_";

	public DefaultMutableTreeNode load() { // クラス一覧を取得する
		String FILENAME = ".\\workspace\\data\\html\\annotated.html";
		try {
			File file = new File(FILENAME);
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((str = br.readLine()) != null) {
				if (str.indexOf(row) != -1) {
					makeTree();
				}
			}
			// System.out.println("end.");
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		return treedata;
	}

	private void makeTree() { // 今見ているクラスの階層を求める
		int now = 0;
		int startRow = str.indexOf(row);
		int s2 = startRow + 4;
		while (str.substring(s2, s2 + 2).matches("[0-9]_") || str.substring(s2, s2 + 3).matches("[0-9][0-9]_")) {
			if (str.substring(s2, s2 + 2).matches("[0-9]_")) {
				s2 += 2;
			} else if (str.substring(s2, s2 + 3).matches("[0-9][0-9]_")) {
				s2 += 3;
			}
			now++;
		}
		makeTree2(now);

		before = now;
	}

	private void makeTree2(int now) { // 適切な位置にクラスを挿入
		if ((str.indexOf(dir) != -1)) { // クラスがディレクトリか判断
			isDir = true;
		} else if ((str.indexOf(clasS) != -1)) {
			isDir = false;
		}

		int start = str.indexOf(pre);
		int end = str.indexOf(post);
		name = str.substring(start + 6, end);
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(name);
		if ((isDir == true && beforeIsFolder == false) || (isDir == false && beforeIsFolder == false && before > now)) {
			for (int i = 0; i < (before - now); i++) {
				parentNode = (DefaultMutableTreeNode) parentNode.getParent();
			}
		} else if (isDir == false && beforeIsFolder == false && before < now) {
			parentNode = (DefaultMutableTreeNode) parentNode.getLastChild();
		}
		parentNode.add(childNode);
		if (isDir == true) {
			parentNode = childNode;
			beforeIsFolder = true;
		} else {
			beforeIsFolder = false;
		}
	}
}

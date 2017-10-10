package makeCallGraph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class MyFrame extends JFrame implements TreeSelectionListener {
	private static final long serialVersionUID = 1L;
	private JTree tree;
	private ArrayList<String> methodList;
	private MyPanel p2, p3, p4;
	private JScrollPane scp2, scp3, scp4;
	private LoadClassName lcn = new LoadClassName();
	private LoadMethod lm;
	static final int PANELWIDTH = 790;
	private int PANELHEIGHT = 670;

	// public static void main(String[] args) {
	// EventQueue.invokeLater(new Runnable() {
	// public void run() {
	// try {
	// MyFrame frame = new MyFrame();
	// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// frame.setBounds(10, 10, 1350, 720);
	// frame.setTitle("CallGraph");
	// frame.setVisible(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// }

	public static void start() { // ツールを起動させる
		try {
			MyFrame frame = new MyFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setBounds(10, 10, 1350, 720);
			frame.setTitle("CallGraph");
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	MyFrame() {
		// メインパネル
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BorderLayout());
		setContentPane(mainPane);

		// 左：クラス一覧 右：グラフとソースコード用のスプリットペイン のスプリットペイン
		JSplitPane treeAndRightPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, true);
		treeAndRightPane.setDividerSize(5);
		mainPane.add(treeAndRightPane);

		// クラス一覧表示のためのツリーとスクロールペイン
		tree = new JTree(lcn.load());
		tree.addTreeSelectionListener(this);

		JScrollPane scp = new JScrollPane();
		scp.getViewport().setView(tree);
		scp.setPreferredSize(new Dimension(200, PANELHEIGHT));
		treeAndRightPane.setLeftComponent(scp);

		// 左：グラフ 右：ソースコード のスプリットペイン
		JSplitPane graphAndSrcPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, true);
		treeAndRightPane.setRightComponent(graphAndSrcPane);
		graphAndSrcPane.setDividerSize(5);

		// コールグラフ表示のためのパネルとスクロールペイン
		p2 = new MyPanel(this, lm);
		scp2 = new JScrollPane();
		scp2.getViewport().setView(p2);
		scp2.setPreferredSize(new Dimension(PANELWIDTH, PANELHEIGHT));
		graphAndSrcPane.setLeftComponent(scp2);

		// 上：ソースコード1 下：ソースコード2 のスプリットペイン
		JSplitPane twoSrcPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		graphAndSrcPane.setRightComponent(twoSrcPane);
		twoSrcPane.setDividerSize(5);

		// ソースコード表示のためのパネル1とスクロールペイン
		p3 = new MyPanel(this, lm);
		scp3 = new JScrollPane();
		scp3.getViewport().setView(p3);
		scp3.setPreferredSize(new Dimension(300, PANELHEIGHT / 2));
		scp3.getViewport().setBackground(Color.white);
		p3.setScp(scp3);
		twoSrcPane.setLeftComponent(scp3);

		// ソースコード表示のためのパネル2とスクロールペイン
		p4 = new MyPanel(this, lm);
		scp4 = new JScrollPane();
		scp4.getViewport().setView(p4);
		scp4.setPreferredSize(new Dimension(300, PANELHEIGHT / 2));
		scp4.getViewport().setBackground(Color.white);
		p4.setScp(scp4);
		twoSrcPane.setRightComponent(scp4);

		lm = new LoadMethod();
	}

	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		TreePath path = tree.getSelectionPath();

		if (node != null) {
			// コールグラフパネルの初期化
			getContentPane().remove(p2);
			p2 = new MyPanel(this, lm);
			p2.setPreferredSize(new Dimension(PANELWIDTH, PANELHEIGHT));
			scp2.getViewport().setView(p2);
			this.validate();

			// 選ばれたクラスのパスを取得
			int pathCount = path.getPathCount();
			ArrayList<Object> nodePath = new ArrayList<Object>();
			nodePath.add(path);
			Object[] data = path.getPath();
			p2.init();
			methodList = lm.showMethod(data, pathCount, p2);
			if (methodList != null) {
				p2.paintClass(methodList.get(0), p3, p4);
				p2.paintMbtn(methodList, p3, p4);
			}
		}
	}
}

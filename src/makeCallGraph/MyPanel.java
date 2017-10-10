package makeCallGraph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ArrayList<Method> methodNodeList = new ArrayList<Method>();
	private MyPanel classPanel;
	private LoadMethod lm;
	private int methodCount = 0;
	private int l, r, h;
	private int methodNum = 0;
	private int clsPnlWidth = 0; // クラスパネルの横幅
	private Color colorList[] = { Color.pink, Color.yellow, Color.cyan, Color.magenta, Color.orange, Color.green };
	private int colorNum = 0;
	private Frame f;
	private JScrollPane scp;

	public MyPanel(Frame f, LoadMethod lm) {
		setLayout(null);
		setBackground(Color.white);
		this.f = f;
		this.lm = lm;
	}

	public LoadMethod getLm() {
		return lm;
	}

	public Frame getF() {
		return f;
	}

	public JScrollPane getScp() {
		return scp;
	}

	public void setScp(JScrollPane scp) {
		this.scp = scp;
	}

	public void init() {
		this.methodNodeList.clear();
	}

	public void paintClass(String classPath, MyPanel p1, MyPanel p2) { // 選んだクラス名と枠を表示
		methodCount = 0;
		methodNum = 0;
		colorNum = 0;

		// クラス名のラベル
		JLabel lbl = new JLabel();
		FontMetrics fm = lbl.getFontMetrics(lbl.getFont());
		int classNameWidth = fm.stringWidth(classPath);

		// クラス名の幅とメソッドボタンを配置したときの幅を比べて大きいほうを採用
		clsPnlWidth = Math.max(classNameWidth + 50, clsPnlWidth);

		// クラスパネルの大きさがコールグラフパネルより大きいときは後者を再描画
		if (clsPnlWidth >= this.getSize().getWidth()) {
			this.setPreferredSize(new Dimension(clsPnlWidth + 200, (int) this.getSize().getHeight()));
			invalidate();
			getParent().validate();
		}
		int x = (this.getSize().width) / 2 - (clsPnlWidth + 20) / 2; // クラスパネルを描き始めるxを計算

		classPanel = new MyPanel(this.getF(), this.getLm());
		classPanel.setName("classPanel");
		classPanel.setOpaque(false);
		classPanel.setBorder(new LineBorder(Color.BLACK, 1));
		classPanel.setBounds(x, 30, clsPnlWidth + 25, 85);

		if (this.getSize().getWidth() <= clsPnlWidth + 20) {
			this.setPreferredSize(new Dimension(clsPnlWidth + 40, (int) this.getSize().getHeight()));
		}
		this.add(classPanel);

		lbl.setText(classPath);
		lbl.setBounds(10, 10, classNameWidth, fm.getHeight());
		classPanel.add(lbl);

		classPath = classPath.substring(classPath.indexOf(".") + 1);
		String cutClassName = classPath.substring(classPath.lastIndexOf(".") + 1);
		paintSourceButton(cutClassName, p1.getScp(), p2.getScp());
	}

	private void paintSourceButton(String className, JScrollPane scp1, JScrollPane scp2) {
		ImageIcon icon = new ImageIcon("./workspace/data/14460.png");

		JButton srcBtn = new JButton(icon);
		srcBtn.setBounds(classPanel.getWidth() - 50, 10, 30, 20);

		for (String s : LoadMethod.classPathList) {
			String tmps = s.substring(s.lastIndexOf("\\") + 1, s.lastIndexOf("."));
			if (tmps.equals(className)) {
				srcBtn.addMouseListener(new MySrcButtonListener(s, null, scp1, scp2));

			}
		}
		classPanel.add(srcBtn);
	}

	public void initwidth() { // クラスの枠の横幅の初期化
		this.clsPnlWidth = 0;
	}

	public void addWidth(String methodPath) { // クラスの枠の横幅を足していく
		String methodName = methodPath.substring(methodPath.lastIndexOf(".") + 1);
		FontMetrics fm = this.getFontMetrics(this.getFont());
		clsPnlWidth += fm.stringWidth(methodName) + 50;
	}

	public void paintMbtn(ArrayList<String> methodList, final MyPanel p1, final MyPanel p2) { // 選んだクラスのメソッドを表示
		FontMetrics fm = this.getFontMetrics(this.getFont());
		int x1 = 20;

		for (int i = 1; i < methodList.size(); i++) {
			String methodName = methodList.get(i).substring(methodList.get(i).lastIndexOf(".") + 1);
			String methodPath = methodList.get(i);
			final Method method1 = new Method(methodName, methodPath, methodNum, new JButton(), null, this, p1.getScp(),
					p2.getScp());
			final JButton btn = method1.getBtn();
			btn.setBounds(x1, 40, fm.stringWidth(methodName) + 40, 30);

			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					removeBtn();
					String cmd = method1.getMethodPath();
					paintNextMbtn(cmd, (int) (classPanel.getBounds().getX() + btn.getBounds().getCenterX()), method1,
							p1, p2);
				}
			});

			method1.setX1(btn.getBounds().getCenterX() + classPanel.getBounds().getMinX());
			method1.setY1(btn.getBounds().getCenterY() + classPanel.getBounds().getMinY());
			method1.setBtn(btn);
			classPanel.add(method1.getBtn());
			x1 += fm.stringWidth(methodName) + 50;
		}
	}

	private void removeBtn() { // 現在表示されている呼び出し先メソッドの全消去
		int comCount = MyPanel.this.getComponentCount();
		methodNodeList.clear();
		methodNum = 0;
		colorNum = 0;
		for (int i = comCount - 1; i >= 0; i--) {
			if (MyPanel.this.getComponent(i).getName().equals("Button")) {
				MyPanel.this.remove(MyPanel.this.getComponent(i));
			}
		}
	}

	public void paintNextMbtn(String cmd, int btnCenterX, Method pMethod, final MyPanel p1, final MyPanel p2) { // 呼び出し先のメソッドの表示
		r = l = btnCenterX;
		methodNum++;
		methodCount = 0;
		h = 70 * methodNum;
		ArrayList<String> cMethod = lm.showNextMethod(cmd);

		if (cMethod == null) { // 呼び出し先メソッドがない（葉ノード）
			pMethod.getBtn().setBorder(new LineBorder(Color.black, 2));
			System.out.println("this method don't call other method.");
			return;
		}

		for (String method : cMethod) { // 呼び出し先メソッドの数だけ繰り返す
			makeNode(method, p1, p2, pMethod);
		}

		validate();
		repaint();
	}

	// ノードの作成、ボタンの作成
	private void makeNode(String mthdPath, MyPanel p1, MyPanel p2, Method parentMthd) {
		FontMetrics fm = this.getFontMetrics(this.getFont());
		String methodName = mthdPath.substring(mthdPath.lastIndexOf(".") + 1);
		String methodPath = mthdPath;
		String classPath = mthdPath.substring(0, mthdPath.lastIndexOf("."));
		int stringWidth = fm.stringWidth(methodName);
		final Method method = new Method(methodName, methodPath, methodNum, new JButton(), parentMthd, this,
				p1.getScp(), p2.getScp());
		final JButton btn = method.getBtn();
		btn.setToolTipText(classPath);

		for (int i = 0; i < methodNodeList.size(); i++) {
			Method node = methodNodeList.get(i);
			if (node.getMethodPath().equals(methodPath)) {
				paintNodeColor(node, methodPath, btn, method);
				break;
			}
		}

		if (methodCount % 2 == 0 && (l -= stringWidth + 40 + 5) > 0) {

			if (60 + h + 30 > (int) this.getSize().getHeight()) {
				this.setPreferredSize(
						new Dimension((int) this.getSize().width, (int) this.getSize().getHeight() + 120));
				invalidate();
				getParent().validate();
			}
			btn.setBounds(l, 60 + h, stringWidth + 40, 30);
			l -= 5;

		} else {

			r += 5;
			if ((r + stringWidth + 40) > this.getSize().getWidth()) {
				if (60 + h + 30 > (int) this.getSize().getHeight()) {
					this.setPreferredSize(new Dimension((int) this.getSize().getWidth() + 20 + stringWidth + 40,
							(int) this.getSize().getHeight() + 120));
				} else {
					this.setPreferredSize(new Dimension((int) this.getSize().getWidth() + 20 + stringWidth + 40,
							(int) this.getSize().getHeight()));
				}
				invalidate();
				getParent().validate();
			}
			btn.setBounds(r, 60 + h, stringWidth + 40, 30);
			r += 5 + stringWidth + 40;

		}
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btn2Action(method);
				String cmd = method.getMethodPath();
				paintNextMbtn(cmd, (int) btn.getBounds().getCenterX(), method, p1, p2);
			}
		});

		method.setX1(btn.getBounds().getCenterX());
		method.setY1(btn.getBounds().getCenterY());
		method.setBtn(btn);
		methodNodeList.add(method);
		this.add(method.getBtn());
		methodCount++;
	}

	private void paintNodeColor(Method node, String methodPath, JButton btn, Method mthd) {
		if (node.getColorNum() >= 0) { // すでに同じメソッドがあるとき

			btn.setBackground(colorList[node.getColorNum()]);
			mthd.setColorNum(node.getColorNum());

		} else {

			node.getBtn().setBackground(colorList[colorNum]);
			btn.setBackground(colorList[colorNum]);
			node.setColorNum(colorNum);
			mthd.setColorNum(colorNum);
			colorNum++;

		}
	}

	public void btn2Action(Method method2) { // 呼び出し先のメソッドのボタンを押したときの挙動
		methodNum = method2.getMethodNum();
		for (int i = methodNodeList.size() - 1; i >= 0; i--) {
			Method methodNode = methodNodeList.get(i);

			if (methodNode.getMethodNum() > method2.getMethodNum()) {
				this.remove(methodNode.getBtn());
				methodNodeList.remove(i);
				ArrayList<Method> sameMethod = new ArrayList<Method>();

				for (Method mNode : methodNodeList) {
					if (methodNode.getMethodPath().equals(mNode.getMethodPath())) {
						sameMethod.add(mNode);
					}
				}

				if (sameMethod.size() == 1) {
					sameMethod.get(0).getBtn().setBackground(Color.white);
					sameMethod.get(0).setColorNum(-1);
					colorNum--;
				}
			}
		}
		repaint();
	}

	public void paintImplementsClass(int x, int y) {
		System.out.println(x + " pIC " + y);
		classPanel = new MyPanel(this.getF(), this.getLm());
		classPanel.setName("classPanel");
		classPanel.setOpaque(false);
		classPanel.setBorder(new LineBorder(Color.BLACK, 1));
		classPanel.setBounds(x, y + 70, 50, 85);
		// if (this.getSize().getWidth() <= width + 20) {
		// this.setPreferredSize(new Dimension(width + 40, (int)
		// this.getSize().getHeight()));
		// }
		this.add(classPanel);
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) { // ノード間の線を引く
		if (methodNodeList == null) {
			return;
		}

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		for (Method node : methodNodeList) {
			double x1 = node.getParentMethod().getX1();
			double y1 = node.getParentMethod().getY1();
			double x2 = node.getX1();
			double y2 = node.getY1();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.draw(new Line2D.Double(x1, y1 + 15, x2, y2 - 15));
		}
	}
}

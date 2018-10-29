package view;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;

import javax.swing.JLabel;

import controller.ClassPanelButtonListener;
import model.Class;
import model.Method;

public class TraceClassPanel extends MyClassPanel {
	// Frame f;
	// MyGraphPanel p;
	// Class c;
	// JLabel lbl;
	// int width, mWidth, cWidth, cHeight;
	// int panelX;
	// ArrayList<MyMethodButton> btnList = new ArrayList<MyMethodButton>();
	// FontMetrics btnFm;

	public TraceClassPanel(Frame f, MyGraphPanel p, Class c) {
		// this.f = f;
		// this.p = p;
		// this.c = c;
		//
		// initVariable();
		// initPanel();
		// makePanel();
		super(f, p, c);

	}

	// private void initVariable() {
	// width = 0;
	// mWidth = 0;
	// cWidth = 0;
	// }
	//
	// private void initPanel() {
	// setName("classPanel");
	// setOpaque(false);
	// setBorder(new LineBorder(Color.BLACK, 1));
	// setBackground(Color.white);
	// setLayout(null);
	// }
	//
	public void makePanel() {

		calcWidth();

		setBounds(panelX, 30, width, 85);

		lbl.setText(c.getClassName());
		lbl.setBounds(10, 10, cWidth, cHeight);
		add(lbl);

		paintPmethodButton();
	}

	// 呼び出し元クラスのパネルの大きさ関係
	private void calcWidth() {

		// クラス名のラベル
		lbl = new JLabel();
		FontMetrics fm = lbl.getFontMetrics(lbl.getFont());
		cWidth = fm.stringWidth(c.getClassName());
		cHeight = fm.getHeight();

		// メソッドボタン
		for (Method method : c.getMethodList()) {
			addMethodButtonWidth(method);
		}

		width = Math.max(mWidth + 30, cWidth + 20);

		// frameよりもパネルの方の幅が大きい場合はframeの幅を大きくする
		int diff = p.getWidth() - width;
		if (diff <= 0) {
			p.setPreferredSize(new Dimension(width + 200, p.getHeight()));
			diff = 100;
			invalidate();
			f.validate();
		}

		panelX = diff / 2;

	}

	private void addMethodButtonWidth(Method method) {

		// MyMethodButton btn = new MyMethodButton(p, method, false, 0);
		MyMethodButton btn;

		if (method.isConstructor()) {
			btn = new RoundedCornerButton(p, method, true, 0);
		} else {
			btn = new TraceButton(p, method, true, 0);
		}
		btn.addMouseListener(new ClassPanelButtonListener(method));
		btnList.add(btn);
		mWidth += btn.fm.stringWidth(method.getMethodName()) + 50;

		((TraceGraphPanel) p).setBorder(btn.getBorder());
	}

	private void paintPmethodButton() {

		int btnX = 20;

		for (MyMethodButton btn : btnList) {
			btn.setting(btnX);
			btnX += btn.getWidth() + 10;
			add(btn);
		}
	}
}

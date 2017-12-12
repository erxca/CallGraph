package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import model.Class;
import model.Method;

public class MyPanel extends JPanel {
	JPanel p;
	Frame f;
	JScrollPane scp;
	Class c;
	JLabel lbl;
	JPanel classPanel = new JPanel();
	int width, mWidth, cWidth, cHeight;
	int panelX;
	ArrayList<JButton> btnList = new ArrayList<JButton>();

	public MyPanel(Frame f, JPanel p, JScrollPane scp, Class c) {
		this.f = f;
		this.p = p;
		this.scp = scp;
		this.c = c;

		initVariable();
	}

	private void initVariable() {
		width = 50;
		mWidth = 0;
		cWidth = 0;
	}

	private void init() {
		f.remove(p);
		int width = p.getWidth();
		int height = p.getHeight();
		p = new JPanel();
		p.setOpaque(false);
		p.setBackground(Color.black);
		p.setBounds(0, 0, width, height);
		scp.getViewport().setView(p);
		f.repaint();
	}

	public void makeClassCps() {

		init();
		calcClassPanelWidth();

		// classPanel.setBounds(diff / 2, 50, width, 200);
		classPanel.setName("classPanel");
		classPanel.setOpaque(false);
		classPanel.setBorder(new LineBorder(Color.BLACK, 1));
		classPanel.setBounds(panelX, 30, width + 25, 85);
		classPanel.setVisible(true);
		p.add(classPanel);

		lbl.setText(c.getClassName());
		lbl.setBounds(10, 10, cWidth, cHeight);
		classPanel.add(lbl);

		paintPmethodButton();
		// p.setVisible(true);
		// p.invalidate();
		// p.validate();
		// p.repaint();
	}

	// 呼び出し元クラスのパネルの大きさ関係
	private void calcClassPanelWidth() {

		// クラス名のラベル
		lbl = new JLabel();
		FontMetrics fm = lbl.getFontMetrics(lbl.getFont());
		cWidth = fm.stringWidth(c.getClassName());
		cHeight = fm.getHeight();

		// メソッドボタン
		for (Method method : c.getMethodList()) {
			addMethodButtonWidth(method.getMethodName());
		}

		width += Math.max(mWidth, cWidth + 50);

		// frameよりもパネルの方の幅が大きい場合はframeの幅を大きくする
		int diff = p.getWidth() - width;
		if (diff <= 0) {
			p.setPreferredSize(new Dimension(width + 200, p.getHeight()));
			diff = 100;
			// p.invalidate();
			// p.validate();
		}

		panelX = diff / 2;

	}

	private void addMethodButtonWidth(String methodName) {

		JButton btn = new JButton(methodName);
		btn.setBackground(Color.white);
		// ここでactionセットする
		btnList.add(btn);
		FontMetrics fm = btn.getFontMetrics(btn.getFont());
		mWidth += fm.stringWidth(methodName) + 50;

	}

	private void paintPmethodButton() {

		int btnX = (width - (mWidth - 50)) / 2;
		JButton btn = new JButton();
		FontMetrics fm = btn.getFontMetrics(btn.getFont());

		for (int i = 0; i < btnList.size(); i++) {
			btn = btnList.get(i);
			btn.setBounds(btnX + i * 50, 40, fm.stringWidth(btn.getText()) + 40, 30);
			classPanel.add(btn);
		}
	}
}

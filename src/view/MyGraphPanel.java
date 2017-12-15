package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import model.Method;

public class MyGraphPanel extends JPanel {

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int viewW = screenSize.width;
	int viewH = screenSize.height;
	ArrayList<MyMethodButton> nodeList = new ArrayList<MyMethodButton>();
	IWorkbenchPage page;
	Frame f;

	public MyGraphPanel(IWorkbenchPage page, Frame f) {

		setOpaque(true);
		setBackground(Color.white);
		setLayout(null);
		setBounds(0, 0, viewW / 2, viewH / 2);

		this.page = page;
		this.f = f;

	}

	public void removeBtn() {
		for (Component c : getComponents()) {
			if (c.getName().equals("CalledMehod")) {
				remove(c);
			}
		}
	}

	public void makeCalledMethod(ArrayList<Method> mList, int centerX) {
		int mWidth = 20;
		for (Method m : mList) {

			System.out.println(m.getMethodName());
			MyMethodButton btn = new MyMethodButton(this, m, true);
			nodeList.add(btn);
			mWidth += btn.fm.stringWidth(m.getMethodName()) + 50;
		}

		int btnX;
		if ((btnX = centerX - (mWidth + 10) / 2 + 20) > 0) {
			for (MyMethodButton btn : nodeList) {
				btn.setting(btnX, 30 + 85 + 45);
				btnX += btn.getWidth() + 10;
				add(btn);
			}
		} else {
			// パネルサイズでかく
		}
		// invalidate();
		// f.validate();
		repaint();
	}

	public void changeViewFocus() {
		try {
			page.showView("org.eclipse.jdt.ui.PackageExplorer");
			page.showView("tool.test.views.TestView");
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void paintComponent(Graphics g) { // ノード間の線を引く
		// if (methodNodeList == null) {
		// return;
		// }

		super.paintComponent(g);
		// Graphics2D g2 = (Graphics2D) g;

		// for (Method node : methodNodeList) {
		// double x1 = node.getParentMethod().getX1();
		// double y1 = node.getParentMethod().getY1();
		// double x2 = node.getX1();
		// double y2 = node.getY1();
		// g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		// g2.draw(new Line2D.Double(x1, y1 + 15, x2, y2 - 15));
		// }
	}
}

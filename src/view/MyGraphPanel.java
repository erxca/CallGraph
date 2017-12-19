package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;

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
	int level;
	final int HALF_BTN_Y = 15;
	final int BRANK = 60;
	final int PARENT_CENTER_Y = 100;

	public MyGraphPanel(IWorkbenchPage page, Frame f) {

		setOpaque(true);
		setBackground(Color.white);
		setLayout(null);
		setBounds(0, 0, viewW / 2, viewH / 2);

		this.page = page;
		this.f = f;
		this.level = 0;

	}
	//
	// public void removeBtn() {
	// for (Component c : getComponents()) {
	// if (c.getName().equals("CalledMethod")) {
	// remove(c);
	// }
	// }
	//
	// nodeList.clear();
	// level = 0;
	// }

	public void removeBtn(int level) {

		Iterator<MyMethodButton> itr = nodeList.iterator();
		while (itr.hasNext()) {
			MyMethodButton btn = itr.next();
			System.out.println("btn  " + btn.getText());
			if (btn.getLevel() > level) {
				remove(btn);
				System.out.println("remove  " + btn.getText());
				itr.remove();
			}
		}
		this.level = level;
	}

	public void makeCalledMethod(ArrayList<Method> mList, int centerX, double pY) {
		level++;
		int mWidth = 20;
		for (Method m : mList) {

			System.out.println(m.getMethodName());
			MyMethodButton btn = new MyMethodButton(this, m, true, level);
			nodeList.add(btn);
			mWidth += btn.fm.stringWidth(m.getMethodName()) + 50;
		}

		checkLeftEnd(centerX, pY, mWidth);
		checkRightEnd(centerX, pY, mWidth);

		invalidate();
		f.validate();
		repaint();
	}

	private void checkLeftEnd(int centerX, double pY, int mWidth) { // 左端がはみ出ていないかチェックしてノードを配置
		int btnX = (btnX = centerX - (mWidth + 10) / 2 + 20) > 0 ? btnX : 20;

		for (MyMethodButton btn : nodeList) {
			if (btn.getLevel() == level) {
				btn.setting(btnX, (PARENT_CENTER_Y - HALF_BTN_Y) + (BRANK + HALF_BTN_Y) * level);
				btn.setPXY(centerX, pY);
				btnX += btn.getWidth() + 10;
				add(btn);
			}
		}
	}

	private void checkRightEnd(int centerX, double pY, int mWidth) { // 右端がはみ出ていないかチェック
		int methodRightEnd = centerX + mWidth / 2 + 20;
		if (methodRightEnd > getWidth()) {
			setPreferredSize(new Dimension(methodRightEnd, getHeight()));
		}
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
		if (nodeList == null) {
			return;
		}

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		for (MyMethodButton btn : nodeList) {
			double x1 = btn.getMyX();
			double y1 = btn.getMyY();
			double x2 = btn.getpX();
			double y2 = btn.getpY();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.draw(new Line2D.Double(x1, y1, x2, y2));
		}
	}
}

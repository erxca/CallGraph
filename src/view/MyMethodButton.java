package view;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

import model.Method;

public class MyMethodButton extends JButton {
	FontMetrics fm;
	MyGraphPanel p;
	Method m;
	int nameWidth;
	int level;
	double pX, pY, myX, myY;
	int colorNum = -1;
	ArrayList<Integer> calledLineList = new ArrayList<Integer>();

	final int P_METHOD_Y = 40;
	final int HEIGHT = 30;
	final int CLASS_PANEL_Y = 30;

	boolean isLeaf = false;

	public MyMethodButton(MyGraphPanel p, Method m, boolean isCalled, int level) {
		String name = m.getMethodName();
		this.p = p;
		this.m = m;
		this.level = level;
		setText(name);
		setBackground(Color.white);
		addAction();
		// addMouseListener(new MyButtonListener(m));
		// setCallerPath();

		fm = this.getFontMetrics(this.getFont());
		nameWidth = fm.stringWidth(name);

		if (isCalled) {
			setToolTipText(m.getDeclaringClassName());
			setName("CalledMethod");
		}
	}

	public void setting(int x) {
		setBounds(x, P_METHOD_Y, nameWidth + 40, 30);
		setMyXY(x, P_METHOD_Y + getBounds().getHeight() / 2);
	}

	public void setting(int x, int y) {
		setBounds(x, y, nameWidth + 40, HEIGHT);
		setMyXY(x + getBounds().getWidth() / 2, y);
	}

	private void addAction() {
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				p.removeBtn(level);
				if (m.getMethodCallSet().size() == 0) {
					setBorder(new LineBorder(Color.black, 2));
					isLeaf = true;
				}
				if (level == 0) {
					p.initList();
					p.makeCalledMethod(m, (int) (getBounds().getCenterX() + getParent().getBounds().getX()),
							getBounds().getMaxY() + getParent().getBounds().getY());
				} else {
					p.makeCalledMethod(m, (int) getBounds().getCenterX(), getBounds().getMaxY());
				}
			}

		});
	}

	// private void setCallerPath() {
	// for (Method method : m.getMethodCallList()) {
	// method.setCallerClassPath(m.getPath());
	// // System.out.println(method.getMethodName() + " " +
	// // method.getCallerClassPath());
	// }
	// }

	public boolean equals(MyMethodButton btn) {
		if (this.m.equals(btn.getM())) {
			return true;
		} else {
			return false;
		}
	}

	public Method getM() {
		return m;
	}

	public int getLevel() {
		return level;
	}

	public void setMyXY(double myX, double myY) {
		this.myX = myX;
		this.myY = myY;
	}

	public void setPXY(double pX, double pY) {
		this.pX = pX;
		this.pY = pY;
	}

	public double getpX() {
		return pX;
	}

	public double getpY() {
		return pY;
	}

	public double getMyX() {
		return myX;
	}

	public double getMyY() {
		return myY;
	}

	public int getColorNum() {
		return colorNum;
	}

	public void setColorNum(int colorNum) {
		this.colorNum = colorNum;
	}

	public void setBgColor(int colorNum) {
		setBackground(p.colorList[colorNum]);
		this.colorNum = colorNum;
	}

	public ArrayList<Integer> getCalledLineList() {
		return calledLineList;
	}

	public void setCalledLineList(ArrayList<Integer> calledLineList) {
		this.calledLineList = calledLineList;
	}

}

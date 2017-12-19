package view;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.Method;

public class MyMethodButton extends JButton {
	FontMetrics fm;
	MyGraphPanel p;
	Method m;
	int nameWidth;
	int level;
	double pX, pY, myX, myY;

	final int P_METHOD_Y = 40;
	final int HEIGHT = 30;
	final int CLASS_PANEL_Y = 30;

	public MyMethodButton(MyGraphPanel p, Method m, boolean isCalled, int level) {
		String name = m.getMethodName();
		this.p = p;
		this.m = m;
		this.level = level;
		setText(name);
		setBackground(Color.white);
		addAction();

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
				if (level == 0) {
					p.makeCalledMethod(m.getMethodCallList(),
							(int) (getBounds().getCenterX() + getParent().getBounds().getX()),
							getBounds().getMaxY() + getParent().getBounds().getY());
				} else {
					p.makeCalledMethod(m.getMethodCallList(), (int) getBounds().getCenterX(), getBounds().getMaxY());
				}
			}

		});
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

}

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

	public MyMethodButton(MyGraphPanel p, Method m, boolean isCalled) {
		String name = m.getMethodName();
		this.p = p;
		this.m = m;
		setText(name);
		setBackground(Color.white);
		addAction();

		fm = this.getFontMetrics(this.getFont());
		nameWidth = fm.stringWidth(name);

		if (isCalled) {
			setName("CalledMethod");
		}
	}

	public void setting(int x) {
		setBounds(x, 40, nameWidth + 40, 30);
	}

	public void setting(int x, int y) {
		setBounds(x, y, nameWidth + 40, 30);
	}

	public void addAction() {
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(m.getMethodName());
				p.removeBtn();
				p.makeCalledMethod(m.getMethodCallList(),
						(int) (getBounds().getCenterX() + getParent().getBounds().getX()));
				// String cmd = method1.getMethodPath();
				// paintNextMbtn(cmd, (int) (classPanel.getBounds().getX() +
				// btn.getBounds().getCenterX()), method1,
				// p1, p2);
			}
		});
	}

}

package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class MyButtonListener extends MouseAdapter {
	private JPopupMenu popup;

	public MyButtonListener(String classPath) {

		popup = new JPopupMenu();
		JMenuItem view1 = new JMenuItem("エディタに表示");
		view1.addMouseListener(new MyButtonAdapter(classPath));
		popup.add(view1);

	}

	public void mouseReleased(MouseEvent e) {
		showPopup(e);
	}

	public void mousePressed(MouseEvent e) {
		showPopup(e);
	}

	private void showPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}
}

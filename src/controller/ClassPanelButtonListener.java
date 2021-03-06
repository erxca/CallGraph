package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import model.Method;

public class ClassPanelButtonListener extends MouseAdapter {
	public JPopupMenu popup;

	public ClassPanelButtonListener(Method m) {

		popup = new JPopupMenu();
		JMenuItem view1 = new JMenuItem("エディタに表示");
		view1.addMouseListener(new MyButtonAdapter(m));
		popup.add(view1);

		JMenuItem view2 = new JMenuItem("ブレークポイントに設定");
		view2.addMouseListener(new BpSettingAdapter(m));
		popup.add(view2);

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

package makeCallGraph;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;

public class MyButtonListener extends MouseAdapter {
	StyleContext sc = new StyleContext();
	DefaultStyledDocument doc = new DefaultStyledDocument(sc);
	private JPopupMenu popup;

	public MyButtonListener(String methodPath, String methodName,
			JScrollPane scp1, JScrollPane scp2, MyPanel p, Method m) {
		popup = new JPopupMenu();
		JMenuItem view1 = new JMenuItem("ビュー1に表示");
		view1.addMouseListener(new MyMenuItemListener(methodPath, scp1));
		JMenuItem view2 = new JMenuItem("ビュー2に表示");
		view2.addMouseListener(new MyMenuItemListener(methodPath, scp2));
		popup.add(view1);
		popup.add(view2);

		for (ClassOrInterface iF : LoadMethod.classAndInterfaceList) {
			if (!iF.isClass()) {
				if (iF.getPath() != null && methodPath.startsWith(iF.getPath())) {
					JMenu menu = new JMenu("実装クラスの表示");
					System.out.println(methodPath + " "
							+ iF.getImplementsList().size());
					for (ClassOrInterface clasS : iF.getImplementsList()) {
						StringBuffer buf = new StringBuffer(clasS.getPath());
						buf.append(".");
						buf.append(methodName);
						JMenu menu2 = new JMenu(clasS.getName());
						JMenuItem view_1 = new JMenuItem("ビュー1に表示");
						view_1.addMouseListener(new MyMenuItemListener(buf
								.toString(), scp1));
						JMenuItem view_2 = new JMenuItem("ビュー2に表示");
						view_2.addMouseListener(new MyMenuItemListener(buf
								.toString(), scp2));
						menu2.add(view_1);
						menu2.add(view_2);
						menu.add(menu2);
					}
					popup.add(menu);
					break;
				}
			}
		}
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

package makeCallGraph;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

public class MySrcButtonListener extends MouseAdapter {
	private JPopupMenu popup;

	public MySrcButtonListener(String methodPath, String methodName,
			JScrollPane scp1, JScrollPane scp2) {
		popup = new JPopupMenu();
		JMenuItem view1 = new JMenuItem("ビュー1に表示");
		view1.addMouseListener(new MyMenuItemListener(methodPath, scp1));
		JMenuItem view2 = new JMenuItem("ビュー2に表示");
		view2.addMouseListener(new MyMenuItemListener(methodPath, scp2));
		popup.add(view1);
		popup.add(view2);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int modi = e.getModifiers();
		if ((modi & MouseEvent.BUTTON1_MASK) != 0) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}
}

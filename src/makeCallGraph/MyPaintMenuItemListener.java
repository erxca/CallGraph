package makeCallGraph;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyPaintMenuItemListener extends MouseAdapter {
	private MyPanel p;
	private double x, y;
	private Method m;

	public MyPaintMenuItemListener(MyPanel p, Method m) {
		this.p = p;
		this.m = m;
		// this.x = m.getX1();
		// this.y = m.getY1();
		// System.out.println(x + y);
	}

	public void mouseReleased(MouseEvent e) {
		action();
	}

	private void action() {
		p.paintImplementsClass((int) m.getX1(), (int) m.getY1() + 15);

	}
}

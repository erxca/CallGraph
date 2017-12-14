package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class MyGraphPanel extends JPanel {

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int viewW = screenSize.width;
	int viewH = screenSize.height;

	public MyGraphPanel() {

		setOpaque(true);
		setBackground(Color.white);
		setLayout(null);
		setBounds(0, 0, viewW / 2, viewH / 2);

	}
}

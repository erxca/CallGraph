package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class PluginView extends ViewPart {
	ScrolledComposite scrollComposite;
	Composite composite;
	static Frame f;
	static JPanel p;
	static JScrollPane scp;

	// public ToolView() {
	// // TODO 自動生成されたコンストラクター・スタブ
	// }

	@Override
	public void createPartControl(Composite parent) {

		// // スクロール可能なコンポジットを作成
		// scrollComposite = new ScrolledComposite(parent, SWT.V_SCROLL |
		// SWT.H_SCROLL);
		// // スクロール可能なコンポジットを親とするコンポジットを作成する
		// composite = new Composite(scrollComposite, SWT.BORDER);
		// composite.setLayout(null);
		// composite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		//
		// // スクロールバー設定
		// scrollComposite.setExpandHorizontal(true);
		// scrollComposite.setExpandVertical(true);
		// scrollComposite.setContent(composite);
		// scrollComposite.setMinSize(composite.computeSize(SWT.DEFAULT,
		// SWT.DEFAULT));

		f = SWT_AWT.new_Frame(new Composite(parent, SWT.EMBEDDED));

		// f.setBackground(Color.black);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int viewW = screenSize.width;
		int viewH = screenSize.height;

		p = new JPanel();
		// p.setLayout(null);
		p.setOpaque(false);
		p.setBackground(Color.white);
		p.setBounds(0, 0, viewW / 2, viewH / 2);

		scp = new JScrollPane();
		scp.getViewport().setView(p);
		scp.setBounds(0, 0, viewW / 2, viewH / 2);
		scp.getViewport().setBackground(Color.white);

		f.add(scp);

		// f.add(p);
		// FontMetrics fm = f.getFontMetrics(f.getFont());

		// f.setVisible(true);
		// f.repaint();
		System.out.println(p.getSize().getWidth() + " " + p.getSize().getHeight());
		// makeClassCps();

	}

	public static JPanel getP() {
		return p;
	}

	public static Frame getF() {
		return f;
	}

	public static JScrollPane getScp() {
		return scp;
	}

	@Override
	public void setFocus() {
		// TODO 自動生成されたメソッド・スタブ

	}
}

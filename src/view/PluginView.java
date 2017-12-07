package view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

public class PluginView extends ViewPart {
	ScrolledComposite scrollComposite;
	Composite composite;

	// public ToolView() {
	// // TODO 自動生成されたコンストラクター・スタブ
	// }

	@Override
	public void createPartControl(Composite parent) {

		// スクロール可能なコンポジットを作成
		scrollComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		// スクロール可能なコンポジットを親とするコンポジットを作成する
		composite = new Composite(scrollComposite, SWT.BORDER);
		composite.setLayout(null);
		composite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		// スクロールバー設定
		scrollComposite.setExpandHorizontal(true);
		scrollComposite.setExpandVertical(true);
		scrollComposite.setContent(composite);
		scrollComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		makeClassCps();
	}

	public void makeClassCps() {

		Frame f = SWT_AWT.new_Frame(new Composite(composite, SWT.EMBEDDED));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int viewW = screenSize.width;
		int viewH = screenSize.height;
		f.setBounds(0, 0, viewW, viewH);

		System.out.println(f.getSize().getWidth() + " " + f.getSize().getHeight());

		JPanel classP = new JPanel();
		// Composite pClass = new Composite(composite, SWT.BORDER);
		// pClass.setLayout(null);
		// pClass.setBounds(20, 20, 50, 50);
		// pClass.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		// int cpsX = pClass.getSize().x;
		// int cpsY = pClass.getSize().y;
		// System.out.println("x :" + cpsX + " y : " + cpsY);

		// Label textLabel = new Label(composite, SWT.BORDER | SWT.CENTER);
		// textLabel.setText("TextLabel");
		// textLabel.setBounds(20, 20, 50, 50);
	}

	@Override
	public void setFocus() {
		// TODO 自動生成されたメソッド・スタブ

	}
}

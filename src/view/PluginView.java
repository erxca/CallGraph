package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

public class PluginView extends ViewPart {

	// public ToolView() {
	// // TODO 自動生成されたコンストラクター・スタブ
	// }

	@Override
	public void createPartControl(Composite parent) {
		// frame = SWT_AWT.new_Frame(new Composite(parent, SWT.EMBEDDED));
		// frame.setLayout(new FlowLayout());
		//
		// // メインパネル
		// JPanel mainPane = new JPanel();
		// mainPane.setLayout(new BorderLayout());
		// frame.add(mainPane);

		// スクロール可能なコンポジットを作成
		ScrolledComposite scrollComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		// スクロール可能なコンポジットを親とするコンポジットを作成する
		Composite composite = new Composite(scrollComposite, SWT.NONE);
		composite.setLayout(null);
		composite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		// スクロールバー設定
		scrollComposite.setExpandHorizontal(true);
		scrollComposite.setExpandVertical(true);
		scrollComposite.setContent(composite);
		scrollComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	@Override
	public void setFocus() {
		// TODO 自動生成されたメソッド・スタブ

	}
}

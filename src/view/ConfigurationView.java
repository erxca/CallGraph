package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class ConfigurationView extends ViewPart {
	ScrolledComposite scrollComposite;
	Composite composite;
	public static Frame f;
	public static JPanel p;
	static JScrollPane scp;
	public static JTextField paraTf, methodTf, classTf, mParaTf;
	public static ArrayList<String> paraList = null;
	int y = 10;

	public void createPartControl(Composite parent) {

		f = SWT_AWT.new_Frame(new Composite(parent, SWT.EMBEDDED));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int viewW = screenSize.width;
		int viewH = screenSize.height;

		p = new JPanel();
		p.setOpaque(true);
		p.setBackground(Color.white);
		p.setBounds(0, 0, viewW / 2, viewH / 2);
		p.setLayout(null);

		scp = new JScrollPane();
		scp.getViewport().setView(p);
		scp.getViewport().setBackground(Color.white);

		f.add(scp);

		JLabel lbl = new JLabel();
		lbl.setText("「トレース実行による可視化」実行時の設定");
		lbl.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		lbl.setBounds(10, y, lbl.getPreferredSize().width, lbl.getPreferredSize().height);
		p.add(lbl);

		y += lbl.getHeight() + 10;

		setLabel("プログラムの引数を入力してください\n（引数が複数ある場合は半角スペースで区切って入力してください）");
		paraTf = setTextField();

		setLabel("ブレークメソッドを設定します\n設定したいメソッド名を正しく入力してください");
		methodTf = setTextField();

		setLabel("メソッドが定義されているクラス名を正しく入力してください");
		classTf = setTextField();

		setLabel("引数の型を入力します\n（図からの設定のみ）");
		mParaTf = setTextField();
		mParaTf.setEditable(false);
	}

	private void setLabel(String text) {
		JTextPane lbl = new JTextPane();
		lbl.setText(text);
		lbl.setOpaque(false);
		lbl.setEditable(false);
		lbl.setFocusable(false);
		lbl.setBounds(10, y, lbl.getPreferredSize().width, lbl.getPreferredSize().height);
		p.add(lbl);

		y += lbl.getHeight() + 5;
	}

	private JTextField setTextField() {
		JTextField tf = new JTextField(30);
		tf.setBounds(10, y, tf.getPreferredSize().width, tf.getPreferredSize().height);
		p.add(tf);

		y += tf.getHeight() + 20;

		return tf;
	}

	public static JTextField getParaTf() {
		return paraTf;
	}

	public static JTextField getMethodTf() {
		return methodTf;
	}

	public static JTextField getClassTf() {
		return classTf;
	}

	public static JTextField getmParaTf() {
		return mParaTf;
	}

	public static ArrayList<String> getParaList() {
		return paraList;
	}

	public static void setParaList(ArrayList<String> paraList) {
		ConfigurationView.paraList = paraList;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}

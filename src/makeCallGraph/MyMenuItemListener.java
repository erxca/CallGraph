package makeCallGraph;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

public class MyMenuItemListener extends MouseAdapter {
	private String methodPath;
	private JScrollPane scp;
	private JTextPane tp = new JTextPane() {
		private static final long serialVersionUID = 1L;

		@Override
		public boolean getScrollableTracksViewportWidth() {
			return false; // 折り返しを行わない
		}
	};
	StyleContext sc = new StyleContext();
	DefaultStyledDocument doc = new DefaultStyledDocument(sc);
	private MethodDetail metMod;
	private boolean firstLine = false;
	private int left, right;

	public MyMenuItemListener(String methodPath, JScrollPane scp) {
		this.methodPath = methodPath;
		this.scp = scp;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		action();
	}

	public void action() {
		String classPath = methodPath;
		StringBuffer buf = new StringBuffer();
		int dotNum;

		if (classPath.indexOf(".java") == -1) {
			buf.append(".\\workspace\\data\\src");
			while ((dotNum = classPath.indexOf(".")) >= 0) {
				buf.append("\\");
				buf.append(classPath.substring(0, dotNum));
				classPath = classPath.substring(dotNum + 1);
			}
			buf.append(".java");
			classPath = buf.toString();
		}
		try {
			setTextPane(classPath);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public void setTextPane(String filePath) throws FileNotFoundException {
		try {// ドキュメント全消去
			doc.remove(0, doc.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		this.scp.getViewport().setView(tp);
		tp.setStyledDocument(doc);

		// タブサイズの設定
		int tabSize = 2; // 設定したいタブサイズ(文字数)
		FontMetrics fm = tp.getFontMetrics(tp.getFont());
		int charWidth = fm.charWidth('m');
		int tabLength = charWidth * tabSize;
		TabStop[] tabs = new TabStop[10];
		for (int j = 0; j < tabs.length; j++) {
			tabs[j] = new TabStop((j + 1) * tabLength);
		}
		TabSet tabSet = new TabSet(tabs);
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setTabSet(attrs, tabSet);
		tp.getStyledDocument().setParagraphAttributes(0, tp.getDocument().getLength(), attrs, false);

		tp.setEditable(false);
		tp.setBackground(Color.white);
		showSourceCode(filePath);
	}

	public void showSourceCode(String filePath) throws FileNotFoundException {
		File file = new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String str;
		int n = 0;
		int off = 0;
		left = 0;
		right = 0;
		int start = 0;
		int count = 0;

		metMod = checkMod();
		try {
			while ((str = br.readLine()) != null) {
				StringBuffer sb = new StringBuffer();
				sb.append(str + "\n");
				int end = 0;
				if (metMod != null) {
					if (metMod.getReturnValue() == null && str.indexOf(metMod.getMethodName()) != -1) {
						n++;
						if (n == 2) {
							firstLine = true;
							start = off;
						}
					} else if (metMod.getReturnValue() != null && str.indexOf("(") != -1) {
						if (str.indexOf(metMod.getReturnValue()) != -1 && str.indexOf(metMod.getMethodName()) != -1
								&& str.indexOf(metMod.getMethodName()) < str.indexOf("(")) {
							firstLine = true;
							start = off;
						}
					}
					if (firstLine == true) {
						end = countBrakets(str);
					}

					if (start > 0 && count < 10) {
						start += sb.length();
						count++;
					}
					doc.insertString(off, new String(sb), sc.getStyle(StyleContext.DEFAULT_STYLE));
					MutableAttributeSet attr = new SimpleAttributeSet();
					StyleConstants.setBackground(attr, Color.pink);
					doc.setCharacterAttributes(off, end, attr, true);
					off += sb.length();
				} else {
					doc.insertString(off, new String(sb), sc.getStyle(StyleContext.DEFAULT_STYLE));
					off += sb.length();
				}
			}
			br.close();
			tp.setCaretPosition(start);
		} catch (IOException | BadLocationException e) {
			e.printStackTrace();
		}
	}

	private int countBrakets(String str) { // メソッド全体をハイライトするために｛｝を数える
		String tempStr = str;
		int leftIndex = 0, rightIndex = 0;
		int cutStr = 0;

		leftIndex = str.substring(cutStr).indexOf("{");
		rightIndex = str.substring(cutStr).indexOf("}");
		while (tempStr != null && (leftIndex != -1 || rightIndex != -1)) {
			if (leftIndex != -1 && rightIndex != -1) {
				if (leftIndex < rightIndex) {
					cutStr = leftIndex + 1;
					left++;
				} else {
					cutStr = rightIndex + 1;
					right++;
				}
			} else if (leftIndex != -1 && rightIndex == -1) {
				cutStr = leftIndex + 1;
				left++;
			} else if (leftIndex == -1 && rightIndex != -1) {
				cutStr = rightIndex + 1;
				right++;
			}
			tempStr = tempStr.substring(cutStr);
			leftIndex = tempStr.indexOf("{");
			rightIndex = tempStr.indexOf("}");
		}

		if (left == right) {
			firstLine = false;
		}
		return str.length();
	}

	private MethodDetail checkMod() {
		for (MethodDetail metMod : LoadMethod.methodList) {
			if (metMod.getMethodPath().equals(this.methodPath)) {
				return metMod;
			}
		}
		return null;
	}
}

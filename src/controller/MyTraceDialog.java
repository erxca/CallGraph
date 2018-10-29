package controller;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MyTraceDialog extends Dialog {
	Composite composite;
	Text text;

	public MyTraceDialog(Shell parent) {
		super(parent);
	}

	protected Point getInitialSize() {
		return new Point(420, 200);
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("実行時設定");
	}

	protected Control createDialogArea(Composite parent) {

		composite = (Composite) super.createDialogArea(parent);

		Label label1 = new Label(composite, SWT.NULL);
		label1.setText("「『トレース実行による可視化』実行時の設定」は設定済みですか？");
		Label label2 = new Label(composite, SWT.NULL);
		label2.setText("設定済みの場合は「OK」を押すと可視化が始まります。");
		Label label3 = new Label(composite, SWT.NULL);
		label3.setText("未設定の場合は「キャンセル」を押して設定を行ってください。");

		// text = new Text(composite, SWT.SINGLE | SWT.BORDER);
		// GridData gridData1 = new GridData();
		// gridData1.horizontalAlignment = GridData.FILL;
		// gridData1.grabExcessHorizontalSpace = true;
		// text.setLayoutData(gridData1);
		// // text.setLayoutData(new GridData(GridData.FILL_BOTH));
		// text.setText("test");
		return composite;

	}
	//
	// public String getArgs() {
	//
	// return text.getText();
	// }

}

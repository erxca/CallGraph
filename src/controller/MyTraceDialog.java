package controller;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
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
		label1.setText("プログラムの引数を入力してください");
		Label label2 = new Label(composite, SWT.NULL);
		label2.setText("（引数が複数ある場合は半角スペースで区切って入力してください）");

		text = new Text(composite, SWT.SINGLE | SWT.BORDER);
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		text.setLayoutData(gridData1);
		// text.setLayoutData(new GridData(GridData.FILL_BOTH));
		text.setText("test");
		return composite;

	}

	public String getArgs() {

		return text.getText();
	}

}

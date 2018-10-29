package controller;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import model.Parsing;
import view.TraceGraphPanel;

public class StartFromLeadHandler extends AbstractHandler {
	Shell shell = Display.getDefault().getActiveShell();
	public static int traceNum = 0;
	public static TraceGraphPanel tgp = null;
	static boolean start = false;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub

		// 描画の処理
		if (!start) {
			MessageDialog.openInformation(shell, "error", "トレース結果がありません。\nトレースを行ってください。");
			return null;
		}

		if (tgp != null) {

			tgp.checkMethod(traceNum);

			if (traceNum == 0) {
				Parsing.olf.write("PROCEED\tfrom start");
			} else {
				Parsing.olf.write("PROCEED\tnext");
			}

			traceNum++;
			// System.out.println(traceNum);
		}

		return null;
	}

}

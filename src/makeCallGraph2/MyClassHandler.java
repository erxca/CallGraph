package makeCallGraph2;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class MyClassHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		ISelection selection = HandlerUtil.getActiveMenuSelectionChecked(event);
		System.out.println(selection);
		String sel = selection.toString();
		int end = sel.indexOf(".java");
		String temp = sel.substring(0, end);
		int i, start = 0;
		if ((i = temp.lastIndexOf("[")) >= 0) {
			start = i + 1;
		} else if ((i = temp.lastIndexOf(" ")) >= 0) {
			start = i + 1;
		} else {
			System.err.println("error!!");
		}
		String className = temp.substring(start, temp.length());

		System.out.println(className);
		return null;
	}

}

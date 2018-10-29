package controller;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;

import model.Parsing;

public class MyProjectHandler extends AbstractHandler {
	private Parsing parsing = new Parsing();
	static String pjtName = "";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		ISelection selection = HandlerUtil.getActiveMenuSelectionChecked(event);
		String sel = selection.toString();

		int idx;
		if ((idx = sel.indexOf("\n")) > 0) {
			pjtName = sel.substring(1, idx);
		} else if ((idx = sel.indexOf("]")) > 0) {
			pjtName = sel.substring(1, idx);

			if ((idx = pjtName.indexOf(" ")) > 0) {
				pjtName = pjtName.substring(0, idx);
			}
		}

		System.out.println(pjtName);
		// pjtName = sel.substring(1, idx); // selは"["から始まっているので
		// // System.out.println(pjtName);
		try {
			parsing.getWorkspaceInfo(pjtName);
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}

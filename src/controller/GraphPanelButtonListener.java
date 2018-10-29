package controller;

import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import model.Method;

public class GraphPanelButtonListener extends ClassPanelButtonListener {
	protected JPopupMenu popup;

	public GraphPanelButtonListener(Method caller, Method callee, ArrayList<Integer> calledLineList) {

		super(callee);

		popup = super.popup;
		StringBuffer sb = new StringBuffer("呼び出し行を表示（");
		sb.append(calledLineList.size());
		sb.append("ヶ所）");
		JMenuItem view2 = new JMenuItem(sb.toString());

		view2.addMouseListener(new ShowCalledLineAdapter(callee, calledLineList, caller.getPath()));
		popup.add(view2);

	}
}

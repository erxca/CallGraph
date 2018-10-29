package controller;

import java.util.ArrayList;

import javax.swing.JMenuItem;

import model.Method;

public class TracePanelButtonListener extends GraphPanelButtonListener {

	public TracePanelButtonListener(Method caller, Method callee, ArrayList<Integer> calledLineList) {
		super(caller, callee, calledLineList);
		// TODO Auto-generated constructor stub

		JMenuItem view = new JMenuItem("静的解析結果を使用して可視化");

		view.addMouseListener(new TracePanelButtonAdapter(callee));
		popup.add(view);
	}

}

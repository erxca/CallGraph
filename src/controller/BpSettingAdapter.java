package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.Method;
import model.Parsing;

public class BpSettingAdapter extends MouseAdapter {
	Method m;

	public BpSettingAdapter(Method m) {
		// TODO Auto-generated constructor stub
		this.m = m;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		setConfiguration();
	}

	public void setConfiguration() {

		view.ConfigurationView.getMethodTf().setText(m.getMethodName());
		view.ConfigurationView.getClassTf().setText(m.getDeclaringClassName());

		StringBuffer sb = new StringBuffer();
		for (String s : m.getParametersList()) {
			sb.append(s);
			sb.append(" ");
		}

		view.ConfigurationView.getmParaTf().setText(sb.toString());
		view.ConfigurationView.setParaList(m.getParametersList());

		view.ConfigurationView.p.invalidate();
		view.ConfigurationView.f.validate();
		view.ConfigurationView.p.repaint();

		StringBuffer sbf = new StringBuffer();
		sbf.append(m.getDeclaringClassName());
		sbf.append(".");
		sbf.append(m.getMethodName());

		Parsing.olf.write("SETTING\tbreapoint\t\t", sbf.toString());
	}
}

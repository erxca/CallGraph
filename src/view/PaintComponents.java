package view;

import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import model.Class;

public class PaintComponents {
	MyGraphPanel p;
	Frame f;
	JScrollPane scp;
	Class c;
	ArrayList<JButton> btnList = new ArrayList<JButton>();
	IWorkbenchPage page;

	public PaintComponents(Frame f, JScrollPane scp, IWorkbenchPage page, Class c) {

		this.f = f;
		this.scp = scp;
		this.c = c;
		this.page = page;

	}

	private void init() {

		p = new MyGraphPanel(page, f);
		scp.getViewport().setView(p);
		f.repaint();

	}

	public void makeClassCps() {

		init();
		MyClassPanel cp = new MyClassPanel(f, p, c);
		p.setCp(cp);
		p.add(cp);
		p.setVisible(true);

		p.invalidate();
		p.validate();

		try {
			page.showView("tool.test.views.TestView");
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

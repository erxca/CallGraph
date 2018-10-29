package view;

import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import model.Class;

public class PaintTraceView {
	MyGraphPanel p;
	Frame f;
	JScrollPane scp;
	Class c;
	ArrayList<JButton> btnList = new ArrayList<JButton>();
	IWorkbenchPage page;
	String selectedClass;

	public PaintTraceView(Frame f, JScrollPane scp, IWorkbenchPage page, Class c) {

		this.f = f;
		this.scp = scp;
		this.c = c;
		this.page = page;

	}

	private void init() {

		p = new TraceGraphPanel(page, f);
		// ((TraceGraphPanel) p).setSelectedClass(selectedClass);
		// ((TraceGraphPanel) p).setMethodOfClassPanelList(c.getMethodList());
		// ((TraceGraphPanel) p).setName("nameeee");
		scp.getViewport().setView(p);
		f.repaint();

		controller.StartFromLeadHandler.tgp = (TraceGraphPanel) p;
		controller.StartFromBreakpointHandler.tgp = (TraceGraphPanel) p;
	}

	public void makeClassCps() {

		init();
		TraceClassPanel cp = new TraceClassPanel(f, p, c);
		cp.makePanel();
		p.setCp(cp);
		p.add(cp);
		p.setVisible(true);

		p.invalidate();
		p.validate();

		try {

			page.showView("tool.test.views.TraceView");

		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setSelectedClass(String selectedClass) {
		this.selectedClass = selectedClass;
	}
}

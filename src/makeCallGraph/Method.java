package makeCallGraph;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JScrollPane;

public class Method {
	private String methodPath;
	private int methodNum;
	private JButton btn;
	private double x1, y1;
	private Method parentMethod;
	private int colorNum;

	Method(String methodName, String methodPath, int methodNum, JButton btn, Method parentMethod, MyPanel p,
			JScrollPane scp1, JScrollPane scp2) {
		this.methodPath = methodPath;
		this.methodNum = methodNum;
		this.btn = btn;
		this.parentMethod = parentMethod;
		this.colorNum = -1;

		this.btn.setText(methodName);
		this.btn.setBackground(Color.WHITE);
		this.btn.setName("Button");
		this.btn.setActionCommand(methodPath);
		this.btn.addMouseListener(new MyButtonListener(methodPath, methodName, scp1, scp2, p, this));
	}

	public String getMethodPath() {
		return methodPath;
	}

	public int getMethodNum() {
		return methodNum;
	}

	public JButton getBtn() {
		return btn;
	}

	public void setBtn(JButton btn) {
		this.btn = btn;
	}

	public double getX1() {
		return x1;
	}

	public void setX1(double x1) {
		this.x1 = x1;
	}

	public double getY1() {
		return y1;
	}

	public void setY1(double y1) {
		this.y1 = y1;
	}

	public Method getParentMethod() {
		return parentMethod;
	}

	public void setParentMethod(Method parentMethod) {
		this.parentMethod = parentMethod;
	}

	public int getColorNum() {
		return colorNum;
	}

	public void setColorNum(int colorNum) {
		this.colorNum = colorNum;
	}
}

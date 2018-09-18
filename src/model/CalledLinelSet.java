package model;

public class CalledLinelSet {
	private String methodName;
	private int lineNum;
	private boolean isChanged = false; // 文字位置を行数へ変換済みかどうか

	public CalledLinelSet(String methodName, int lineNum) {
		this.methodName = methodName;
		this.lineNum = lineNum;
	}

	public String getMethodName() {
		return methodName;
	}

	public int getLineNum() {
		return lineNum;
	}

	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

}

package makeCallGraph;

public class MethodDetail {
	private String methodPath;
	private String methodName;
	private String returnValue;
	private String filePath;

	public MethodDetail(String methodPath) {
		this.methodPath = methodPath;
		this.methodName = methodPath.substring(methodPath.lastIndexOf(".") + 1);

	}

	public String getMethodPath() {
		return methodPath;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getFilePath() {
		return filePath;
	}

}

package model.trace;

import model.Method;

public class CallerAndCallee {
	Method caller, callee;
	boolean isDown; // trueなら呼び出し先への遷移、falseなら呼び出し元への遷移

	public CallerAndCallee(Method caller, Method callee, boolean isDown) {
		this.caller = caller;
		this.callee = callee;
		this.isDown = isDown;

		if (caller == null) {
			this.caller = dealWithNullMethod();
		}
		if (callee == null) {
			this.callee = dealWithNullMethod();
		}

		// showSet();
	}

	private Method dealWithNullMethod() {
		Method m = new Method("null", null, null);
		return m;
	}

	public Method getCaller() {
		return caller;
	}

	public Method getCallee() {
		return callee;
	}

	public boolean isDown() {
		return isDown;
	}

	public void showSet() {
		System.out.print(caller.getMethodName() + " --> " + callee.getMethodName() + " ( ");
		if (isDown) {
			System.out.print("down");
		} else {
			System.out.print("up");
		}
		System.out.println(" )");
	}
}

package sk.anivit.stacktracegen.recording;

import java.util.ArrayList;

import javassist.CtClass;
import javassist.CtMethod;

public class CalledMethod {
	ArrayList<CalledMethod> children = new ArrayList<CalledMethod>();
	private CtMethod method;
	private CalledMethod parrent = null;
	private long start;
	private long stop;
	private CtClass containingClass;

	public CalledMethod(CtClass containingClass, CtMethod method) {
		this.containingClass = containingClass;
		this.method = method;
		this.start = System.currentTimeMillis();

	}

	public CtClass getContainingClass() {
		return containingClass;
	}

	public CtMethod getMethod() {
		return method;
	}

	public ArrayList<CalledMethod> getChildren() {
		return children;
	}

	public void addChild(CalledMethod m) {
		children.add(m);
		m.setParrent(this);
	}

	public void setParrent(CalledMethod parrent) {
		this.parrent = parrent;
	}

	public CalledMethod getParrent() {
		return parrent;
	}

	public void methodReturned() {
		stop = System.currentTimeMillis();

	}

	public long getRunTime() {
		
		return stop-start;
	}
@Override
public String toString() {
	String r = containingClass.getName()+":"+method.getName()+"["+getRunTime()+"]"+"\n";
	for ( CalledMethod c : children) {
		r+="	"+c.toString();
	}
	return r;
	
}
}

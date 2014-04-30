package sk.anivit.stacktracegen.recording;

import java.util.ArrayList;

import sk.anivit.stacktracegen.annotations.SequenceDiagram;
import javassist.CtClass;
import javassist.CtMethod;

public class DiagramBean {

	private String fileName;

	private String caption;

	private String outputDir;
	private CalledMethod root = null;
	private CalledMethod lastMethod = null;
	private long startTime;
	private long stopTime;

	private CtMethod entryPoint;

	private ArrayList<CtClass> recordedDiagramClasses = new ArrayList<CtClass>();
	private ArrayList<CtClass> allDiagramClasses;
	private String label;

	public DiagramBean(CtMethod entryPoint, String caption, String fileName,
			String outputDir, ArrayList<CtClass> dClasses, String label) {
		this.entryPoint = entryPoint;
		root = new CalledMethod(entryPoint.getDeclaringClass(), entryPoint);
		allDiagramClasses = dClasses;
		this.caption = caption;
		this.fileName = fileName;
		this.outputDir = outputDir;
		this.label = label;

	}

	public ArrayList<CtClass> getAllDiagramClasses() {
		return allDiagramClasses;
	}

	public ArrayList<CtClass> getRecordedDiagramClasses() {
		return recordedDiagramClasses;
	}

	public static DiagramBean newInstance(CtMethod entryPoint, String caption,
			String fileName, String outputDir, ArrayList<CtClass> dClasses,
			String label) {

		return new DiagramBean(entryPoint, caption, fileName, outputDir,
				dClasses, label);
	}

	public void setOutputFileName(String fileName) {
		this.fileName = fileName;

	}

	public void setCaption(String caption) {
		this.caption = caption;

	}

	public String getLabel() {
		return label;
	}

	public String getCaption() {
		return caption;
	}

	public String getFileName() {
		return fileName;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;

	}

	public void setStopTime(long stopTime) {
		this.stopTime = stopTime;
		

	}

	public String getOutputDir() {
		return outputDir;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getStopTime() {
		return stopTime;
	}

	public void methodCalled(CalledMethod m) {

		if (!recordedDiagramClasses.contains(m.getMethod().getDeclaringClass())) {
			recordedDiagramClasses.add(m.getMethod().getDeclaringClass());
		}
	
		if (lastMethod == null) {
			root.addChild(m);
			lastMethod = m;

		} else {
			lastMethod.addChild(m);
			lastMethod = m;
		}
	}

	public void methodReturned() {
		lastMethod.methodReturned();
		
		lastMethod = lastMethod.getParrent();

	}

	public CalledMethod getRoot() {
		return root;
	}

	public CtMethod getEntryPoint() {
		return entryPoint;
	}
}

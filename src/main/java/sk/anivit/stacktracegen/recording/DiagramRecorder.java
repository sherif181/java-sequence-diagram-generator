package sk.anivit.stacktracegen.recording;

import java.io.IOException;
import java.util.ArrayList;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import sk.anivit.stacktracegen.writer.LatexWriter;
import sk.anivit.stacktracegen.writer.WriterI;

public class DiagramRecorder {
	static ClassPool pool = ClassPool.getDefault();
	protected static DiagramBean currentDiagramBean;
	private static ArrayList<DiagramBean> records = new ArrayList<DiagramBean>();
	private static WriterI writer = new LatexWriter();
	static java.util.logging.Logger log = java.util.logging.Logger
			.getLogger(DiagramBean.class.toString());

	public static void startRecording(String entryPointName, String caption,
			String FileName, String outputDir, String diaClasses, String label) {
		String[] l = diaClasses.replace("[", "").replace("class", "")
				.replace("]", "").replace(" ", "").split(",");
		try {
			ArrayList<CtClass> dClasses = new ArrayList<CtClass>();
			for (String s : l) {
				dClasses.add(pool.get(s));
			}
			String[] ms = entryPointName.split(":");

			CtClass cls = pool.get(ms[0]);

			CtMethod entryPoint = cls.getDeclaredMethod(ms[1]);
			currentDiagramBean = DiagramBean.newInstance(entryPoint, caption,
					FileName, outputDir, dClasses, label);
			currentDiagramBean.setStartTime(System.currentTimeMillis());
			log.info("Recording started " + entryPointName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}


	public static void methodCalled(String m) {
		if(currentDiagramBean==null){
			return;
		}
		log.fine("Method called " + m);
		String[] ms = m.split(":");
		try {
			CtClass cls = pool.get(ms[0]);
			CtMethod method = cls.getDeclaredMethod(ms[1]);
			
			currentDiagramBean.methodCalled(new CalledMethod(cls, method));

		} catch (Exception e) {
			log.severe("Method called error " + e.getMessage());
		}
	}

	public static void stopRecording() {
		currentDiagramBean.getRoot().methodReturned();
		currentDiagramBean.setStopTime(System.currentTimeMillis());
		records.add(currentDiagramBean);
		log.info("Recording stoped "
				+ currentDiagramBean.getEntryPoint().getLongName());
		
		try {
			writer.write(currentDiagramBean);
		} catch (IOException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		currentDiagramBean=null;
	}

	public static ArrayList<DiagramBean> getRecords() {
		return records;
	}

	public static void methodReturned() {
		if(currentDiagramBean==null){
			return;
		}
		try {

			currentDiagramBean.methodReturned();

		} catch (Exception e) {
			log.severe("Method called error " + e.getMessage());

		}
	}

	public static void setWriter(WriterI writer) {
		DiagramRecorder.writer = writer;
	}
}

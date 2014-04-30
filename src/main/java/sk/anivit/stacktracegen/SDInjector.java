package sk.anivit.stacktracegen;

import java.lang.reflect.Method;
import java.util.ArrayList;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import sk.anivit.stacktracegen.annotations.Caption;
import sk.anivit.stacktracegen.annotations.FileName;
import sk.anivit.stacktracegen.annotations.Label;
import sk.anivit.stacktracegen.annotations.OutputDirectory;
import sk.anivit.stacktracegen.annotations.SequenceDiagram;
import sk.anivit.stacktracegen.recording.DiagramRecorder;

public class SDInjector {
	private ArrayList<Class<?>> clsInDiagrams = new ArrayList<Class<?>>();//contains all classes which have been  modified to call the record methods
	java.util.logging.Logger log = java.util.logging.Logger
			.getLogger(SDInjector.class.toString());

	public void entryMethod(CtMethod m) throws CannotCompileException {
		CtClass dClass = m.getDeclaringClass();
		String entryPointName = dClass.getName() + ":" + m.getName();
		String caption = null;
		String label = null;
		try {
			if (m.hasAnnotation(Caption.class)) {

				caption = ((Caption) m.getAnnotation(Caption.class)).value();
				log.info("caption: " + caption);
			}
			String fileName;
			if (m.hasAnnotation(FileName.class)) {
				fileName = ((FileName) m.getAnnotation(FileName.class)).value();
			} else {
				fileName = entryPointName.replace(":", ".") + ".tex";
				log.info("fileName: " + fileName);
			}

			String outDir = null;
			if (dClass.hasAnnotation(OutputDirectory.class)) {
				outDir = ((OutputDirectory) dClass
						.getAnnotation(OutputDirectory.class)).value();

			}

			if (m.hasAnnotation(OutputDirectory.class)) {
				outDir = ((OutputDirectory) m
						.getAnnotation(OutputDirectory.class)).value();
			}
			if (m.hasAnnotation(Label.class)) {
				label = ((Label) m.getAnnotation(Label.class)).value();
			}

			if (outDir == null) {
				outDir = "doc";
			}
			if (caption == null) {
				caption = entryPointName.replace(":", " ");
			}

			if (label == null) {
				label = entryPointName.replace(":", ".");
			}
			log.info("outDir: " + outDir);
			log.info("Entry method " + entryPointName);
			log.info("caption: " + caption);
			ArrayList<Class<?>>diagramsToRender =new ArrayList<Class<?>>();
			
			for (Class<?> c :((SequenceDiagram)m.getAnnotation(SequenceDiagram.class)).value()) {
				diagramsToRender.add(c);
			}
			m.insertBefore(DiagramRecorder.class.getName()
					+ ".startRecording(\"" + entryPointName + "\",\"" + caption
					+ "\",\"" + fileName + "\",\"" + outDir + "\",\""
					+ diagramsToRender + "\",\"" + label + "\");");
			m.insertAfter(DiagramRecorder.class.getName() + ".stopRecording();");

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

	}

	public void addDiagramClass(Class<?> c) {
		if (!clsInDiagrams.contains(c)) {
			clsInDiagrams.add(c);
		}

	}

	public void diagramClass(CtClass ct) throws CannotCompileException {


		for (CtMethod m : ct.getDeclaredMethods()) {
			

			if (!m.getDeclaringClass().getName().equals(Object.class.getName())) {

				m.insertBefore(DiagramRecorder.class.getName()
						+ ".methodCalled(\"" + ct.getName() + ":" + m.getName()
						+ "\");");
				m.insertAfter(DiagramRecorder.class.getName()
						+ ".methodReturned();");
				log.fine("Method listener " + m.getDeclaringClass() + ":"
						+ m.getName());
			}

		}
	}

	public boolean isDiagramClass(CtClass ct) {
		for (Class<?> c : clsInDiagrams) {

			if (c.getName().equals(ct.getName())) {

				return true;
			}
		}
		return false;
	}

}

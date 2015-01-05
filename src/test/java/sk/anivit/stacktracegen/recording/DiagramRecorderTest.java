package sk.anivit.stacktracegen.recording;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import org.junit.Test;

import sk.anivit.stacktracegen.SDInjector;
import sk.anivit.stacktracegen.writer.WriterI;

public class DiagramRecorderTest {

	@Test
	public void testStartRecording() throws Exception {
		ClassPool pool = ClassPool.getDefault();

		String entryPointName = sk.anivit.stacktracegen.recording.Test.class
				.getName() + ":testik";
		String caption = "caption";
		String fileName = "fn";
		String outputDir = "od";
		String diaClasses = sk.anivit.stacktracegen.recording.Test.class
				.getName();
		DiagramRecorder.startRecording(entryPointName, caption, fileName,
				outputDir, diaClasses, "label");
		assertEquals(DiagramRecorder.currentDiagramBean.getEntryPoint(), pool
				.get(sk.anivit.stacktracegen.recording.Test.class.getName())
				.getDeclaredMethod("testik"));
		assertEquals(DiagramRecorder.currentDiagramBean.getCaption(), caption);
		assertEquals(DiagramRecorder.currentDiagramBean.getFileName(), fileName);

		assertEquals(DiagramRecorder.currentDiagramBean.getOutputDir(),
				outputDir);
		assertEquals(DiagramRecorder.currentDiagramBean.getDiagramClasses()
				.size(), 1);
		assertTrue(DiagramRecorder.currentDiagramBean.getDiagramClasses()
				.contains(
						pool.get(sk.anivit.stacktracegen.recording.Test.class
								.getName())));

	}

	@Test
	public void testMethodCalled() throws Exception {
		ClassPool pool = ClassPool.getDefault();

		String entryPointName = sk.anivit.stacktracegen.recording.Test.class
				.getName() + ":testik";
		String caption = "caption";
		String fileName = "fn";
		String outputDir = "od";
		String diaClasses = sk.anivit.stacktracegen.recording.Test.class
				.getName();
		DiagramRecorder.startRecording(entryPointName, caption, fileName,
				outputDir, diaClasses, "");

		DiagramRecorder
				.methodCalled(sk.anivit.stacktracegen.recording.Test.class
						.getName() + ":testik");

		DiagramRecorder
				.methodCalled(sk.anivit.stacktracegen.recording.Test.class
						.getName() + ":testik");
		DiagramRecorder.methodReturned();
		DiagramRecorder.methodReturned();
		CalledMethod r = DiagramRecorder.currentDiagramBean.getRoot();
		assertEquals(1, r.getChildren().size());
		assertEquals(
				pool.get(sk.anivit.stacktracegen.recording.Test.class.getName())
						.getDeclaredMethod("testik"), r.getMethod());

	}

	@Test
	public void testStopRecording() throws Exception {
		final ClassPool pool = ClassPool.getDefault();

		String entryPointName = sk.anivit.stacktracegen.recording.Test.class
				.getName() + ":testik";
		String caption = "caption";
		String fileName = "fn";
		String outputDir = "od";
		String diaClasses = sk.anivit.stacktracegen.recording.Test.class
				.getName();
		DiagramRecorder.startRecording(entryPointName, caption, fileName,
				outputDir, diaClasses, "");

		DiagramRecorder
				.methodCalled(sk.anivit.stacktracegen.recording.Test.class
						.getName() + ":testik");

		DiagramRecorder
				.methodCalled(sk.anivit.stacktracegen.recording.Test.class
						.getName() + ":testik");
		DiagramRecorder.methodReturned();
		DiagramRecorder.methodReturned();

		DiagramRecorder.setWriter(new WriterI() {

			@Override
			public void write(DiagramBean bean) throws IOException {
				CalledMethod r = DiagramRecorder.currentDiagramBean.getRoot();
				assertEquals(1, r.getChildren().size());
				try {
					assertEquals(
							pool.get(
									sk.anivit.stacktracegen.recording.Test.class
											.getName()).getDeclaredMethod(
									"testik"), r.getMethod());
				} catch (NotFoundException e) {
					fail(e.getMessage());
					e.printStackTrace();
				}
				bean.setStopTime(-10);

			}
		});
		DiagramBean b = DiagramRecorder.currentDiagramBean;
		DiagramRecorder.stopRecording();
		assertEquals(-10, b.getStopTime());

	}

	@Test
	public void testAbstractMethodRecording() throws Exception {
		SDInjector i = new SDInjector();

		CtClass t = ClassPool.getDefault().makeClass("test");
		CtClass sup = ClassPool.getDefault().makeClass("test");
		t.setSuperclass(sup);

		CtMethod m = CtNewMethod.make(
				"public void test() { System.out.println(\"test ok\"); }",
				sup);
		sup.addMethod(m);

		i.diagramClass(t);
		System.out.println(m.getMethodInfo().getDescriptor());

	}

}

package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import javassist.ClassPool;
import javassist.CtClass;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sk.anivit.stacktracegen.annotations.Caption;
import sk.anivit.stacktracegen.annotations.FileName;
import sk.anivit.stacktracegen.annotations.Label;
import sk.anivit.stacktracegen.annotations.OutputDirectory;
import sk.anivit.stacktracegen.annotations.SequenceDiagram;
import sk.anivit.stacktracegen.recording.DiagramBean;
import sk.anivit.stacktracegen.recording.DiagramRecorder;

public class AnnotationTest {


	private int test;
	private  static final int NO_ANNOTATIONS=0;
	private static final int FILE_NAME=1;
	private static final int OUTPUT_DIR = 2;
	private static final int LABEL = 3;
	private static final int CAPTION = 4;
	
	@Before
	public void setUp() {

		DiagramRecorder.getRecords().clear();
	}
	
	@Test
	@SequenceDiagram({ Controller.class, Model.class })
	public void noAnnotationsTest() {
		test = NO_ANNOTATIONS;
		Controller c = new Controller(new Model());
		c.init();
		c.simpleBeanOperation();
	}
	@Test
	@FileName("test")
	@SequenceDiagram({ Controller.class, Model.class })
	public void fileNameTest() {
		test = FILE_NAME;
		Controller c = new Controller(new Model());
		c.init();
		c.simpleBeanOperation();
	}
	
	@Test
	@OutputDirectory("test1/test2/test3")
	@SequenceDiagram({ Controller.class, Model.class })
	public void outputDirTest() {
		test = OUTPUT_DIR;
		Controller c = new Controller(new Model());
		c.init();
		c.simpleBeanOperation();
	}
	
	@Test
	@Label("LABEL")
	@SequenceDiagram({ Controller.class, Model.class })
	public void labelTest() {
		test = LABEL;
		Controller c = new Controller(new Model());
		c.init();
		c.simpleBeanOperation();
	}
	@Test
	@Caption("CAPTION")
	@SequenceDiagram({ Controller.class, Model.class })
	public void captionTest() {
		test = CAPTION;
		Controller c = new Controller(new Model());
		c.init();
		c.simpleBeanOperation();
	}
	@After
	public void tearDown() throws Exception {
		DiagramBean rec = DiagramRecorder.getRecords().get(0);
		
		if(test==NO_ANNOTATIONS){
			checkNoAnnotations(rec);
		}
		if(test==FILE_NAME){
			checkFileName(rec);
		}
		if(test==OUTPUT_DIR){
			checkOutputDir(rec);
		}
		
		
		if(test==CAPTION){
			checkCaption(rec);
		}
		if(test==LABEL){
			checkLabel(rec);
		}
	}

	private void checkLabel(DiagramBean rec) {
		assertEquals("LABEL", rec.getLabel());
	}

	private void checkCaption(DiagramBean rec) {
		assertEquals("CAPTION", rec.getCaption());
	}

	private void checkOutputDir(DiagramBean rec) {
		assertEquals("test1/test2/test3", rec.getOutputDir());
	}

	private void checkFileName(DiagramBean rec) {
		assertEquals("test", rec.getFileName());
		
	}

	private void checkNoAnnotations(DiagramBean rec) throws Exception {
		ClassPool pool = ClassPool.getDefault();
		ArrayList<CtClass> c = rec.getRecordedClasses();
		for (CtClass cl : c) {
			System.out.println(cl.getName());
		}
		assertEquals(2, rec.getDiagramClasses().size());
		assertEquals(pool.get(Controller.class.getName()), rec.getDiagramClasses().get(0));
		assertEquals(pool.get(Model.class.getName()), rec.getDiagramClasses().get(1));
		assertEquals("test.AnnotationTest.noAnnotationsTest.tex", rec.getFileName());
		assertEquals("test.AnnotationTest.noAnnotationsTest", rec.getLabel());
		assertEquals("test.AnnotationTest noAnnotationsTest", rec.getCaption());
	}
}

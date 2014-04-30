package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sk.anivit.stacktracegen.annotations.SequenceDiagram;
import sk.anivit.stacktracegen.recording.CalledMethod;
import sk.anivit.stacktracegen.recording.DiagramBean;
import sk.anivit.stacktracegen.recording.DiagramRecorder;
import static org.junit.Assert.fail;

public class DiagramStructureTest {
	private static final int SIMPLE = 0;
	private static final int RECURSIVE = 1;
	private static final int FOR = 2;
	private static final int LONG = 3;
	private int test;

	@Test
	@SequenceDiagram({ Controller.class, Model.class, Bean.class,
			Persistor.class })
	public void simpleStructureTest() {
		test = SIMPLE;
		Controller c = new Controller(new Model());
		c.init();
		c.simpleBeanOperation();
	}

	@Test
	@SequenceDiagram({ Controller.class, Model.class, Bean.class,
			Persistor.class })
	public void longBeanOperation() {
		test = LONG;
		Controller c = new Controller(new Model());
		c.init();
		c.longBeanOperation();
	}

	@Test
	@SequenceDiagram({ Controller.class, Model.class, Bean.class,
			Persistor.class })
	public void recursiveBeanOperation() {
		test = RECURSIVE;
		Controller c = new Controller(new Model());
		c.init();
		c.recursiveBeanOperation();
	}

	@Test
	@SequenceDiagram({ Controller.class, Model.class, Bean.class,
			Persistor.class })
	public void forBeanOperation() {
		test = FOR;
		Controller c = new Controller(new Model());
		c.init();
		c.forBeanOperation();
	}

	@Before
	public void setUp() {

		DiagramRecorder.getRecords().clear();
	}

	@After
	public void tearDown() {

		assertEquals(1, DiagramRecorder.getRecords().size());
		DiagramBean rec = DiagramRecorder.getRecords().get(0);
		CalledMethod root = rec.getRoot();
		if (test == SIMPLE) {
			checkSimple(root);

		}

		if (test == LONG) {
			long actual = rec.getStopTime() - rec.getStartTime();

			assertEquals(actual, root.getRunTime());
			if(actual<1000){
				fail();
			}
			if(actual>1100){
				fail();
			}

		}

	}





	private void checkSimple(CalledMethod root) {

		ArrayList<CalledMethod> cRoot = root.getChildren();
		CalledMethod cRInit = cRoot.get(0);
		assertEquals("init", cRInit.getMethod().getName());
		CalledMethod cRINewInstance = cRInit.getChildren().get(0);
		CalledMethod cRSetBean = cRInit.getChildren().get(1);
		CalledMethod cRPersist = cRInit.getChildren().get(2);
		assertEquals("newInstance", cRINewInstance.getMethod().getName());
		assertEquals("setBean", cRSetBean.getMethod().getName());
		assertEquals("persist", cRPersist.getMethod().getName());

		CalledMethod cRSimpleOp = cRoot.get(1);
		assertEquals("simpleBeanOperation", cRSimpleOp.getMethod().getName());
		assertEquals("getBean", cRSimpleOp.getChildren().get(0).getMethod()
				.getName());
		assertEquals("simpleBeanOperation", cRSimpleOp.getChildren().get(1)
				.getMethod().getName());
		assertEquals("setBean", cRSimpleOp.getChildren().get(2).getMethod()
				.getName());
		assertEquals("persist", cRSimpleOp.getChildren().get(3).getMethod()
				.getName());
		assertEquals("persist", cRSimpleOp.getChildren().get(3).getChildren()
				.get(0).getMethod().getName());

		assertEquals(2, cRoot.size());

		assertEquals(3, cRInit.getChildren().size());
		assertEquals(4, cRSimpleOp.getChildren().size());

	}

}

package sk.anivit.stacktracegen.test;

import org.junit.Test;

import sk.anivit.stacktracegen.annotations.Caption;
import sk.anivit.stacktracegen.annotations.FileName;
import sk.anivit.stacktracegen.annotations.Label;
import sk.anivit.stacktracegen.annotations.OutputDirectory;
import sk.anivit.stacktracegen.annotations.SequenceDiagram;

@OutputDirectory("build/test")


public class T {


	@Test
	@FileName("TestFile.tex")
	@Label("Tsdiagram")
	@Caption("This is a nice  sequence diagram`")
	@SequenceDiagram({ ExapleClass1.class ,ExapleClass2.class, ExapleClass3.class})
	public void testModifyClasses() {

		new ExapleClass1().exampleMethod11();
	}

	public class ExapleClass1 {

		public void exampleMethod11() {
			exampleMethod12();
			exampleMethod12();
			new ExapleClass2().exampleMethod21();
			new ExapleClass2().exampleMethod21();
			exampleMethod12();
		}

		public void exampleMethod12() {
			try {
				Thread.sleep(19);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public class ExapleClass2 {

		public void exampleMethod21() {
			for (int i = 0; i < 10; i++) {
				new ExapleClass3().exampleMethod31();
			}
			try {
				Thread.sleep(1100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public class ExapleClass3 {

		public void exampleMethod31() {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}

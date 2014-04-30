package sk.anivit.stacktracegen;

import java.util.ArrayList;

import org.junit.runner.JUnitCore;

import sk.anivit.stacktracegen.annotations.SequenceDiagram;
import sk.anivit.stacktracegen.finder.ClassFinder;
import sk.anivit.stacktracegen.finder.Visitor;
import sk.anivit.stacktracegen.recording.DiagramRecorder;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import javassist.Translator;

public class SDGeneratorMain {
	public static void main(String[] args) throws Throwable {
		final ArrayList<String> al = new ArrayList<String>();
		final ClassPool pool = ClassPool.getDefault();
		ClassFinder.findClasses(new Visitor<String>() {

			@Override
			public boolean visit(String t) {


				try {
					CtClass c = pool.get(t);
					for (CtMethod m : c.getMethods()) {
						if (m.hasAnnotation(SequenceDiagram.class)) {
							al.add(t);
							System.out.println("Generating diagram for "+t);
							break;
						}
					}

				} catch (NotFoundException e) {

				}
				return true;
			}
		});
		Translator xlat = new InjectorTranslator();

		Loader loader = new Loader();
		loader.addTranslator(pool, xlat);

		loader.run(JUnitCore.class.getName(),
				(String[]) al.toArray(new String[al.size()]));

	}
}

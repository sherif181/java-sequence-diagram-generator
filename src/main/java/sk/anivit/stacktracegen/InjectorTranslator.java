package sk.anivit.stacktracegen;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.Translator;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import sk.anivit.stacktracegen.annotations.OutputDirectory;
import sk.anivit.stacktracegen.annotations.SequenceDiagram;

class InjectorTranslator implements Translator {
	
	private static SDInjector injector = new SDInjector();
	public InjectorTranslator() throws NotFoundException {
	
		
			  Reflections reflections = new Reflections(new ConfigurationBuilder()
	            .setUrls(ClasspathHelper.forJavaClassPath())
	            .setScanners(new MethodAnnotationsScanner()));
	      	
	      	for ( Method m : reflections.getMethodsAnnotatedWith(SequenceDiagram.class)) {
				for (Class<?> c : m.getAnnotation(SequenceDiagram.class).value()) {
				
					injector.addDiagramClass(c);		
					
				}
	      		
			}
	      	
	      	

		
	}

	

	@Override
	public void onLoad(ClassPool pool, String classname)
			throws NotFoundException, CannotCompileException {

	

		CtClass ct = pool.getCtClass(classname);

		for (CtMethod m : ct.getMethods()) {
			if(m.hasAnnotation(SequenceDiagram.class)){
					injector.entryMethod(m);
			}
			
		}
		if(injector.isDiagramClass(ct)){
			injector.diagramClass(ct);
	
			
		}
		
	}

	@Override
	public void start(ClassPool pool) throws NotFoundException,
			CannotCompileException {
	}

}
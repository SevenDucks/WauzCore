package eu.wauz.wauzcore.system.annotations;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.reflections.Reflections;

/**
 * Used to load premade content through the use of annotations.
 * 
 * @author Wauzmons
 */
public class AnnotationLoader {
	
	/**
	 * A reflection instance for finding annotated classes.
	 */
	private Reflections reflections;
	
	/**
	 * Initializes and runs the loader.
	 */
	public static void init() {
		try {
			new AnnotationLoader().run();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new instance of the loader.
	 */
	private AnnotationLoader() {
		reflections = new Reflections("eu.wauz.wauzcore");
	}
	
	/**
	 * Runs the loader.
	 * 
	 * @throws Exception Failed to load a class.
	 */
	private void run() throws Exception {
		Command.CommandAnnotationHelper.init(this);
	}
	
	/**
	 * Gets all classes annotated with the given annotation.
	 * 
	 * @param annotationClass The class of the annotation.
	 * 
	 * @return All matching classes.
	 */
	public Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotationClass) {
		return reflections.getTypesAnnotatedWith(annotationClass);
	}

}

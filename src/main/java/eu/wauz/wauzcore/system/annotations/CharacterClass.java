package eu.wauz.wauzcore.system.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import eu.wauz.wauzcore.players.classes.WauzPlayerClass;
import eu.wauz.wauzcore.players.classes.WauzPlayerClassPool;

/**
 * An annotation to mark classes as character classes, to automatically register them on startup.
 * 
 * @author Wauzmons
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CharacterClass {
	
	/**
	 * An utility to help registering character classes.
	 * 
	 * @author Wauzmons
	 */
	public static class ClassAnnotationHelper {
		
		/**
		 * Registers instances of all classes, annotated with this annotation.
		 * 
		 * @param loader The annotation loader to use.
		 * 
		 * @throws Exception Failed to load a class.
		 */
		public static void init(AnnotationLoader loader) throws Exception {
			for(Class<?> clazz : loader.getAnnotatedClasses(CharacterClass.class)) {
				Object object = clazz.newInstance();
				WauzPlayerClassPool.registerClass((WauzPlayerClass) object);
			}
		}
		
	}
	
}

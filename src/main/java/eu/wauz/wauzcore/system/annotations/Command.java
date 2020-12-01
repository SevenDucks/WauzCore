package eu.wauz.wauzcore.system.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;

/**
 * An annotation to mark classes as commands, to automatically register them on startup.
 * 
 * @author Wauzmons
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
	
	/**
	 * An utility to help registering commands.
	 * 
	 * @author Wauzmons
	 */
	public static class CommandAnnotationHelper {
		
		/**
		 * Registers instances of all classes, annotated with this annotation.
		 * 
		 * @param loader The annotation loader to use.
		 * 
		 * @throws Exception Failed to load a class.
		 */
		public static void init(AnnotationLoader loader) throws Exception {
			for(Class<?> clazz : loader.getAnnotatedClasses(Command.class)) {
				Object object = clazz.newInstance();
				WauzCommandExecutor.registerCommand((WauzCommand) object);
			}
		}
		
	}
	
}

package eu.wauz.wauzcore.system.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.skills.WauzPlayerSkill;
import eu.wauz.wauzcore.skills.WauzPlayerSkillExecutor;

/**
 * An annotation to mark classes as skillgems, to automatically register them on startup.
 * 
 * @author Wauzmons
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Skillgem {
	
	/**
	 * An utility to help registering skillgems.
	 * 
	 * @author Wauzmons
	 */
	public static class SkillgemAnnotationHelper {
		
		/**
		 * Registers instances of all classes, annotated with this annotation.
		 * 
		 * @param loader The annotation loader to use.
		 * 
		 * @throws Exception Failed to load a class.
		 */
		public static void init(AnnotationLoader loader) throws Exception {
			int count = 0;
			for(Class<?> clazz : loader.getAnnotatedClasses(Skillgem.class)) {
				Object object = clazz.newInstance();
				WauzPlayerSkillExecutor.registerSkill((WauzPlayerSkill) object, true);
				count++;
			}
			WauzCore.getInstance().getLogger().info("Loaded " + count + " Skillgems!");
		}
		
	}
	
}

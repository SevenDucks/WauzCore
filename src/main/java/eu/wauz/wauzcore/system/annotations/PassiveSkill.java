package eu.wauz.wauzcore.system.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkill;
import eu.wauz.wauzcore.skills.passive.AbstractPassiveSkillPool;

/**
 * An annotation to mark classes as passive skills, to automatically register them on startup.
 * 
 * @author Wauzmons
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PassiveSkill {
	
	/**
	 * An utility to help registering passive skills.
	 * 
	 * @author Wauzmons
	 */
	public static class PassiveSkillAnnotationHelper {
		
		/**
		 * Registers instances of all classes, annotated with this annotation.
		 * 
		 * @param loader The annotation loader to use.
		 * 
		 * @throws Exception Failed to load a class.
		 */
		public static void init(AnnotationLoader loader) throws Exception {
			int count = 0;
			for(Class<?> clazz : loader.getAnnotatedClasses(PassiveSkill.class)) {
				Object object = clazz.newInstance();
				AbstractPassiveSkillPool.registerPassive((AbstractPassiveSkill) object);
				count++;
			}
			WauzCore.getInstance().getLogger().info("Loaded " + count + " Passives!");
		}
		
	}
	
}

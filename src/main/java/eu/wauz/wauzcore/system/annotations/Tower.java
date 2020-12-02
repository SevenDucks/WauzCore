package eu.wauz.wauzcore.system.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import eu.wauz.wauzcore.mobs.towers.DefenseTower;
import eu.wauz.wauzcore.mobs.towers.WauzTowers;

/**
 * An annotation to mark classes as towers, to automatically register them on startup.
 * 
 * @author Wauzmons
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Tower {
	
	/**
	 * An utility to help registering towers.
	 * 
	 * @author Wauzmons
	 */
	public static class TowerAnnotationHelper {
		
		/**
		 * Registers instances of all classes, annotated with this annotation.
		 * 
		 * @param loader The annotation loader to use.
		 * 
		 * @throws Exception Failed to load a class.
		 */
		public static void init(AnnotationLoader loader) throws Exception {
			for(Class<?> clazz : loader.getAnnotatedClasses(Tower.class)) {
				Object object = clazz.newInstance();
				WauzTowers.registerTower((DefenseTower) object);
			}
		}
		
	}
	
}

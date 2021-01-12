package eu.wauz.wauzcore.system.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.enhancements.WauzEnhancement;
import eu.wauz.wauzcore.items.enhancements.WauzEquipmentEnhancer;

/**
 * An annotation to mark classes as enhancements, to automatically register them on startup.
 * 
 * @author Wauzmons
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Enhancement {
	
	/**
	 * An utility to help registering enhancements.
	 * 
	 * @author Wauzmons
	 */
	public static class EnhancementAnnotationHelper {
		
		/**
		 * Registers instances of all classes, annotated with this annotation.
		 * 
		 * @param loader The annotation loader to use.
		 * 
		 * @throws Exception Failed to load a class.
		 */
		public static void init(AnnotationLoader loader) throws Exception {
			int count = 0;
			for(Class<?> clazz : loader.getAnnotatedClasses(Enhancement.class)) {
				Object object = clazz.newInstance();
				WauzEquipmentEnhancer.registerEnhancement((WauzEnhancement) object);
				count++;
			}
			WauzCore.getInstance().getLogger().info("Loaded " + count + " Enhancements!");
		}
		
	}
	
}

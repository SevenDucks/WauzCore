package eu.wauz.wauzcore.system.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.building.shapes.WauzBrush;
import eu.wauz.wauzcore.building.shapes.WauzBrushes;

/**
 * An annotation to mark classes as brushes, to automatically register them on startup.
 * 
 * @author Wauzmons
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Brush {
	
	/**
	 * An utility to help registering brushes.
	 * 
	 * @author Wauzmons
	 */
	public static class BrushAnnotationHelper {
		
		/**
		 * Registers instances of all classes, annotated with this annotation.
		 * 
		 * @param loader The annotation loader to use.
		 * 
		 * @throws Exception Failed to load a class.
		 */
		public static void init(AnnotationLoader loader) throws Exception {
			int count = 0;
			for(Class<?> clazz : loader.getAnnotatedClasses(Brush.class)) {
				Object object = clazz.getDeclaredConstructor().newInstance();
				WauzBrushes.registerBrush((WauzBrush) object);
				count++;
			}
			WauzCore.getInstance().getLogger().info("Loaded " + count + " Brushes!");
		}
		
	}
	
}

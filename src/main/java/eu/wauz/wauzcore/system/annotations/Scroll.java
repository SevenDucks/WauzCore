package eu.wauz.wauzcore.system.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.scrolls.InventoryScroll;
import eu.wauz.wauzcore.items.scrolls.WauzScrolls;

/**
 * An annotation to mark classes as scrolls, to automatically register them on startup.
 * 
 * @author Wauzmons
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scroll {
	
	/**
	 * An utility to help registering scrolls.
	 * 
	 * @author Wauzmons
	 */
	public static class ScrollAnnotationHelper {
		
		/**
		 * Registers instances of all classes, annotated with this annotation.
		 * 
		 * @param loader The annotation loader to use.
		 * 
		 * @throws Exception Failed to load a class.
		 */
		public static void init(AnnotationLoader loader) throws Exception {
			int count = 0;
			for(Class<?> clazz : loader.getAnnotatedClasses(Scroll.class)) {
				Object object = clazz.getDeclaredConstructor().newInstance();
				WauzScrolls.registerScroll((InventoryScroll) object);
				count++;
			}
			WauzCore.getInstance().getLogger().info("Loaded " + count + " Scrolls!");
		}
		
	}
	
}

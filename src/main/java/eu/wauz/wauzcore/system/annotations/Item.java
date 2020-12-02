package eu.wauz.wauzcore.system.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import eu.wauz.wauzcore.items.CustomItem;
import eu.wauz.wauzcore.system.EventMapper;

/**
 * An annotation to mark classes as items, to automatically register them on startup.
 * 
 * @author Wauzmons
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Item {
	
	/**
	 * An utility to help registering items.
	 * 
	 * @author Wauzmons
	 */
	public static class ItemAnnotationHelper {
		
		/**
		 * Registers instances of all classes, annotated with this annotation.
		 * 
		 * @param loader The annotation loader to use.
		 * 
		 * @throws Exception Failed to load a class.
		 */
		public static void init(AnnotationLoader loader) throws Exception {
			for(Class<?> clazz : loader.getAnnotatedClasses(Item.class)) {
				Object object = clazz.newInstance();
				EventMapper.registerCustomItem((CustomItem) object);
			}
		}
		
	}
	
}

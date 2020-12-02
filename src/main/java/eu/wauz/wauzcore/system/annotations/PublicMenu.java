package eu.wauz.wauzcore.system.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import eu.wauz.wauzcore.menu.util.MenuRegister;
import eu.wauz.wauzcore.menu.util.WauzInventory;

/**
 * An annotation to mark classes as public menus, to automatically register them on startup.
 * 
 * @author Wauzmons
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PublicMenu {
	
	/**
	 * An utility to help registering public menus.
	 * 
	 * @author Wauzmons
	 */
	public static class MenuAnnotationHelper {
		
		/**
		 * Registers instances of all classes, annotated with this annotation.
		 * 
		 * @param loader The annotation loader to use.
		 * 
		 * @throws Exception Failed to load a class.
		 */
		public static void init(AnnotationLoader loader) throws Exception {
			for(Class<?> clazz : loader.getAnnotatedClasses(PublicMenu.class)) {
				Object object = clazz.newInstance();
				MenuRegister.registerInventory((WauzInventory) object);
			}
		}
		
	}
	
}

package eu.wauz.wauzcore.system.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import eu.wauz.wauzcore.mobs.pets.WauzPetAbility;
import eu.wauz.wauzcore.mobs.pets.WauzPetAbilities;

/**
 * An annotation to mark classes as pet abilities, to automatically register them on startup.
 * 
 * @author Wauzmons
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PetAbility {
	
	/**
	 * An utility to help registering pet abilities.
	 * 
	 * @author Wauzmons
	 */
	public static class AbilityAnnotationHelper {
		
		/**
		 * Registers instances of all classes, annotated with this annotation.
		 * 
		 * @param loader The annotation loader to use.
		 * 
		 * @throws Exception Failed to load a class.
		 */
		public static void init(AnnotationLoader loader) throws Exception {
			for(Class<?> clazz : loader.getAnnotatedClasses(PetAbility.class)) {
				Object object = clazz.newInstance();
				WauzPetAbilities.registerAbility((WauzPetAbility) object);
			}
		}
		
	}
	
}

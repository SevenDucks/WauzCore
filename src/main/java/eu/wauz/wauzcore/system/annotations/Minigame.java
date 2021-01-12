package eu.wauz.wauzcore.system.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.arcade.ArcadeLobby;
import eu.wauz.wauzcore.arcade.ArcadeMinigame;

/**
 * An annotation to mark classes as minigames, to automatically register them on startup.
 * 
 * @author Wauzmons
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Minigame {
	
	/**
	 * An utility to help registering minigames.
	 * 
	 * @author Wauzmons
	 */
	public static class MinigameAnnotationHelper {
		
		/**
		 * Registers instances of all classes, annotated with this annotation.
		 * 
		 * @param loader The annotation loader to use.
		 * 
		 * @throws Exception Failed to load a class.
		 */
		public static void init(AnnotationLoader loader) throws Exception {
			int count = 0;
			for(Class<?> clazz : loader.getAnnotatedClasses(Minigame.class)) {
				Object object = clazz.newInstance();
				ArcadeLobby.registerMinigame((ArcadeMinigame) object);
				count++;
			}
			WauzCore.getInstance().getLogger().info("Loaded " + count + " Minigames!");
		}
		
	}
	
}

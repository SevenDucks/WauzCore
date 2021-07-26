package eu.wauz.wauzcore.system.annotations;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.reflections.Reflections;

/**
 * Used to load premade content through the use of annotations.
 * 
 * @author Wauzmons
 */
public class AnnotationLoader {
	
	/**
	 * A reflection instance for finding annotated classes.
	 */
	private Reflections reflections;
	
	/**
	 * Initializes and runs the loader for all annotations.
	 * Only called once per server run.
	 */
	public static void init() {
		try {
			new AnnotationLoader().run();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new instance of the loader.
	 */
	private AnnotationLoader() {
		reflections = new Reflections("eu.wauz.wauzcore");
	}
	
	/**
	 * Runs the loader.
	 * 
	 * @throws Exception Failed to load a class.
	 */
	private void run() throws Exception {
		PublicMenu.MenuAnnotationHelper.init(this);
		Command.CommandAnnotationHelper.init(this);
		CharacterClass.ClassAnnotationHelper.init(this);
		Item.ItemAnnotationHelper.init(this);
		Scroll.ScrollAnnotationHelper.init(this);
		Tower.TowerAnnotationHelper.init(this);
		PetAbility.AbilityAnnotationHelper.init(this);
		PassiveSkill.PassiveSkillAnnotationHelper.init(this);
		Skill.SkillAnnotationHelper.init(this);
		Skillgem.SkillgemAnnotationHelper.init(this);
		Rune.RuneAnnotationHelper.init(this);
		Enhancement.EnhancementAnnotationHelper.init(this);
		Minigame.MinigameAnnotationHelper.init(this);
		Brush.BrushAnnotationHelper.init(this);
	}
	
	/**
	 * Gets all classes annotated with the given annotation.
	 * 
	 * @param annotationClass The class of the annotation.
	 * 
	 * @return All matching classes.
	 */
	public Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotationClass) {
		return reflections.getTypesAnnotatedWith(annotationClass);
	}

}

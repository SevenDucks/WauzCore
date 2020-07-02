package eu.wauz.wauzcore.players.classes;

/**
 * The starting stats and passive skills of a class.
 * 
 * @author Wauzmons
 *
 * @see WauzPlayerClass
 */
public class WauzPlayerClassStats {
	
	/**
	 * The starting level of the sword skill.
	 */
	private int swordSkill = 100000;
	
	/**
	 * The maximum level of the sword skill.
	 */
	private int swordSkillMax = 200000;
	
	/**
	 * The starting level of the axe skill.
	 */
	private int axeSkill = 100000;
	
	/**
	 * The maximum level of the axe skill.
	 */
	private int axeSkillMax = 200000;
	
	/**
	 * The starting level of the staff skill.
	 */
	private int staffSkill = 100000;
	
	/**
	 * The maximum level of the staff skill.
	 */
	private int staffSkillMax = 200000;

	/**
	 * @return The starting level of the sword skill.
	 */
	public int getSwordSkill() {
		return swordSkill;
	}

	/**
	 * @param swordSkill The new starting level of the sword skill.
	 */
	public void setSwordSkill(int swordSkill) {
		this.swordSkill = swordSkill;
	}

	/**
	 * @return The maximum level of the sword skill.
	 */
	public int getSwordSkillMax() {
		return swordSkillMax;
	}

	/**
	 * @param swordSkillMax The new maximum level of the sword skill.
	 */
	public void setSwordSkillMax(int swordSkillMax) {
		this.swordSkillMax = swordSkillMax;
	}

	/**
	 * @return The starting level of the axe skill.
	 */
	public int getAxeSkill() {
		return axeSkill;
	}

	/**
	 * @param axeSkill The new starting level of the axe skill.
	 */
	public void setAxeSkill(int axeSkill) {
		this.axeSkill = axeSkill;
	}

	/**
	 * @return The maximum level of the axe skill.
	 */
	public int getAxeSkillMax() {
		return axeSkillMax;
	}

	/**
	 * @param axeSkillMax The new maximum level of the axe skill.
	 */
	public void setAxeSkillMax(int axeSkillMax) {
		this.axeSkillMax = axeSkillMax;
	}

	/**
	 * @return The starting level of the staff skill.
	 */
	public int getStaffSkill() {
		return staffSkill;
	}

	/**
	 * @param staffSkill The new starting level of the staff skill.
	 */
	public void setStaffSkill(int staffSkill) {
		this.staffSkill = staffSkill;
	}

	/**
	 * @return The maximum level of the staff skill.
	 */
	public int getStaffSkillMax() {
		return staffSkillMax;
	}

	/**
	 * @param staffSkillMax The new maximum level of the staff skill.
	 */
	public void setStaffSkillMax(int staffSkillMax) {
		this.staffSkillMax = staffSkillMax;
	}
	
}

package chalmers.dax021308.ecosystem.model.population;

/**
 * Common settings for all agents to use.
 * 
 * Sets and gets from CommonSettingsPanel.
 * 
 * @author Erik Ramqvist
 *
 */
public class CommonSettings {
	public static CommonSettings predSettings = new CommonSettings();
	public static CommonSettings preySettings = new CommonSettings();
	public static CommonSettings grassSettings = new CommonSettings();
	
	
	private int capacity;
	private int max_energy;
	private double visionRange;
	private double maxAcceleration;
	private double maxSpeed;
	private int width;
	private int height;
	private double interaction_range;
	private double eating_range;
	private double focus_range;
	private double velocity_decay;
	private boolean groupBehavior;
	
	public CommonSettings() {
	}
	public double getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getMax_energy() {
		return max_energy;
	}
	public void setMax_energy(int max_energy) {
		this.max_energy = max_energy;
	}
	public double getVisionRange() {
		return visionRange;
	}
	public void setVisionRange(double visionRange) {
		this.visionRange = visionRange;
	}
	public double getMaxAcceleration() {
		return maxAcceleration;
	}
	public void setMaxAcceleration(double maxAcceleration) {
		this.maxAcceleration = maxAcceleration;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public double getInteraction_range() {
		return interaction_range;
	}
	public void setInteraction_range(double interaction_range) {
		this.interaction_range = interaction_range;
	}
	public double getEating_range() {
		return eating_range;
	}
	public void setEating_range(double eating_range) {
		this.eating_range = eating_range;
	}
	public double getFocus_range() {
		return focus_range;
	}
	public void setFocus_range(double focus_range) {
		this.focus_range = focus_range;
	}
	public double getVelocity_decay() {
		return velocity_decay;
	}
	public void setVelocity_decay(double velocity_decay) {
		this.velocity_decay = velocity_decay;
	}
	public boolean isGroupBehavior() {
		return groupBehavior;
	}
	public void setGroupBehavior(boolean groupBehavior) {
		this.groupBehavior = groupBehavior;
	}
}

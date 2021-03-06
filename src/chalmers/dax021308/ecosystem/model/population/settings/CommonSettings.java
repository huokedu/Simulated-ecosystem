package chalmers.dax021308.ecosystem.model.population.settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Common settings for all agents to use.
 * 
 * Sets and gets from CommonSettingsPanel.
 * 
 * @author Erik Ramqvist
 *
 */
public abstract class CommonSettings {
	
	public class DoubleSettingsContainer {
		public DoubleSettingsContainer(String name, double min, double max, double defaultValue) {
			this.value = defaultValue;
			this.defaultValue = defaultValue;
			this.max = max;
			this.min = min;
			this.name = name;
		}
		public String name;
		public double value;
		public double max;
		public double min;
		public double defaultValue;
	}

	public class BooleanSettingsContainer {
		public BooleanSettingsContainer(String name, boolean defaultValue) {
			this.defaultValue = defaultValue;
			this.value = defaultValue;
			this.name = name;
		}
		public String name;
		public boolean value;
		public boolean defaultValue;
	}
	
	protected List<DoubleSettingsContainer> doubleSettings;
	protected List<BooleanSettingsContainer> booleanSettings;
	
	
	/*	Common Settings containers	*/
	public DoubleSettingsContainer capacity;
	public DoubleSettingsContainer max_energy;
	public DoubleSettingsContainer visionRange;
	public DoubleSettingsContainer maxAcceleration;
	public DoubleSettingsContainer maxSpeed;
	public DoubleSettingsContainer width;
	public DoubleSettingsContainer height;
	public DoubleSettingsContainer interaction_range;
	public DoubleSettingsContainer eating_range;
	public DoubleSettingsContainer focus_range;
	public DoubleSettingsContainer velocity_decay;
	public DoubleSettingsContainer obstacle_safety_distance;
	public DoubleSettingsContainer reproduction_rate;
	
	/* Common boolean containers. */
	protected BooleanSettingsContainer groupBehavior;
	protected BooleanSettingsContainer pathFinding;
	
	public CommonSettings() {
		doubleSettings = new ArrayList<DoubleSettingsContainer>();
		
		/* Initialize global default values (from AbstractAgent) */ 
		//TODO: Put only global here and remove population specific settings?
		capacity 					= new DoubleSettingsContainer("Capacity", 1, Integer.MAX_VALUE, Integer.MAX_VALUE);
		interaction_range			= new DoubleSettingsContainer("Interaction range", 1, 100, 10);
		eating_range				= new DoubleSettingsContainer("Eating range", 1, 100, 5);
		focus_range					= new DoubleSettingsContainer("Focus range", 1, 1000, 100);
		obstacle_safety_distance	= new DoubleSettingsContainer("Obstacle safety distance", 1, 100, 10);
		velocity_decay				= new DoubleSettingsContainer("Velocity decay", 1, 10, 1); //TODO: Add support for decimals. Only integers now.

		doubleSettings.add(interaction_range);
		doubleSettings.add(eating_range);
		doubleSettings.add(focus_range);
		doubleSettings.add(obstacle_safety_distance);
		doubleSettings.add(velocity_decay);

		booleanSettings = new ArrayList<BooleanSettingsContainer>();
		
		groupBehavior 	= new BooleanSettingsContainer("Group behavior", true);
		pathFinding 	= new BooleanSettingsContainer("Pathfinding", true);
		
		booleanSettings.add(groupBehavior);
		booleanSettings.add(pathFinding);
	}
	public List<DoubleSettingsContainer> getDoubleSettings() {
		return doubleSettings;
	}
	public void setDoubleSettings(List<DoubleSettingsContainer> doubleSettings) {
		this.doubleSettings = doubleSettings;
	}
	public List<BooleanSettingsContainer> getBooleanSettings() {
		return booleanSettings;
	}
	public void setBooleanSettings(
			List<BooleanSettingsContainer> booleanSettings) {
		this.booleanSettings = booleanSettings;
	}
	public double getMaxSpeed() {
		return maxSpeed.value;
	}
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed.value = maxSpeed;
	}
	public int getCapacity() {
		return (int) capacity.value;
	}
	public void setCapacity(int capacity) {
		this.capacity.value = capacity;
	}
	public int getMax_energy() {
		return (int) max_energy.value;
	}
	public void setMax_energy(int max_energy) {
		this.max_energy.value = max_energy;
	}
	public double getVisionRange() {
		return visionRange.value;
	}
	public void setVisionRange(double visionRange) {
		this.visionRange.value = visionRange;
	}
	public double getMaxAcceleration() {
		return maxAcceleration.value;
	}
	public void setMaxAcceleration(double maxAcceleration) {
		this.maxAcceleration.value = maxAcceleration;
	}
	public int getWidth() {
		return (int) width.value;
	}
	public void setWidth(int width) {
		this.width.value = width;
	}
	public int getHeight() {
		return (int) height.value;
	}
	public void setHeight(int height) {
		this.height.value = height;
	}
	public double getInteraction_range() {
		return interaction_range.value;
	}
	public void setInteraction_range(double interaction_range) {
		this.interaction_range.value = interaction_range;
	}
	public double getEating_range() {
		return eating_range.value;
	}
	public void setEating_range(double eating_range) {
		this.eating_range.value = eating_range;
	}
	public double getFocus_range() {
		return focus_range.value;
	}
	public void setFocus_range(double focus_range) {
		this.focus_range.value = focus_range;
	}
	public double getVelocity_decay() {
		return velocity_decay.value;
	}
	public void setVelocity_decay(double velocity_decay) {
		this.velocity_decay.value = velocity_decay;
	}
	public boolean isGroupBehavior() {
		return groupBehavior.value;
	}
	public void setGroupBehavior(boolean groupBehavior) {
		this.groupBehavior.value = groupBehavior;
	}
	
	public double getReproductionRate() {
		return reproduction_rate.value;
	}
	public void setHeight(double rate) {
		if(rate > 1) {
			this.reproduction_rate.value = 1;
		} else if(rate < 0) {
			this.reproduction_rate.value = 0;
		} else {
			this.reproduction_rate.value = rate;
		}
	}
}

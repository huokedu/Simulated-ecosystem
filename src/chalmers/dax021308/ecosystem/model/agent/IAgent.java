package chalmers.dax021308.ecosystem.model.agent;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import chalmers.dax021308.ecosystem.model.population.IPopulation;
import chalmers.dax021308.ecosystem.model.util.Gender;
import chalmers.dax021308.ecosystem.model.util.Vector;
import chalmers.dax021308.ecosystem.model.util.Position;


/**
 * IAgent describes an arbitrary single individual. 
 * @author Albin
 */
public interface IAgent extends Cloneable {
	
	/**
	 * @return The position of the IAgent.
	 */
	public Position getPosition();
	
	/**
	 * Updates the position of the IAgent.
	 */
	public void updatePosition(List<IPopulation> predators, List<IPopulation> preys, List<IPopulation> neutral, Dimension dim);
	
	/**
	 * @return The name of the IAgent.
	 */
	public String getName();
	
	/**
	 * @return The color of the IAgent.
	 */
	public Color getColor();
	
	/**
	 * @return The width of the IAgent.
	 */
	public int getWidth();
	
	/**
	 * @return The height of the IAgent.
	 */
	public int getHeight();
	
	/**
	 * @return The fitness of the IAgent.
	 */
	public double getFitness();
	
	/**
	 * @param fitness - The new fitness.
	 */
	public void setFitness(double fitness);
	
	/**
	 * @return The velocity of the IAgent.
	 */
	public Vector getVelocity();
	
	/**
	 * @param speed - The new velocity.
	 */
	public void setVelocity(Vector velocity);
	
	/**
	 * Tries to create one or more new IAgents, with data from the two provided IAgents.
	 * @param agent - The IAgent which this IAgent will reproduce with.
	 * @return A list of IAgents if the reproduction was successful, otherwise null.
	 */
	public List<IAgent> reproduce(IAgent agent);
	
	/**
	 * @return The gender of the IAgent specified by some enum. Returns null if genderless.
	 */
	public Gender getGender();

	/**
	 * Clone an agent. 
	 * <p>
	 * Returns a reference to a new agent with the values of the calling one. 
	 * @throws CloneNotSupportedException 
	 */
	public IAgent cloneAgent() throws CloneNotSupportedException;
}

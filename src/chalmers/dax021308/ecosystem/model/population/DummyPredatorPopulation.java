package chalmers.dax021308.ecosystem.model.population;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import chalmers.dax021308.ecosystem.model.agent.IAgent;
import chalmers.dax021308.ecosystem.model.agent.SimplePredatorAgent;
import chalmers.dax021308.ecosystem.model.environment.SurroundingsSettings;
import chalmers.dax021308.ecosystem.model.util.Position;
import chalmers.dax021308.ecosystem.model.util.Vector;

/**
 * 
 * @author Henrik
 * 
 */
public class DummyPredatorPopulation extends AbstractPopulation {
	
	
	private double maxSpeed;
	private double visionRange;
	
	public DummyPredatorPopulation(int initPopulationSize, Color color, double maxSpeed,
			double maxAcceleration, double visionRange, SurroundingsSettings surroundings) {
		super("Simple Predator", color, surroundings);
		this.visionRange = visionRange;
		agents = initializePopulation(initPopulationSize, surroundings.getGridDimension(), color,
				maxSpeed, maxAcceleration, visionRange);
	}

	// TODO (surroundings) why does this take a gridDimension?
	private List<IAgent> initializePopulation(int populationSize,
			Dimension gridDimension, Color color, double maxSpeed,
			double maxAcceleration, double visionRange) {
		List<IAgent> newAgents = new ArrayList<IAgent>(populationSize);
		addNeutralPopulation(this);
		for (int i = 0; i < populationSize; i++) {
			Position randPos = this.surroundings.getWorldShape().getRandomPosition(gridDimension);
			Vector velocity = new Vector(maxSpeed, maxSpeed);
			
			//Create a random vector (uniformly) inside a circle with radius maxSpeed.
			while (velocity.getNorm() > maxSpeed) {
				velocity.setVector(-maxSpeed + Math.random() * 2 * maxSpeed,
						-maxSpeed + Math.random() * 2 * maxSpeed);
			}
			IAgent a = new SimplePredatorAgent("Big Hungry", randPos, color, 10,
					20, velocity, maxSpeed, maxAcceleration, visionRange);
			newAgents.add(a);
		}
		return newAgents;
	}
}

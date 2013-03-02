package chalmers.dax021308.ecosystem.model.agent;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Random;

import chalmers.dax021308.ecosystem.model.population.IPopulation;
import chalmers.dax021308.ecosystem.model.util.Log;
import chalmers.dax021308.ecosystem.model.util.Position;
import chalmers.dax021308.ecosystem.model.util.Vector;

/**
 * The pig is wide and pink, nuff said.
 * 
 * @author Erik
 *
 */
public class PigAgent extends AbstractAgent {

	private Position reUsedPosition;
	private Random ran;
	private double wanderVelocity = 0.3;
	private boolean hungry;

	public PigAgent(String name, Position p, Color c, int width, int height,Vector velocity, double maxSpeed, double visionRange,double maxAcceleration, Random ran) {
		super(name, p, c, width, height, velocity, maxSpeed, visionRange, maxAcceleration);
		this.ran = ran;
	}
/*
	@Override
	public void calculateNextPosition(List<IPopulation> predators, List<IPopulation> preys, List<IPopulation> neutral, Dimension dim) {
		/*if(reUsedPosition == null) {
			reUsedPosition = new Position(position);
		}
		nextPosition = reUsedPosition;
		int predatorsSize = predators.size();
		List<IAgent> predatorPop;
		boolean hasNearbyPredator = false;
		for(int i = 0 ; i < predatorsSize ; i++) {
			predatorPop = predators.get(i).getAgents();
			int predatorPopSize = predatorPop.size();
			IAgent pred;
			Position p;
			for(int j = 0 ; j < predatorPopSize; j++) {
				pred = predatorPop.get(j);
				p = pred.getPosition();
				double distance = p.getDistance(position);
				if(distance < visionRange) {
					if(p.getX() > position.getX()) {
						velocity.x = -maxAcceleration;
					} else {
						velocity.x = maxAcceleration;						
					}
					if(p.getY() > position.getX()) {
						velocity.y = -maxAcceleration;	
					} else {
						velocity.y = maxAcceleration;						
					}
					hasNearbyPredator = true;
				}
			}
		}
		if(!hasNearbyPredator) {
			if(ran.nextBoolean()) {
				velocity.x = wanderVelocity;
				velocity.y = -wanderVelocity;
			} else if(ran.nextBoolean())  {
				velocity.x = -wanderVelocity;
				velocity.y = wanderVelocity;
			}
		}

		Vector separationForce = getSeparationForce(neutral);
		velocity.x = velocity.x * 1000 + separationForce.x * 1000 + ran.nextDouble() * 10;
		velocity.y = velocity.y * 1000 + separationForce.y * 1000 + ran.nextDouble() * 10;
		Log.v(velocity.toString());
		nextPosition = new Position(position.getX() + separationForce.x, position.getY() + separationForce.x);
		Log.v(nextPosition.toString());
	}*/

	@Override
	public void updatePosition() {
		this.reUsedPosition = position;
		this.position = nextPosition;
	}
	
	/**
	 * @author Sebbe
	 */
	@Override
	public void calculateNextPosition(List<IPopulation> predators,
			List<IPopulation> preys, List<IPopulation> neutral,
			Dimension gridDimension) {

		Vector predatorForce = getPredatorForce(predators);
		Vector separationForce = getSeparationForce(neutral);
		// Vector separationForce = new Vector();
		Vector environmentForce = getEnvironmentForce(gridDimension);
		Vector preyForce = getPreyForce(preys);

		/*
		 * Sum the forces from walls, predators and neutral to form the
		 * acceleration force. If the acceleration exceeds maximum acceleration
		 * --> scale it to maxAcceleration, but keep the correct direction of
		 * the acceleration.
		 */
		Vector acceleration = environmentForce.multiply(100)
				.add(predatorForce.multiply(3))
				.add(separationForce.multiply(10).add(preyForce));
		double accelerationNorm = acceleration.getNorm();
		if (accelerationNorm > maxAcceleration) {
			acceleration.multiply(maxAcceleration / accelerationNorm);
		}

		/*
		 * The new velocity is then just: v(t+dt) = (v(t)+a(t+1)*dt)*decay,
		 * where dt = 1 in this case. There is a decay that says if they are not
		 * affected by any force, they will eventually stop. If speed exceeds
		 * maxSpeed --> scale it to maxSpeed, but keep the correct direction.
		 */
		Vector newVelocity = this.getVelocity().add(acceleration)
				.multiply(VELOCITY_DECAY);
		double speed = newVelocity.getNorm();
		if (speed > maxSpeed) {
			newVelocity.multiply(maxSpeed / speed);
		}

		this.setVelocity(newVelocity);
		if(reUsedPosition == null) { 
			reUsedPosition = Position.positionPlusVector(position, velocity);
			nextPosition = reUsedPosition;
		} else {
			reUsedPosition.setPosition(position.getX() + velocity.x, position.getY() + velocity.y);
			nextPosition = reUsedPosition;
		}
	}

	/**
	 * @author Henrik
	 * @param preys
	 *            The list of preys to eat
	 * @return returns The force the preys attracts the agent with
	 * 
	 * @author Erik, Method optimized for ArrayList  
	 */
	private Vector getPreyForce(List<IPopulation> preys) {
		Vector preyForce = new Vector(0, 0);
		IPopulation pop;
		int preySize = preys.size();
		for (int j = 0; j < preySize; j++) {
			pop = preys.get(j);
			List<IAgent> agents = pop.getAgents();
			int agentSize = agents.size();
			for (int i = 0; i < agentSize; i++) {
				IAgent a = agents.get(i);
				Position p = a.getPosition();
				double distance = getPosition().getDistance(p);
				if (distance <= visionRange) {
					if (distance <= INTERACTION_RANGE) {
						// Food found, let's eat it and make some reproducing
						// possible
						agents.remove(i);
						i--;
						agentSize--;
						hungry = false;
					} else {
						Vector newForce = new Vector(p, getPosition());
						double norm = newForce.getNorm();
						preyForce.add(newForce.multiply(1 / (norm * distance)));
					}
				}
			}
		}
		return preyForce;
	}

	/**
	 * @author Sebbe "Predator Force" is defined as the sum of the vectors
	 *         pointing away from all the predators in vision, weighted by the
	 *         inverse of the distance to the predators, then normalized to have
	 *         unit norm. Can be interpreted as the average sum of forces that
	 *         the agent feels, weighted by how close the source of the force
	 *         is.
	 *         
	 * @author Erik, Method optimized for ArrayList        
	 */
	private Vector getPredatorForce(List<IPopulation> predators) {
		Vector predatorForce = new Vector(0, 0);
		int nVisiblePredators = 0;
		IPopulation pop;
		int popSize = predators.size();
		for (int i = 0; i < popSize ; i++) {
			pop = predators.get(i);
			List<IAgent> agents = pop.getAgents();
			int agentSize = agents.size();
			IAgent predator;
			for (int j = 0; j < agentSize; j++ ) {
				predator = agents.get(j);
				Position p = predator.getPosition();
				double distance = getPosition().getDistance(p);
				if (distance <= visionRange) { // If predator is in vision range
												// for prey
					/*
					 * Create a vector that points away from the predator.
					 */
					Vector newForce = new Vector(this.getPosition(), p);

					/*
					 * Add this vector to the predator force, with proportion to
					 * how close the predator is. Closer predators will affect
					 * the force more than those far away.
					 */
					double norm = newForce.getNorm();
					predatorForce.add(newForce.multiply(1 / (norm * distance)));
					nVisiblePredators++;
				}
			}
		}

		if (nVisiblePredators == 0) { // No predators near --> Be unaffected
			predatorForce.setVector(0, 0);
		} else { // Else set the force depending on visible predators and
					// normalize it to maxAcceleration.
			double norm = predatorForce.getNorm();
			predatorForce.multiply(maxAcceleration / norm);
		}

		return predatorForce;
	}

	@Override
	public List<IAgent> reproduce(IAgent agent, int populationSize) {
		return null;
	}

	public IAgent reproduceOne(int populationSize) {
		return null;
	}

}

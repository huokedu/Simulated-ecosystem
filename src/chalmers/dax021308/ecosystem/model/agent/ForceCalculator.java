package chalmers.dax021308.ecosystem.model.agent;

import java.awt.Dimension;
import java.util.List;

import chalmers.dax021308.ecosystem.model.environment.obstacle.IObstacle;
import chalmers.dax021308.ecosystem.model.util.IShape;
import chalmers.dax021308.ecosystem.model.util.Position;
import chalmers.dax021308.ecosystem.model.util.Vector;

/**
 * A class made for containing the methods for the forces which need to be
 * calculated for the agents
 * 
 * @author Henrik
 * 
 */
public class ForceCalculator {
	protected static final double RANDOM_FORCE_MAGNITUDE = 0.05;
	protected final static double INTERACTION_RANGE = 10;
	protected final static double ENVIRONMENT_CONSTANT = 50;
	protected final static double OBSTACLE_CONSTANT = 50;

	/**
	 * A random force that the agent gets influenced by. Can be interpreted as
	 * an estimation error that the agent does in where to head.
	 * 
	 * @return A vector pointing approximately in the same direction as the
	 *         agents velocity.
	 * @author Sebbe
	 * @param velocity
	 */
	protected Vector randomForce(Vector velocity) {
		double randX = -RANDOM_FORCE_MAGNITUDE + 2 * RANDOM_FORCE_MAGNITUDE
				* Math.random();
		double randY = -RANDOM_FORCE_MAGNITUDE + 2 * RANDOM_FORCE_MAGNITUDE
				* Math.random();
		return new Vector(velocity.x + randX, velocity.y + randY);
	}

	/**
	 * The agent is influences by the mutual interaction force because it is
	 * subject to attraction and repulsion from other individuals that it wants
	 * to group with. This force describes the relationship between the tendency
	 * to steer towards other groups of agents, but not be to close to them
	 * either.
	 * 
	 * @param neutral
	 *            - The population of neutral agents.
	 * @return A vector with the force that this agent feels from other neutral
	 *         agents in that it interacts with.
	 * @author Sebbe
	 */
	protected Vector mutualInteractionForce(List<IAgent> neutralNeighbours,
			Position pos) {
		Vector mutualInteractionForce = new Vector(0, 0);
		Vector newForce = new Vector(0, 0);
		IAgent agent;
		int size = neutralNeighbours.size();
		for (int i = 0; i < size; i++) {
			agent = neutralNeighbours.get(i);
			if (agent != this) {
				Position p = agent.getPosition();
				double distance = pos.getDistance(p);
				double Q = 0; // Q is a function of the distance.
				if (distance <= INTERACTION_RANGE) {
					Q = -20 * (INTERACTION_RANGE - distance);
				} else {
					Q = 1;
				}
				newForce.x = p.getX() - pos.getX();
				newForce.y = p.getY() - pos.getY();
				double norm = newForce.getNorm();
				double v = Q / (norm * distance);
				newForce.x = newForce.x * v;
				newForce.y = newForce.y * v;
				mutualInteractionForce.x = (mutualInteractionForce.x + newForce.x);
				mutualInteractionForce.y = (mutualInteractionForce.y + newForce.y);
			}
		}
		return mutualInteractionForce;
	}

	/**
	 * The environment force is at the moment defined as
	 * 1/((wall-constant)*(distance to wall))^2. The agents feel the forces from
	 * the wall directly to the left, right, top and bottom.
	 * 
	 * @param dim
	 *            - The dimensions of the rectangular environment.
	 * @return A vector with the force that an agent feel from its environment.
	 * @author Sebbe
	 */
	protected Vector getEnvironmentForce(Dimension dim, IShape shape,
			Position position) {
		/*
		 * The positions below is just an orthogonal projection on to the walls.
		 */
		Position xWallLeft = shape.getXWallLeft(dim, position);
		Position xWallRight = shape.getXWallRight(dim, position);
		Position yWallBottom = shape.getYWallBottom(dim, position);
		Position yWallTop = shape.getYWallTop(dim, position);

		/*
		 * There is a "-1" in the equation just to make it more unlikely that
		 * they actually make it to the wall, despite the force they feel (can
		 * be interpreted as they stop 1 pixel before the wall).
		 */
		Vector environmentForce = new Vector(0, 0);
		double xWallLeftForce = 0;
		double xWallRightForce = 0;
		double yWallBottomForce = 0;
		double yWallTopForce = 0;

		/*
		 * Only interacts with walls that are closer than INTERACTION_RANGE.
		 */
		double leftWallDistance = position.getDistance(xWallLeft);
		if (leftWallDistance <= INTERACTION_RANGE) {
			xWallLeftForce = 1 / (leftWallDistance * leftWallDistance);
		}

		double rightWallDistance = position.getDistance(xWallRight);
		if (rightWallDistance <= INTERACTION_RANGE) {
			xWallRightForce = -1 / (rightWallDistance * rightWallDistance);
		}

		double bottomWallDistance = position.getDistance(yWallBottom);
		if (bottomWallDistance <= INTERACTION_RANGE) {
			yWallBottomForce = 1 / (bottomWallDistance * bottomWallDistance);
		}

		double topWallDistance = position.getDistance(yWallTop);
		if (topWallDistance <= INTERACTION_RANGE) {
			yWallBottomForce = yWallTopForce = -1
					/ (topWallDistance * topWallDistance);
		}

		/*
		 * Add the forces from left and right to form the total force from walls
		 * in x-axis. Add the forces from top and bottom to form the total force
		 * from walls in y-axis. Create a force vector of the forces.
		 */
		double xForce = (xWallLeftForce + xWallRightForce);
		double yForce = (yWallBottomForce + yWallTopForce);
		environmentForce.setVector(xForce, yForce);

		return environmentForce.multiply(ENVIRONMENT_CONSTANT);
	}

	/**
	 * The tendency of an agent to continue moving forward with its velocity.
	 * 
	 * @param velocity
	 *            TODO
	 * 
	 * @return The forward thrust force.
	 * @author Sebbe
	 */
	protected Vector forwardThrust(Vector velocity) {
		double a = 0.1; // Scaling constant
		double x = velocity.x;
		double y = velocity.y;
		double norm = velocity.getNorm();
		Vector forwardThrust = new Vector(a * x / norm, a * y / norm);
		return forwardThrust;
	}

	/**
	 * This is the force that makes neighbouring agents to equalize their
	 * velocities and therefore go in the same direction. The sphere of
	 * incluence is defined as 2*INTERACTION_RANGE at the moment.
	 * 
	 * @param velocity
	 *            TODO
	 * @param neutralNeighbours
	 *            TODO
	 * @param position
	 *            TODO
	 * @param neutral
	 *            - The population of neutral agents.
	 * @return a vector with the force influencing the agents to steer in the
	 *         same direction as other nearby agents.
	 * @author Sebbe
	 */
	protected Vector arrayalForce(Vector velocity,
			List<IAgent> neutralNeighbours, Position position) {
		Vector arrayalForce = new Vector(0, 0);
		Vector newForce = new Vector();
		double nAgentsInVision = 0;
		int size = neutralNeighbours.size();
		IAgent agent;
		for (int i = 0; i < size; i++) {
			agent = neutralNeighbours.get(i);
			if (agent != this) {
				Position p = agent.getPosition();
				double distance = position.getDistance(p);
				if (distance <= INTERACTION_RANGE * 2) {
					newForce.setVector(0, 0);
					newForce.add(agent.getVelocity());
					newForce.add(velocity);
					double h = 4; // Scaling constant
					newForce.x *= h;
					newForce.y *= h;
					arrayalForce.x = (arrayalForce.x + newForce.x);
					arrayalForce.y = (arrayalForce.y + newForce.y);
					nAgentsInVision = nAgentsInVision + 1.0;
				}
			}
		}
		if (nAgentsInVision > 0) {
			arrayalForce.x /= nAgentsInVision;
			arrayalForce.y /= nAgentsInVision;
		}
		return arrayalForce;
	}

	protected Vector getObstacleForce(List<IObstacle> obstacles,
			Position position) {
		Vector obstacleForce = new Vector();
		for (IObstacle o : obstacles) {
			if (o.isCloseTo(position, INTERACTION_RANGE)) {
				Position obstaclePos = o.closestBoundary(position);
				double distance = position.getDistance(obstaclePos);
				if (distance <= INTERACTION_RANGE) {
					Vector singleForce = new Vector(position, obstaclePos);
					singleForce.toUnitVector().multiply(
							1 / (distance * distance));
					obstacleForce.add(singleForce);
				}
			}
		}
		return obstacleForce.multiply(OBSTACLE_CONSTANT);
	}
}

package chalmers.dax021308.ecosystem.model.environment.obstacle;

import java.awt.Color;
import java.util.List;

import chalmers.dax021308.ecosystem.model.environment.SimulationSettings;
import chalmers.dax021308.ecosystem.model.util.Position;
import chalmers.dax021308.ecosystem.model.util.Vector;

/**
 * Abstract class for obstacles
 * 
 * @author Sebastian Anerud, Erik Ramqvist
 *
 */
public abstract class AbstractObstacle implements IObstacle {
	
	protected Position position;
	protected double width;
	protected double height;
	protected Color color;
	
	@Override
	/**
	 * returns the width of the obstacle.
	 */
	public double getWidth() {
		return width;
	}
	@Override
	/**
	 * returns the height of the obstacle.
	 */
	public double getHeight() {
		return height;
	}
	
	@Override
	/**
	 * returns the position of the obstacle
	 */
	public Position getPosition() {
		return new Position(position);
	}
	
	@Override
	/**
	 * Moves the obstacle +x and +y distance.
	 */
	public void moveObstacle(double x, double y) {
		this.position = new Position(position.getX() + x, position.getY() + y);
	}
	
	@Override
	/**
	 * Sets the position of the obstacle to (x,y).
	 */
	public void setPosition(Position p) {
		this.position = p;
	}
	
	@Override
	/**
	 * returns the color of the obstacle
	 */
	public Color getColor(){
		return this.color;
	}
	
	/**
	 * Sets the color of the obstacle
	 */
	public void setColor(Color c) {
		this.color = c;
	}
	
	/**
	 * Scale the obstacle to a specific scalar value.
	 */
	@Override
	public IObstacle scale(double scaleX, double scaleY) {
		position.setX(position.getX() * scaleX);
		position.setY(position.getY() * scaleY);
		width = width * scaleX;
		height = height * scaleY;
		return this;
	}
	
	/**
	 * Used to export an obstacle.
	 */
	public String toBinaryString() {
		StringBuilder sb = new StringBuilder();
		if(this instanceof EllipticalObstacle) {
		sb.append(SimulationSettings.OBSTACLE_ELLIPTICAL); //TODO: Needs shape here.
		} else if(this instanceof RectangularObstacle) {
			sb.append(SimulationSettings.OBSTACLE_RECTANGULAR); //TODO: Needs shape here.
		} else if(this instanceof TriangleObstacle) {
			sb.append(SimulationSettings.OBSTACLE_TRIANGLE);
		} else {
			sb.append(SimulationSettings.OBSTACLE_NONE);
			return sb.toString();
		}
		sb.append(';');
		sb.append(roundTwoDecimals(position.getX()));
		sb.append(';');
		sb.append(roundTwoDecimals(position.getY()));
		sb.append(';');
		sb.append(width);
		sb.append(';');
		sb.append(height);
		sb.append(';');
		sb.append(color.getRed());
		sb.append(';');
		sb.append(color.getGreen());
		sb.append(';');
		sb.append(color.getBlue());
		return sb.toString();
	}
	
	/**
	 * Used to import an obstacle.
	 * @param input
	 * @return
	 */
	public static AbstractObstacle createFromFile(String input) {
		String[] inputArray = input.split(";");
		String shape = inputArray[0];
		AbstractObstacle obs = null;
		if (shape.equals(SimulationSettings.OBSTACLE_RECTANGULAR)) {
			obs = new RectangularObstacle(Double.parseDouble(inputArray[3]), Double.parseDouble(inputArray[4]),
					new Position( Double.parseDouble(inputArray[1]),  Double.parseDouble(inputArray[2])),
					new Color(Integer.parseInt(inputArray[5]),Integer.parseInt(inputArray[6]), Integer.parseInt(inputArray[7])));
		} else if (shape.equals(SimulationSettings.OBSTACLE_ELLIPTICAL)) {
			obs = new EllipticalObstacle(Double.parseDouble(inputArray[3]), Double.parseDouble(inputArray[4]),
					new Position( Double.parseDouble(inputArray[1]),  Double.parseDouble(inputArray[2])),
					new Color(Integer.parseInt(inputArray[5]),Integer.parseInt(inputArray[6]), Integer.parseInt(inputArray[7])));
		} else if (shape.equals(SimulationSettings.OBSTACLE_TRIANGLE)) {
			obs = new TriangleObstacle(Double.parseDouble(inputArray[3]), Double.parseDouble(inputArray[4]),
					new Position( Double.parseDouble(inputArray[1]),  Double.parseDouble(inputArray[2])),
					new Color(Integer.parseInt(inputArray[5]),Integer.parseInt(inputArray[6]), Integer.parseInt(inputArray[7])));
		}  else if (shape.equals(SimulationSettings.OBSTACLE_NONE)) {
			return null;
		}
		return obs;
	}
	
	/**
	 * Rounds a double to two decimals.
	 * @param num
	 * @return
	 */
	public static double roundTwoDecimals(double num) {
		double result = num * 100;
		result = Math.round(result);
		result = result / 100;
		return result;
	}
	
	/**
	 * checks if a position is inside the list of obstacles.
	 * @param obsList The list of obstacles.
	 * @param p the position.
	 * @return true if the position is inside any of the obstacles. 
	 * False if the position is not inside any of the obstacles.
	 */
	public static boolean isInsideObstacleList(List<IObstacle> obsList, Position p) {
		if(obsList == null) {
			return false;
		}
		for(IObstacle o : obsList) {
			if(o.isInObstacle(p)) {
				return true;
			}
		}
		return false;
	}
	
//	@Override
//	public boolean isInsidePath(Position start, Position end) {
//		Position current = new Position(start);
//		double path_threshold = 5.0;
//		while(current.getDistance(end) >= path_threshold ) {
//			if(isInObstacle(current)) {
//				return true;
//			}
//			if(end.getX() > current.getX()) {
//				current.setX(current.getX() + path_threshold);
//			} else if(end.getX() < current.getX()) {
//				current.setX(current.getX() - path_threshold);
//			}
//			if(end.getY() > current.getY()) {
//				current.setY(current.getY() + path_threshold);
//			} else if(end.getY() < current.getY()) {
//				current.setY(current.getY() - path_threshold);
//			}
//		}
//		return false;
//	}
	
	/**
	 * Checks of a linear path is cutting the obstacle at any point.
	 * @return true if the path cuts the obstacle. False otherwise.
	 */
	public boolean isInsidePath(Position start, Position end) {
		Position current = new Position(start);
		double path_threshold = 5.0;
		while(current.getDistance(end) >= path_threshold ) {
			if(isInObstacle(current)) {
				return true;
			}
			if(end.getX() > current.getX()) {
				current.setX(current.getX() + path_threshold);
			} else if(end.getX() < current.getX()) {
				current.setX(current.getX() - path_threshold);
			}
			if(end.getY() > current.getY()) {
				current.setY(current.getY() + path_threshold);
			} else if(end.getY() < current.getY()) {
				current.setY(current.getY() - path_threshold);
			}
		}
		return false;
	}
	
//	public static boolean isInsidePathList(List<IObstacle> obsList, Position start, Position end) {
//		for(IObstacle o : obsList) {
//			if(o.isInsidePath(start, end)) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	/**
	 * Checks if a path is inside a list of obstacles.
	 */
	public static boolean isInsidePathList(List<IObstacle> obsList, Position start, Position end) {
		
		double stepConstant = 5;
		double dirX = end.getX() - start.getX();
		double dirY = end.getY() - start.getY();
		double distance = Math.sqrt(dirX*dirX + dirY*dirY);
		dirX /= distance;
		dirY /= distance;
		int nIterations = (int) (distance/stepConstant);
		
		Position currentPos = new Position(start);
		double currentX = currentPos.getX();
		double currentY = currentPos.getY();
		
		for(int i=0;i<nIterations;i++){
			currentX += dirX;
			currentY += dirY;
			currentPos.setPosition(currentX, currentY);
			for(IObstacle o : obsList) {
				if(o.isInObstacle(currentPos)) {
					System.out.println("Completed in: " + i + " iterations.");
					return true;
				}
			}
			
		}
		
		return false;
	}

}
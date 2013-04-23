package chalmers.dax021308.ecosystem.model.environment.obstacle;

import java.awt.Color;

import chalmers.dax021308.ecosystem.model.util.Position;

public class RectangularObstacle extends AbstractObstacle implements IObstacle{
	
	private static double nStep = 200;

	public RectangularObstacle(double width, double height, Position position, Color color){
		this.position = position;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	
	@Override
	public Position closestBoundary(Position p) {
		Position agentPos = new Position(p.getX()-this.position.getX(), p.getY()-this.position.getY());	
		
		double xSign = Math.signum(agentPos.getX());
		double ySign = Math.signum(agentPos.getY());
		agentPos.setPosition(agentPos.getX()*xSign, agentPos.getY()*ySign);
		
		double x = agentPos.getX();
		double y = agentPos.getY();
		Position bestPos;
		
		if(x < width) {
			bestPos = new Position(x,height);
		} else if (y < height) {
			bestPos = new Position(width,y);
		} else {
			bestPos = new Position(width,height);
		}
		
		bestPos.setPosition(bestPos.getX()*xSign, bestPos.getY()*ySign);
		bestPos.setPosition(bestPos.getX()+this.position.getX(), bestPos.getY()+this.position.getY());
		
		return bestPos;
	}

	@Override
	public boolean isInObstacle(Position p) {
		if(p.getY() < position.getY()+height && p.getY() > position.getY()-height){
			if(p.getX() < position.getX()+width && p.getX() > position.getX()-width) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isCloseTo(Position p, double interactionRange){
		double radius = Math.sqrt(width*width + height*height);
		if(this.position.getDistance(p) <= radius + interactionRange) {
			return true;
		}
		return false;
	}

	@Override
	public Color getColor() {
		return color;
	}
	

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Rectangular Width: ");
		sb.append(width);
		sb.append(" Height: ");
		sb.append(height);
		sb.append(' ');
		return sb.toString();
	}
}
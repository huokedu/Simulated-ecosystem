package chalmers.dax021308.ecosystem.model.environment.obstacle;

import java.awt.Color;

import chalmers.dax021308.ecosystem.model.util.Position;

public class TriangleObstacle extends AbstractObstacle {
	
	public TriangleObstacle(double width, double height, Position position, Color color){
		this.position = position;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	@Override
	public Position closestBoundary(Position p) {
		Position agentPos = new Position(p.getX()-this.position.getX(), p.getY()-this.position.getY());
		
		double xSign = Math.signum(agentPos.getX());
		agentPos.setPosition(agentPos.getX()*xSign, agentPos.getY());
		
		double x = agentPos.getX();
		double y = agentPos.getY();
		
		Position bestPos = new Position();
		if(y <= -height){
			if(x >= width){
				bestPos = new Position(width, -height);
			} else {
				bestPos = new Position(agentPos.getX(), -height);
			}
			
			bestPos.setPosition(bestPos.getX()*xSign, bestPos.getY());
			return bestPos.setPosition(bestPos.getX()+this.position.getX(), bestPos.getY()+this.position.getY());
		}
		
		double yLimHigh = height + x*width/(2*height);
		double yLimLow = -height - width*width/(2*height) + x*width/(2*height);
		
		if(y >= yLimHigh) {
			bestPos = new Position(0,height);
		} else if(y <= yLimLow){
			bestPos = new Position(width,-height);
		} else {
			double k = 2*height/width;
			double t = (k*x + y - height)/(k*k +1);
			double newX = x - t*k;
			double newY = y-t;
			bestPos = new Position(newX, newY);
		}
		
		bestPos.setPosition(bestPos.getX()*xSign, bestPos.getY());
		return bestPos.setPosition(bestPos.getX()+this.position.getX(), bestPos.getY()+this.position.getY());
	}

	@Override
	public boolean isInObstacle(Position p) {
		Position agentPos = new Position(p.getX()-this.position.getX(), p.getY()-this.position.getY());
		
		double xSign = Math.signum(agentPos.getX());
		agentPos.setPosition(agentPos.getX()*xSign, agentPos.getY());
		
		double x = agentPos.getX();
		double y = agentPos.getY();
		
		if(y > height || y < -height){
			return false;
		}
		
		
		if(x > width) {
			return false;
		} 
		
		double yVal = height - 2*height/width*x;
		if(y > yVal){
			return false;
		}
		
		return true;
	}

	@Override
	public boolean isCloseTo(Position p, double interactionRange) {
		return true;
	}


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("TriangularObstacle Width: ");
		sb.append(width);
		sb.append(" Height: ");
		sb.append(height);
		sb.append(' ');
		return sb.toString();
	}
}
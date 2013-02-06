package discreteLogisticChaosModel;

import java.util.ArrayList;
import java.util.List;

import org.jfree.ui.RefineryUtilities;

import daisy.DaisyLineFrame;

public class DiscreteLogisticChaosModel implements Runnable{
	
	private double u0;
	private double r;
	private List<Double> u;
	private int nIterations;
	
	public DiscreteLogisticChaosModel(double u0, double r, int nIterations) {
		if(u0>=1 || u0<=0) {
			u0 = 0.5;
		} else {
			this.u0 = u0;
		}
		this.r = r;
		u = new ArrayList<Double>();
		u.add(this.u0);
		this.nIterations = nIterations;
	}

	
	@Override
	public void run() {
		for(int i=0;i<nIterations;i++) {
			double lastU = u.get(u.size()-1);
			double newU = lastU*Math.exp(r*(1-lastU));
			u.add(newU);
			System.out.println(newU);
		}
		final DiscreteLineFrame demo = new DiscreteLineFrame("Line Chart Demo 6", u);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
	}
}
package pl.edu.agh.miss;

import net.sourceforge.jswarm_pso.FitnessFunction;

public class RastriginFunction extends FitnessFunction {

	public RastriginFunction() {
		super(false); // Minimize this function
	}
	
	@Override
	public double evaluate(double[] position) {
		double x1 = position[0];
		double x2 = position[1];
		return 20.0 + (x1 * x1) + (x2 * x2) - 10.0 * (Math.cos(2 * Math.PI * x1) + Math.cos(2 * Math.PI * x2));
	}

}

package pl.edu.agh.miss;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jswarm_pso.Neighborhood;
import net.sourceforge.jswarm_pso.Neighborhood1D;
import net.sourceforge.jswarm_pso.ParticleUpdateSimple;
import net.sourceforge.jswarm_pso.Swarm;
import pl.edu.agh.miss.chart.Chart;
import pl.edu.agh.miss.chart.Point;
import pl.edu.agh.miss.chart.ScatterChart;
import pl.edu.agh.miss.multidimensional.AlgebraUtils;
import pl.edu.agh.miss.multidimensional.RastriginFunction;
import pl.edu.agh.miss.particle.ParticleUpdateAltercentric;
import pl.edu.agh.miss.particle.ParticleUpdateBC;
import pl.edu.agh.miss.particle.ParticleUpdateEgocentric;
import pl.edu.agh.miss.particle.SpeciesParticle;
import pl.edu.agh.miss.particle.SpeciesType;
import pl.edu.agh.miss.swarm.MultiSwarm;
import pl.edu.agh.miss.swarm.SwarmInformation;

public class Main {

	public static void main(String[] args) {
		System.out.println("Example of Particle MultiSwarm Optimization: Optimizing Rastrijin's funtion");

		/*SpeciesParticle egocentricParticle = new MyParticle(SpeciesType.EGOCENTRIC);
		SwarmInformation egocentricSwarmInfo = new SwarmInformation(
				Swarm.DEFAULT_NUMBER_OF_PARTICLES / 3, 
				egocentricParticle,
				new ParticleUpdateEgocentric(egocentricParticle)
		);
		SpeciesParticle altercentricParticle = new MyParticle(SpeciesType.ALTERCENTRIC);
		SwarmInformation altercentricSwarmInfo = new SwarmInformation(
				Swarm.DEFAULT_NUMBER_OF_PARTICLES / 3, 
				altercentricParticle,
				new ParticleUpdateAltercentric(altercentricParticle)
		);
		SpeciesParticle bcParticle = new MyParticle(SpeciesType.BAD_AT_CONFLICT_HANDLING);
		SwarmInformation bcSwarmInfo = new SwarmInformation(
				Swarm.DEFAULT_NUMBER_OF_PARTICLES / 3, 
				bcParticle,
				new ParticleUpdateBC(bcParticle)
		);
		
		SwarmInformation swarmInfos[] = {egocentricSwarmInfo, altercentricSwarmInfo, bcSwarmInfo};*/
		
		SpeciesParticle simpleParticle = new MyParticle(SpeciesType.EGOCENTRIC);
		SwarmInformation simpleSwarmInfo = new SwarmInformation(
				Swarm.DEFAULT_NUMBER_OF_PARTICLES, 
				simpleParticle,
				new ParticleUpdateSimple(simpleParticle)
		);
		
		SwarmInformation swarmInfos[] = {simpleSwarmInfo};
		
		// Create a swarm (using 'MyParticle' as sample particle and 'MyFitnessFunction' as finess function)
		MultiSwarm multiSwarm = new MultiSwarm(swarmInfos, new RastriginFunction());

		// Use neighborhood
		Neighborhood neigh = new Neighborhood1D(Swarm.DEFAULT_NUMBER_OF_PARTICLES / 5, true);
		multiSwarm.setNeighborhood(neigh);
		multiSwarm.setNeighborhoodIncrement(0.9);

		// Tune swarm's update parameters (if needed)
		multiSwarm.setInertia(0.95);
		multiSwarm.setParticleIncrement(0.8);
		multiSwarm.setGlobalIncrement(0.8);

		// Set position (and velocity) constraints. I.e.: where to look for solutions
		multiSwarm.setMaxPosition(100);
		multiSwarm.setMinPosition(-100);

		int numberOfIterations = 5000;
		List<Point> points = new ArrayList<Point>();
		for(int i = 0; i < numberOfIterations; ++i) {
			// Evolve swarm
			multiSwarm.evolve();
			if(i % 100 == 0 && i > 500) {
				points.add(new Point((double)i, multiSwarm.getBestFitness()));
				System.out.println(multiSwarm.getBestFitness());
			}
		}
		
		Chart chart = new ScatterChart().setTitle("A").setXAxisTitle("a").setYAxisTitle("b");
		chart.addSeries("w", points);
		chart.save();

		// Show best position
		double bestPosition[] = multiSwarm.getBestPosition();
		System.out.print("Best position: [" + bestPosition[0]);
		for(int i = 0; i < bestPosition.length; ++i)
			System.out.print(", " + bestPosition[i]);
		System.out.println("]\nBest fitness: " + multiSwarm.getBestFitness());
	}

}

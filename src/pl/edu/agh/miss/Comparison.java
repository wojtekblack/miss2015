package pl.edu.agh.miss;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jswarm_pso.Neighborhood;
import net.sourceforge.jswarm_pso.Neighborhood1D;
import net.sourceforge.jswarm_pso.Swarm;
import pl.edu.agh.miss.chart.Chart;
import pl.edu.agh.miss.chart.Point;
import pl.edu.agh.miss.chart.ScatterChart;
import pl.edu.agh.miss.multidimensional.RastriginFunction;
import pl.edu.agh.miss.particle.ParticleUpdateAltercentric;
import pl.edu.agh.miss.particle.ParticleUpdateBC;
import pl.edu.agh.miss.particle.ParticleUpdateEgocentric;
import pl.edu.agh.miss.particle.SpeciesParticle;
import pl.edu.agh.miss.particle.SpeciesType;
import pl.edu.agh.miss.swarm.MultiSwarm;
import pl.edu.agh.miss.swarm.SwarmInformation;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_DIMENTIONS;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_ITERATIONS;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_PARTICLES;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_SKIPPED_ITERATIONS;

public class Comparison {
	private static List<Point> standardResults = new ArrayList<Point>();
	private static List<Point> multiSpeciesResults = new ArrayList<Point>();
	
	public static void main(String[] args) {
		System.out.println("Comparison between standard PSO and multi species PSO");
		System.out.println("Number of dimensions: " + NUMBER_OF_DIMENTIONS);
		System.out.println("Number of iterations: " + NUMBER_OF_ITERATIONS);
		System.out.println();
		
		System.out.println("Starting standard optimization");
		runStandardSolution();
		
		System.out.println("Starting multi species optimization");
		runMultiSpeciesSolution();
		
		Chart chart = new ScatterChart().setTitle("PSO Ristrigin optimizing, " + NUMBER_OF_DIMENTIONS + " dimensions, " + NUMBER_OF_ITERATIONS + " iterations").
				setXAxisTitle("Iterations").setYAxisTitle("Fitness");
		chart.addSeries("Standard", standardResults);
		chart.addSeries("Multi species", multiSpeciesResults);
		chart.save();
	}
	
	private static void runStandardSolution(){
		Swarm swarm = new Swarm(NUMBER_OF_PARTICLES, new StandardParticle(), new RastriginFunction(false));
		
		// Use neighborhood
		Neighborhood neigh = new Neighborhood1D(NUMBER_OF_PARTICLES / 5, true);
		swarm.setNeighborhood(neigh);
		swarm.setNeighborhoodIncrement(0.9);

		swarm.setInertia(0.95);
		swarm.setParticleIncrement(0.8);
		swarm.setGlobalIncrement(0.8);
		swarm.setMaxPosition(100);
		swarm.setMinPosition(-100);
		swarm.setMaxMinVelocity(0.1);
		

		for (int i = 0; i < NUMBER_OF_ITERATIONS; i++){
			swarm.evolve();
			
			if(i % 100 == 0 && i > NUMBER_OF_SKIPPED_ITERATIONS) {
				standardResults.add(new Point((double)i, swarm.getBestFitness()));
			}
		}
		
		System.out.println("Best fitness: " + swarm.getBestFitness());
	}
	
	
	private static void runMultiSpeciesSolution(){
		SpeciesParticle egocentricParticle = new MyParticle(SpeciesType.EGOCENTRIC);
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
		
		SwarmInformation swarmInfos[] = {egocentricSwarmInfo, altercentricSwarmInfo, bcSwarmInfo};

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

		for(int i = 0; i < NUMBER_OF_ITERATIONS; ++i) {
			// Evolve swarm
			multiSwarm.evolve();
			if(i % 100 == 0 && i > NUMBER_OF_SKIPPED_ITERATIONS) {
				multiSpeciesResults.add(new Point((double)i, multiSwarm.getBestFitness()));
			}
		}
		
		System.out.println("Best fitness: " + multiSwarm.getBestFitness());
	}
}

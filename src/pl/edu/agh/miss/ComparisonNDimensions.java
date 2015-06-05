package pl.edu.agh.miss;

import static pl.edu.agh.miss.Simulation.NUMBER_OF_DIMENTIONS;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_ITERATIONS;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_PARTICLES;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.jswarm_pso.Neighborhood;
import net.sourceforge.jswarm_pso.Neighborhood1D;
import net.sourceforge.jswarm_pso.Swarm;
import pl.edu.agh.miss.chart.Chart;
import pl.edu.agh.miss.chart.Point;
import pl.edu.agh.miss.chart.ScatterChart;
import pl.edu.agh.miss.multidimensional.RastriginFunction;
import pl.edu.agh.miss.particle.MyParticle;
import pl.edu.agh.miss.particle.StandardParticle;
import pl.edu.agh.miss.particle.deprecated.ParticleUpdateAltercentric;
import pl.edu.agh.miss.particle.deprecated.ParticleUpdateBC;
import pl.edu.agh.miss.particle.deprecated.ParticleUpdateEgocentric;
import pl.edu.agh.miss.particle.deprecated.ParticleUpdateGC;
import pl.edu.agh.miss.particle.deprecated.SpeciesParticle;
import pl.edu.agh.miss.particle.deprecated.SpeciesType;
import pl.edu.agh.miss.swarm.MultiSwarm;
import pl.edu.agh.miss.swarm.SwarmInformation;

public class ComparisonNDimensions {
	private static final int EXECUTIONS = 10;
	private static Map<Integer, List<Double>> standardResults = new HashMap<Integer, List<Double>>();
	private static Map<Integer, List<Double>> multiSpeciesResults = new HashMap<Integer, List<Double>>();
	
	
	
	public static void main(String [] args){
		int dimensions = Simulation.NUMBER_OF_DIMENTIONS;
		
		for(Simulation.NUMBER_OF_DIMENTIONS = 5; Simulation.NUMBER_OF_DIMENTIONS <= dimensions; Simulation.NUMBER_OF_DIMENTIONS += 10){
			System.out.println("Execution for " + Simulation.NUMBER_OF_DIMENTIONS + " dimensions");
			for(int i = 0; i < EXECUTIONS; i++){
				System.out.println("\tExecution " + (i+1) + " of " + EXECUTIONS);
				runStandardSolution();
				runMultiSpeciesSolution();
			}
		}
		
		Simulation.NUMBER_OF_DIMENTIONS = dimensions;
		
		//create charts
		Chart chart = new ScatterChart().setTitle("PSO Ristrigin optimizing, " + NUMBER_OF_DIMENTIONS + " dimensions, " + NUMBER_OF_ITERATIONS + " iterations").
				setXAxisTitle("Dimensions").setYAxisTitle("Fitness").addSubTitle("" + EXECUTIONS + " executions");
		
		//calculate average
		List<Point> standardPoints = new ArrayList<Point>();
		for(int key : standardResults.keySet()){
			List<Double> values = standardResults.get(key);
			double sum = 0.0;
			
			for(double value : values){
				sum += value;
			}
			
			standardPoints.add(new Point(key, sum / values.size()));
		}
		chart.addSeries("Standard", standardPoints);
		
		List<Point> multiSpeciesPoints = new ArrayList<Point>();
		for(int key : multiSpeciesResults.keySet()){
			List<Double> values = multiSpeciesResults.get(key);
			double sum = 0.0;
			
			for(double value : values){
				sum += value;
			}
			
			multiSpeciesPoints.add(new Point(key, sum / values.size()));
		}
		
		chart.addSeries("Multi species", multiSpeciesPoints);
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
		}
		
		if(!standardResults.containsKey(Simulation.NUMBER_OF_DIMENTIONS)) standardResults.put(Simulation.NUMBER_OF_DIMENTIONS, new ArrayList<Double>());
		standardResults.get(Simulation.NUMBER_OF_DIMENTIONS).add(swarm.getBestFitness());
		
	}
	
	
	private static void runMultiSpeciesSolution(){
		SpeciesParticle egocentricParticle = new MyParticle(SpeciesType.EGOCENTRIC);
		ParticleUpdateEgocentric update1 = new ParticleUpdateEgocentric(egocentricParticle);
		update1.setLocalIncrement(0.8);
		update1.setGlobalIncrement(0.8);
		SwarmInformation egocentricSwarmInfo = new SwarmInformation(
				Swarm.DEFAULT_NUMBER_OF_PARTICLES / 4, 
				egocentricParticle,
				update1
		);
		SpeciesParticle altercentricParticle = new MyParticle(SpeciesType.ALTERCENTRIC);
		ParticleUpdateAltercentric update2 = new ParticleUpdateAltercentric(altercentricParticle);
		update2.setGlobalIncrement(0.8);
		SwarmInformation altercentricSwarmInfo = new SwarmInformation(
				Swarm.DEFAULT_NUMBER_OF_PARTICLES / 4, 
				altercentricParticle,
				update2
		);
		SpeciesParticle bcParticle = new MyParticle(SpeciesType.BAD_AT_CONFLICT_HANDLING);
		ParticleUpdateBC update3 = new ParticleUpdateBC(bcParticle);
		update3.setLocalIncrement(0.8);
		update3.setNeighbourIncrement(0.9);
		update3.setGlobalIncrement(0.8);
		SwarmInformation bcSwarmInfo = new SwarmInformation(
				Swarm.DEFAULT_NUMBER_OF_PARTICLES / 4, 
				bcParticle,
				update3
		);
		SpeciesParticle gcParticle = new MyParticle(SpeciesType.GOOD_AT_CONFLICT_HANDLING);
		ParticleUpdateGC update4 = new ParticleUpdateGC(gcParticle);
		update4.setNeighbourIncrement(0.9);
		update4.setGlobalIncrement(0.8);
		SwarmInformation gcSwarmInfo = new SwarmInformation(
				Swarm.DEFAULT_NUMBER_OF_PARTICLES / 4, 
				gcParticle,
				update4
		);
		
		SwarmInformation swarmInfos[] = {egocentricSwarmInfo, altercentricSwarmInfo, bcSwarmInfo, gcSwarmInfo};

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
		}
		
		if(!multiSpeciesResults.containsKey(Simulation.NUMBER_OF_DIMENTIONS)) multiSpeciesResults.put(Simulation.NUMBER_OF_DIMENTIONS, new ArrayList<Double>());
		multiSpeciesResults.get(Simulation.NUMBER_OF_DIMENTIONS).add(multiSwarm.getBestFitness());
	}
}

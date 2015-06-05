package pl.edu.agh.miss;

import static pl.edu.agh.miss.Simulation.NUMBER_OF_DIMENTIONS;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_ITERATIONS;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_PARTICLES;
import static pl.edu.agh.miss.Simulation.NUMBER_OF_SKIPPED_ITERATIONS;

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
import pl.edu.agh.miss.particle.ParticleUpdateAltercentric;
import pl.edu.agh.miss.particle.ParticleUpdateBC;
import pl.edu.agh.miss.particle.ParticleUpdateEgocentric;
import pl.edu.agh.miss.particle.ParticleUpdateGC;
import pl.edu.agh.miss.particle.SpeciesParticle;
import pl.edu.agh.miss.particle.SpeciesType;
import pl.edu.agh.miss.swarm.MultiSwarm;
import pl.edu.agh.miss.swarm.SwarmInformation;

public class ComparisonNIterations {
	private static final int EXECUTIONS = 20;
	private static Map<Integer, List<Double>> standardResults = new HashMap<Integer, List<Double>>();
	private static Map<Integer, List<Double>> multiSpeciesResults = new HashMap<Integer, List<Double>>();
	
	public static void main(String [] args){
		for(int i = 0; i < EXECUTIONS; i++){
			System.out.println("Execution " + (i+1) + " of " + EXECUTIONS);
			runStandardSolution();
			runMultiSpeciesSolution();
		}
		
		//create charts
		Chart chart = new ScatterChart().setTitle("PSO Ristrigin optimizing, " + NUMBER_OF_DIMENTIONS + " dimensions, " + NUMBER_OF_ITERATIONS + " iterations").
				setXAxisTitle("Iterations").setYAxisTitle("Fitness").addSubTitle("" + EXECUTIONS + " executions");
		
		//calculate average
		List<Point> standardPoints = new ArrayList<Point>();
		for(int key : standardResults.keySet()){
			List<Double> values = standardResults.get(key);
			double sum = 0.0;
			
			for(double value : values){
				sum += value;
			}
			
			double average = sum / values.size();
			double standardDeviation = standardDeviation(values, average);
			
			standardPoints.add(new Point(key, average, standardDeviation));
		}
		chart.addSeries("Standard", standardPoints);
		
		List<Point> multiSpeciesPoints = new ArrayList<Point>();
		for(int key : multiSpeciesResults.keySet()){
			List<Double> values = multiSpeciesResults.get(key);
			double sum = 0.0;
			
			for(double value : values){
				sum += value;
			}
			
			double average = sum / values.size();
			double standardDeviation = standardDeviation(values, average);
			
			multiSpeciesPoints.add(new Point(key, average, standardDeviation));
		}
		
		chart.addSeries("Multi species", multiSpeciesPoints);
		chart.saveWithDateStamp("raw/chart");
		
		chart.addStandardDeviation().saveWithDateStamp("sd/chart");
	}
	
	private static double standardDeviation(List<Double> values, double average){
		double sum = 0.0;
		
		for(double value : values){
			sum += Math.pow(average - value, 2.0);
		}
		
		double variance = sum / values.size();
		
		return Math.sqrt(variance);
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
				if(!standardResults.containsKey(i)) standardResults.put(i, new ArrayList<Double>());
				standardResults.get(i).add(swarm.getBestFitness());
			}
		}
	}
	
	
	private static void runMultiSpeciesSolution(){
		SpeciesParticle egocentricParticle = new MyParticle(SpeciesType.EGOCENTRIC);
		ParticleUpdateEgocentric update1 = new ParticleUpdateEgocentric(egocentricParticle);
		update1.setLocalIncrement(0.8);
		update1.setGlobalIncrement(0.8);
		SwarmInformation egocentricSwarmInfo = new SwarmInformation(
				(int) (Swarm.DEFAULT_NUMBER_OF_PARTICLES * 0.25), 
				egocentricParticle,
				update1
		);
		SpeciesParticle altercentricParticle = new MyParticle(SpeciesType.ALTERCENTRIC);
		ParticleUpdateAltercentric update2 = new ParticleUpdateAltercentric(altercentricParticle);
		update2.setGlobalIncrement(0.8);
		SwarmInformation altercentricSwarmInfo = new SwarmInformation(
				(int) (Swarm.DEFAULT_NUMBER_OF_PARTICLES * 0.25), 
				altercentricParticle,
				update2
		);
		SpeciesParticle bcParticle = new MyParticle(SpeciesType.BAD_AT_CONFLICT_HANDLING);
		ParticleUpdateBC update3 = new ParticleUpdateBC(bcParticle);
		update3.setLocalIncrement(0.8);
		update3.setNeighbourIncrement(0.9);
		update3.setGlobalIncrement(0.8);
		SwarmInformation bcSwarmInfo = new SwarmInformation(
				(int) (Swarm.DEFAULT_NUMBER_OF_PARTICLES * 0.15), 
				bcParticle,
				update3
		);
		SpeciesParticle gcParticle = new MyParticle(SpeciesType.GOOD_AT_CONFLICT_HANDLING);
		ParticleUpdateGC update4 = new ParticleUpdateGC(gcParticle);
		update4.setNeighbourIncrement(0.9);
		update4.setGlobalIncrement(0.8);
		SwarmInformation gcSwarmInfo = new SwarmInformation(
				(int) (Swarm.DEFAULT_NUMBER_OF_PARTICLES * 0.35), 
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
			if(i % 100 == 0 && i > NUMBER_OF_SKIPPED_ITERATIONS) {
				if(!multiSpeciesResults.containsKey(i)) multiSpeciesResults.put(i, new ArrayList<Double>());
				multiSpeciesResults.get(i).add(multiSwarm.getBestFitness());
			}
		}
	}
}

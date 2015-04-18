package pl.edu.agh.miss;

import net.sourceforge.jswarm_pso.Neighborhood;
import net.sourceforge.jswarm_pso.Neighborhood1D;
import net.sourceforge.jswarm_pso.ParticleUpdateSimple;
import net.sourceforge.jswarm_pso.Swarm;

public class Main {

	public static void main(String[] args) {
		System.out.println("Example of Particle MultiSwarm Optimization: Optimizing Rastrijin's funtion");

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
		
		/*SpeciesParticle simpleParticle = new MyParticle(SpeciesType.EGOCENTRIC);
		SwarmInformation simpleSwarmInfo = new SwarmInformation(
				Swarm.DEFAULT_NUMBER_OF_PARTICLES, 
				simpleParticle,
				new ParticleUpdateSimple(simpleParticle)
		);
		
		SwarmInformation swarmInfos[] = {simpleSwarmInfo};*/
		
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
		for(int i = 0; i < numberOfIterations; ++i) {
			// Evolve swarm
			multiSwarm.evolve();
		}

		// Show best position
		double bestPosition[] = multiSwarm.getBestPosition();
		System.out.println("Best position: [" + bestPosition[0] + ", " + bestPosition[1] + " ]\nBest fitness: " + multiSwarm.getBestFitness() + "\nKnown Solution: [0.0, 0.0]");
	}

}

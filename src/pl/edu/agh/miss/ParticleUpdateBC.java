package pl.edu.agh.miss;

import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.ParticleUpdate;
import net.sourceforge.jswarm_pso.Swarm;

public class ParticleUpdateBC extends ParticleUpdate {

	public ParticleUpdateBC(Particle particle) {
		super(particle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(Swarm swarm, Particle particle) {
		double position[] = particle.getPosition();
		double velocity[] = particle.getVelocity();
		double globalBestPosition[] = swarm.getBestPosition();
		double particleBestPosition[] = particle.getBestPosition();
		double neighBestPosition[] = swarm.getNeighborhoodBestPosition(particle);

		// Update velocity and position
		for (int i = 0; i < position.length; i++) {
			// Update position
			position[i] = position[i] + velocity[i];

			// Update velocity
			velocity[i] = swarm.getInertia() * velocity[i] // Inertia
					+ Math.random() * swarm.getParticleIncrement() * (particleBestPosition[i] - position[i]) // Local best
					+ Math.random() * swarm.getNeighborhoodIncrement() * (neighBestPosition[i] - position[i]) // Neighborhood best					
					+ Math.random() * swarm.getGlobalIncrement() * (globalBestPosition[i] - position[i]); // Global best
		}
	}

}

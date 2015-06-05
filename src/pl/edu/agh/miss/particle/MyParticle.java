package pl.edu.agh.miss.particle;

import pl.edu.agh.miss.Simulation;
import pl.edu.agh.miss.particle.deprecated.SpeciesParticle;
import pl.edu.agh.miss.particle.deprecated.SpeciesType;

public class MyParticle extends SpeciesParticle {
	

	public MyParticle(SpeciesType type) {
		super(type, Simulation.NUMBER_OF_DIMENTIONS);
	}

}

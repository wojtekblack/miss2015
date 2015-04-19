package pl.edu.agh.miss;

import pl.edu.agh.miss.particle.SpeciesParticle;
import pl.edu.agh.miss.particle.SpeciesType;

public class MyParticle extends SpeciesParticle {
	

	public MyParticle(SpeciesType type) {
		super(type, Simulation.NUMBER_OF_DIMENTIONS);
	}

}

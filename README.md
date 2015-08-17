# Multispecies Particle Swarm Optimization
This repository contains an implementation of [PSO algorithm](https://en.wikipedia.org/wiki/Particle_swarm_optimization) which was modified by introducing several kinds of particles with different behaviours.

## The species
Different kinds of particles are called the *species*. Each species has its own way of calculating the velocity. In a standard PSO every particle determines the direction of movement by combining the following three components:
- Position of global best known solution
- Position of neighbourhood's best known solution
- Position of particle's best known solution

The species and their weights used for calculating velocity are described in a table below:
| Species name | Global | Neighbourhood | Particle | Commentary |
| ------------ | :----: | :-----------: | :------: | ---------- |
| All | 1.0 | 1.0 | 1.0 | This species is influenced by all three components, the same as in the standard PSO |

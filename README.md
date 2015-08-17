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
| GLobal & Local | 1.0 | 0.0 | 1.0 | This species ignores the influence of neighbourhood's best known position |
| GLobal & Neighbourhood | 1.0 | 1.0 | 0.0 | This species ignores the influence of particle's best known position |
| Local & Neighbourhood | 0.0 | 1.0 | 1.0 | This species ignores the influence of global best known position |
| Global Only | 1.0 | 0.0 | 0.0 | This particle is influenced only by global best known position |
| Local Only | 0.0 | 0.0 | 1.0 | This particle is influenced only by particle's best known position |
| Neighbourhood Only | 0.0 | 1.0 | 0.0 | This particle is influenced only by neighbourhood's best known position |
| Random | random | random | random | This particle multiplies all three influences by random weghts. The sum of weights is equal to 3 |

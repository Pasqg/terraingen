# Terrain generator

The terrain generator creates a base heightmap using layered perlin noise,
where each layer is at multiple frequency (harmonics). The resulting
heightmaps look like this:

![perlin noise](docs_files/heightmap_3409129824829640289.png)

This renders terrain like:

![rendered](docs_files/output_3409129824829640289.png)

# Erosion

To make terrain more realistic, the terrain generator can simulate erosion.
There are various papers that provide implementations which this terrain
generator is based on, plus smaller modification for convenience and/or to
achieve desired results.

The gist of the algorithm is to drop a rain particle at a random location
for every iteration. (Parameters of the model are in **bold**):

- The rain particle, starting with a certain **terminal velocity**, hits
  the ground and moves towards a random direction.
- As the particle moves, the speed and direction of the particle are
  affected by the slope (gradient) of the terrain, the **gravity**, its
  mass (**parameterizable starting range**) and **inertia**.
- As the particle moves, it removes sediment with a certain **erosion
  rate**, from its previous location up to its **capacity**, and deposits
  part of the carried sediment at the next location, spreading it across a
  certain **deposition radius**.
- The mass of the particle increases when it picks sediment up, and reduces
  every step according to an **evaporation rate**.
- The simulation stops when the particle has stopped, has lost all its mass
  (below an hard-coded threshold) or has moved for enough iterations (
  hard-coded to minimum between map width and height).

#### Results:

Note the fjord-like structures that emerge. The erosion patterns can be
affected by many parameters.

![eroded heightmap](docs_files/heightmap_eroded_3409129824829640289.png)

![eroded render](docs_files/output_eroded_3409129824829640289.png)

## Erosion parameters exploration

### Inertia

Inertia affects how far a particle moves from its dropping point. High
inertia values result in "washed away" flat-ish landscapes, similar to flat
sand dunes. Low inertia values result in very steep ridges on mountain
sides.

The erosion algorithm can be started from an already eroded heightmap to
combine several effects and reach different results. In the below examples,
layering low inertia after high inertia erosion, creates a nice flat-ish
landscape with plateaus whose sides are then eroded by low-inertia
particles creating interesting ridges and cliffs.

#### Inertia = 0.01, 500K particles:

![](docs_files/heightmap_0.01inertia_500k.png)

![](docs_files/output_0.01inertia_500k.png)

#### Inertia = 0.03, 500K particles:

![](docs_files/heightmap_0.03inertia_500k.png)

![](docs_files/output_0.03inertia_500k.png)

#### Double pass (1) Inertia = 0.01, 500K particles (2) Inertia = 0.001, 200K particles

![](docs_files/heightmap_0.01inertia_500k_0.001inertia_200k.png)

![](docs_files/output_0.01inertia_500k_0.001inertia_200k.png)

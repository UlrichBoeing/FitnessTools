package de.ulrich_boeing.adaptable

//import de.ulrich_boeing.basics.Clipping
//import de.ulrich_boeing.basics.mix
//import processing.core.PApplet
//import processing.core.PImage
//
//class Population(val app: PApplet, val target: PImage, val count: Int) {
//    var population = Array<Adaptable>(count) { Adaptable(app) }
//    var generations = 0
//
//    fun fitness(clipping: Clipping): Array<Float> {
//        return Array(population.size) { i ->
//            population[i].fitness(target, population[i].getImage(clipping))
//        }
//    }
//
//    fun list(fitness: Array<Float>): Array<Pair<Adaptable, Float>> {
//        return Array(population.size) { i ->
//            Pair(population[i], fitness[i])
//        }
//    }
//
//    fun creation(male: Adaptable, female: Adaptable): Adaptable {
//        return Adaptable(male.app, male.DNAs.mix(female.DNAs, 3))
//    }
//
//    fun nextGeneration() {
//
//    }
//
//
//
//
//}

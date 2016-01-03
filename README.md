# Neural Networks Experiment One

This repository was my first experimentation using neural networks evolved via a genetic algorithm.
It has been through various revisions but the core concept is the same.

It consists of a simulation of creatures that must obtain food to increase their energy levels. 
After a set period of time (in ticks), the creatures are bred together to create a new population and
the simulation restarts. 

The new creatures are created by breeding and mutating two creatures from the previous generation. 
Parents are selected at random, with a greater chance of creatures with higher energy being selected 
(known as [roulette wheel selection](https://en.wikipedia.org/wiki/Fitness_proportionate_selection)).


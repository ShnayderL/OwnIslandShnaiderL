# 🏝️ Island Simulation (Java, Multithreading)

This project is a simple multithreaded simulation of an island ecosystem written in Java.  
It models how animals and plants live, eat, move, reproduce, and die over time.

The project was created for learning Java multithreading and object-oriented programming.

---

## 🎯 Project Purpose

The goal of this project is to:
- practice multithreading in Java;
- understand synchronization and shared data access;
- apply object-oriented principles in a simulation;
- observe population changes over time.

---

## 🧩 Simulation Overview

- The island is represented as a grid.
- Each cell contains animals and plants.
- Animals perform the following actions:
  - eat,
  - reproduce,
  - move,
  - die.
- Plants reproduce automatically.
- The simulation runs in parallel to improve performance.

---

## 🏗️ Main Components

### Island
- Controls the entire simulation.
- Creates the island grid.
- Runs simulation in parallel regions.
- Displays population statistics during execution.

### Location
- Represents a single cell of the island.
- Stores animals and plants.
- Executes life cycles in parallel.
- Uses locks to prevent data conflicts.

### Animal (abstract)
- Base class for all animals.
- Contains common properties such as weight, speed, and saturation.
- Defines basic behavior methods like `move()` and `eat()`.

### Animals and Plants
Examples of species in the simulation:
- Predators: Wolf, Fox, Bear, Boa, Eagle
- Herbivores: Mouse, Rabbit, Sheep, Goat, Horse, Deer, Boar, Buffalo, Duck, Caterpillar
- Plants

Each species has its own behavior and rules.

---

## 🔄 Multithreading (Simple Explanation)

- Different parts of the island are processed at the same time.
- Inside each part, animals and plants are processed in parallel.
- Locks are used to keep shared data safe.
- This approach helps simulate many creatures efficiently.

---

## 📊 Simulation Output

During execution, the program prints:
- current simulation day;
- total number of creatures;
- number of predators, herbivores, and plants;
- how many animals were born and died during the day.

---

## ➕ Adding a New Animal

To add a new species:
1. Create a new class that extends `Predator` or `Herbivorous`.
2. Define its behavior (`eat`, `move`, `reproduce`).
3. Add configuration values for the new animal.
4. Register the animal in the simulation.

---

## ⚠️ Limitations

- Designed for learning purposes, not high performance.
- Uses simple synchronization mechanisms.
- Console output is used instead of a logging system.

---

## 📝 Summary

This project is a beginner-friendly example of a multithreaded Java application.  
It demonstrates how animals, plants, and concurrency can be combined into a working simulation.


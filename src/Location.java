import java.util.ArrayList;
import java.util.List;

public class Location {
    private List<Animal> predators = new ArrayList<>();
    private List<Animal> herbivores = new ArrayList<>();
    private List<Plant> plants = new ArrayList<>();

    public void addPredator(Animal animal) {
        predators.add(animal);
    }

    public void removePredator(Animal animal) {
        predators.remove(animal);
    }
    public void addHerbivores(Animal animal) {
        predators.add(animal);
    }

    public void removeHerbivores(Animal animal) {
        predators.remove(animal);
    }
    public void addPlant(Plant plant){
        plants.add(plant);
    }
    public List<Plant> getPlants() {
        return plants;
    }
    public List<Animal> getPredators() {
        return predators;
    }
    public List<Animal> getHerbivores() {
        return herbivores;
    }
}
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Location {
    private final ReentrantLock lock = new ReentrantLock();
    private Point coordinatesOfLocation;
    private List<Animal> predators = new ArrayList<>();
    private List<Animal> herbivores = new ArrayList<>();
    private List<Plant> plants = new ArrayList<>();

    public Location(Point coordinatesOfLocation){
        this.coordinatesOfLocation = coordinatesOfLocation;
    }
    public ReentrantLock getLock() {
        return lock;
    }
    public Point getCoordinatesOfLocation(){
        return this.coordinatesOfLocation;
    }
    public void addPredator(Animal animal) {
        predators.add(animal);
    }

    public void removePredator(Animal animal) {
        if(predators.contains(animal)){
            predators.remove(animal);
        }
    }
    public void addHerbivores(Animal animal) {
        predators.add(animal);
    }

    public void removeHerbivores(Animal animal) {
        if(predators.contains(animal)){
            predators.remove(animal);
        }
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
import java.util.concurrent.TimeUnit;

public class Plant implements Creature{
    private boolean isAlive;
    private final Island currentIsland;
    private Location currentLocation;

    public Plant(Island island, Location location){
        this.currentIsland = island;
        this.currentLocation = location;
        this.isAlive = true;
    }
    public int getWeight(){
        return 1;
    }
    @Override
    public void reproduce() {
        if(currentLocation.getPlants().size() > 1){
            currentLocation.getPlants().add(new Plant(currentIsland, currentLocation));
        }
    }
    @Override
    public String toString() {
        return "\uD83C\uDF31";
    }
    @Override
    public void die() {
        isAlive = false;
        if (currentLocation.getPlants().contains(this)) {
            currentLocation.getPlants().remove(this);
        }
    }
}
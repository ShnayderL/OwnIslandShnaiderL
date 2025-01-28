import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class Animal implements Creature{
    private double weight;
    private int movementSpeed;
    private double maxSaturation;
    private boolean alive;
    private double saturation;
    private Location currentLocation;
    private final Island currentIsland;


    public Animal(Island island, String weightKey, String movementSpeedKey, String amountOfFoodToEatKey, Location location) {
        this.currentIsland = island;
        this.alive = true;
        this.saturation = 1;
        this.currentLocation = location;
        setPropertyValues(weightKey, movementSpeedKey, amountOfFoodToEatKey);
    }
    private void setPropertyValues(String weightKey, String movementSpeedKey, String amountOfFoodToEatKey) {
        Properties prop = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("animalconfig.properties")) {
            prop.load(inputStream);
            this.weight = Double.parseDouble(prop.getProperty(weightKey));
            this.movementSpeed = Integer.parseInt(prop.getProperty(movementSpeedKey));
            this.maxSaturation = Double.parseDouble(prop.getProperty(amountOfFoodToEatKey));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public double getSaturation() {
        return saturation;
    }

    public void setSaturation(double saturation) {
        this.saturation = saturation;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
    public double getWeight() {
        return this.weight;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public Island getCurrentIsland() {
        return currentIsland;
    }

    public double getMaxSaturation() {
        return maxSaturation;
    }

    public abstract void move();
    public abstract void eat();
}

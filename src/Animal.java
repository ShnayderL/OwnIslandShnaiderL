import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class Animal implements Creature{
    private int x;
    private int y;
    private int weight;
    private int maxAmountOnLocation;
    private int movementSpeed;
    private int maxSaturation;
    private final Island currentIsland;


    public Animal(Island island, String weightKey, String maxAmountOnLocationKey, String movementSpeedKey, String amountOfFoodToEatKey) {
        this.currentIsland = island;
        setPropertyValues(weightKey, maxAmountOnLocationKey, movementSpeedKey, amountOfFoodToEatKey);
    }
    private void setPropertyValues(String weightKey, String maxAmountOnLocationKey, String movementSpeedKey, String amountOfFoodToEatKey) {
        Properties prop = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("animalconfig.properties")) {
            prop.load(inputStream);
            this.weight = Integer.valueOf(prop.getProperty(weightKey));
            this.maxAmountOnLocation = Integer.valueOf(prop.getProperty(maxAmountOnLocationKey));
            this.movementSpeed = Integer.valueOf(prop.getProperty(movementSpeedKey));
            this.maxSaturation = Integer.valueOf(prop.getProperty(amountOfFoodToEatKey));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getWeight() {
        return this.weight;
    }

    public int getMaxAmountOnLocation() {
        return maxAmountOnLocation;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public Island getCurrentIsland() {
        return currentIsland;
    }

    public int getMaxSaturation() {
        return maxSaturation;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if(x > 0){
            this.x = x;
        } else{
            throw new IllegalArgumentException("x must be greater than zero");
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if(y > 0){
            this.y = y;
        } else{
            throw new IllegalArgumentException("y must be greater than zero");
        }
    }
    public abstract void move();
    public abstract void eat();
}

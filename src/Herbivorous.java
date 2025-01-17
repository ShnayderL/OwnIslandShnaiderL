import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Herbivorous extends Animal{
    private final Location currentLocation = this.getCurrentIsland().getLocation(this.getX(), this.getY());
    private int saturation;
    private boolean isAlive;
    private int timesMoved;

    public Herbivorous(Island island, String weightKey, String maxAmountOnLocationKey, String movementSpeedKey, String amountOfFoodToEatKey) {
        super(island, weightKey, maxAmountOnLocationKey, movementSpeedKey, amountOfFoodToEatKey);
        this.saturation = 1;
        this.isAlive = true;
        this.timesMoved = 0;
    }

    @Override
    public void reproduce() {
        if (currentLocation.getHerbivores().size() > 1 && saturation == getMaxSaturation()) {
            currentLocation.addHerbivores(new Sheep(this.getCurrentIsland()));
            saturation = 1;
        }
    }
    @Override
    public void die() {
        if (!currentLocation.getHerbivores().isEmpty() && currentLocation.getHerbivores().contains(this)) {
            this.isAlive = false;
            currentLocation.removeHerbivores(this);
        }
    }
    @Override
    public void move() {
        if (currentLocation.getHerbivores().contains(this)) {
            while (this.isAlive) {
                if(timesMoved > 2){
                    this.die();
                }
                int dx = ThreadLocalRandom.current().nextInt(-3, 4);
                int dy = ThreadLocalRandom.current().nextInt(-3, 4);

                int newX = Math.max(0, currentLocation.getCoordinatesOfLocation().getX() + dx);
                int newY = Math.max(0, currentLocation.getCoordinatesOfLocation().getY() + dy);

                while (newY > this.getCurrentIsland().getISLAND_HEIGHT() || newX > this.getCurrentIsland().getISLAND_WIDTH()) {
                    if (newY > this.getCurrentIsland().getISLAND_HEIGHT()) {
                        newY -= this.getCurrentIsland().getISLAND_HEIGHT();
                    }
                    if (newX > this.getCurrentIsland().getISLAND_WIDTH()) {
                        newX -= this.getCurrentIsland().getISLAND_WIDTH();
                    }
                }

                Location newLocation = this.getCurrentIsland().getLocation(newX, newY);

                synchronized (currentLocation.getLock()) {
                    newLocation.getHerbivores().add(this);
                    currentLocation.getHerbivores().remove(this);
                }
                timesMoved++;
            }
        }
    }
    @Override
    public void eat() {
        while (saturation < getMaxSaturation()) {
            synchronized (currentLocation.getLock()) {
                if (currentLocation.getPlants().isEmpty()) {
                    break;
                }
                currentLocation.getPlants().removeFirst();
            }
            saturation ++;
        }
    }
}

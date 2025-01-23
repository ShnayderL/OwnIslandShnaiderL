import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Herbivorous extends Animal {
    private int timesMoved;

    public Herbivorous(Island island, String weightKey, String movementSpeedKey, String amountOfFoodToEatKey, Location location) {
        super(island, weightKey, movementSpeedKey, amountOfFoodToEatKey, location);
        this.timesMoved = 0;
    }
    public Location getNewLocation(){
        int dx = ThreadLocalRandom.current().nextInt(-getMovementSpeed(), getMovementSpeed() + 1);
        int dy = ThreadLocalRandom.current().nextInt(-getMovementSpeed(), getMovementSpeed() + 1);

        int newX = Math.max(0, getCurrentLocation().getCoordinatesOfLocation().getX() + dx);
        int newY = Math.max(0, getCurrentLocation().getCoordinatesOfLocation().getY() + dy);

        int maxX = this.getCurrentIsland().getISLAND_WIDTH();
        int maxY = this.getCurrentIsland().getISLAND_HEIGHT();

        newX = (newX + maxX) % maxX;
        newY = (newY + maxY) % maxY;

        Location newLocation = this.getCurrentIsland().getLocation(new Point(newX, newY));
        if (newLocation == null) {
            newLocation = this.getCurrentIsland().getLocation(new Point(0, 0));
        }
        return newLocation;
    }
    @Override
    public void die() {
        if (!isAlive()) return;

        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getHerbivores().contains(this)) {
                this.setAlive(false);
                getCurrentLocation().getHerbivores().remove(this);
            }
        }
        getCurrentIsland().increaseHerbivoresDied(1);
    }
    @Override
    public void move() {
        if (!isAlive()) return;

        if (timesMoved > 2) {
            this.die();
            return;
        }
        Location newLocation = getNewLocation();
        ReentrantLock newLocationLock = newLocation.getLock();
        if (newLocationLock.tryLock()) {
            try {
                synchronized (getCurrentLocation().getLock()) {
                    getCurrentLocation().getHerbivores().remove(this);
                }
                newLocation.getHerbivores().add(this);
                this.setCurrentLocation(newLocation);
                timesMoved++;
            } finally {
                newLocationLock.unlock();
            }
        }
    }

    @Override
    public void eat() {
        if (!isAlive()) return;

        while (getSaturation() < getMaxSaturation()) {
            synchronized (getCurrentLocation().getLock()) {
                if (getCurrentLocation().getPlants().isEmpty()) {
                    break;
                }
                Plant plant = getCurrentLocation().getPlants().get(0);
                int weight = plant.getWeight();
                plant.die();
                setSaturation(weight);
            }
        }
    }
}

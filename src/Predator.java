import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Predator extends Animal {
    private int timesMoved;

    public Predator(Island island, String weightKey, String movementSpeedKey, String amountOfFoodToEatKey, Location location) {
        super(island, weightKey, movementSpeedKey, amountOfFoodToEatKey, location);
        this.timesMoved = 0;
    }


    @Override
    public void die() {
        if (!isAlive()) return;
        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getPredators().contains(this)) {
                setAlive(false);
                getCurrentLocation().getPredators().remove(this);
            }
        }
        getCurrentIsland().increasePredatorsDied(1);
    }


    @Override
    public void move() {
        if (!isAlive()) return;

        if (timesMoved > 2) {
            die();
            return;
        }

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

        ReentrantLock currentLock = getCurrentLocation().getLock();
        ReentrantLock newLocationLock = newLocation.getLock();

        if (newLocationLock.tryLock()) {
            try {
                synchronized (getCurrentLocation().getLock()) {
                    getCurrentLocation().getPredators().remove(this);
                }
                synchronized (newLocation.getLock()) {
                    newLocation.getPredators().add(this);
                }
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
            Animal target;

            synchronized (getCurrentLocation().getLock()) {
                if (getCurrentLocation().getHerbivores().isEmpty()) {
                    break;
                }
                target = getCurrentLocation().getHerbivores().getFirst();
                target.die();
            }
            if (target != null) {
                setSaturation(Math.min(getSaturation() + target.getWeight(), getMaxSaturation()));
            }
        }
    }
}

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Predator extends Animal {
    private int timesMoved;
    private int chanceToBeEatenByBoa;
    private int chanceToBeEatenByBear;
    private int chanceToBeEatenByEagle;

    public Predator(Island island, String weightKey, String movementSpeedKey, String amountOfFoodToEatKey, Location location) {
        super(island, weightKey, movementSpeedKey, amountOfFoodToEatKey, location);
        this.timesMoved = 0;
    }

    public void setEatProperties(String chanceToBeEatenByBoa, String chanceToBeEatenByBear, String chanceToBeEatenByEagle) {
        Properties prop = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("eatconfig.properties")) {
            prop.load(inputStream);
            this.chanceToBeEatenByBoa = Integer.parseInt(prop.getProperty(chanceToBeEatenByBoa));
            this.chanceToBeEatenByBear = Integer.parseInt(prop.getProperty(chanceToBeEatenByBear));
            this.chanceToBeEatenByEagle = Integer.parseInt(prop.getProperty(chanceToBeEatenByEagle));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void die() {
        if (!isAlive()) return;
        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getPredators().contains(this)) {
                setAlive(false);
                getCurrentLocation().getPredators().remove(this);
                getCurrentIsland().increasePredatorsDied(1);
            }
        }
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
}

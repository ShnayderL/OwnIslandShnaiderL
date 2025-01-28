import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Herbivorous extends Animal {
    private int timesMoved;
    private int chanceToBeEatenByBoa;
    private int chanceToBeEatenByBear;
    private int chanceToBeEatenByEagle;
    private int chanceToBeEatenByWolf;
    private int chanceToBeEatenByFox;
    private int chanceToBeEatenByMouse;
    private int chanceToBeEatenByBoar;
    private int chanceToBeEatenByDuck;

    public Herbivorous(Island island, String weightKey, String movementSpeedKey, String amountOfFoodToEatKey, Location location) {
        super(island, weightKey, movementSpeedKey, amountOfFoodToEatKey, location);
        this.timesMoved = 0;
    }
    public void setEatProperties(String chanceToBeEatenByBoa, String chanceToBeEatenByBear, String chanceToBeEatenByEagle, String chanceToBeEatenByWolf, String chanceToBeEatenByFox, String chanceToBeEatenByMouse, String chanceToBeEatenByBoar, String chanceToBeEatenByDuck) {
        Properties prop = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("eatconfig.properties")) {
            prop.load(inputStream);
            this.chanceToBeEatenByBoa = Integer.parseInt(prop.getProperty(chanceToBeEatenByBoa));
            this.chanceToBeEatenByBear = Integer.parseInt(prop.getProperty(chanceToBeEatenByBear));
            this.chanceToBeEatenByEagle = Integer.parseInt(prop.getProperty(chanceToBeEatenByEagle));
            this.chanceToBeEatenByWolf = Integer.parseInt(prop.getProperty(chanceToBeEatenByWolf));
            this.chanceToBeEatenByFox = Integer.parseInt(prop.getProperty(chanceToBeEatenByFox));
            this.chanceToBeEatenByMouse = Integer.parseInt(prop.getProperty(chanceToBeEatenByMouse));
            this.chanceToBeEatenByBoar = Integer.parseInt(prop.getProperty(chanceToBeEatenByBoar));
            this.chanceToBeEatenByDuck = Integer.parseInt(prop.getProperty(chanceToBeEatenByDuck));

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public int getChanceToBeEatenByDuck() {
        return chanceToBeEatenByDuck;
    }

    public int getChanceToBeEatenByBoar() {
        return chanceToBeEatenByBoar;
    }

    public int getChanceToBeEatenByMouse() {
        return chanceToBeEatenByMouse;
    }

    public int getChanceToBeEatenByFox() {
        return chanceToBeEatenByFox;
    }

    public int getChanceToBeEatenByEagle() {
        return chanceToBeEatenByEagle;
    }

    public int getChanceToBeEatenByWolf() {
        return chanceToBeEatenByWolf;
    }

    public int getChanceToBeEatenByBear() {
        return chanceToBeEatenByBear;
    }

    public int getChanceToBeEatenByBoa() {
        return chanceToBeEatenByBoa;
    }

    @Override
    public void die() {
        if (!isAlive()) return;

        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getHerbivores().contains(this)) {
                this.setAlive(false);
                getCurrentLocation().getHerbivores().remove(this);
                getCurrentIsland().increaseHerbivoresDied(1);
            }
        }
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
                plant.die();
                setSaturation(getSaturation() + 1);
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

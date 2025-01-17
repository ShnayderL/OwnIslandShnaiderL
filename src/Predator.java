import java.util.concurrent.ThreadLocalRandom;

public class Predator extends Animal{
    private final Location currentLocation = this.getCurrentIsland().getLocation(this.getX(), this.getY());
    private int saturation;
    private boolean isAlive;
    private int timesMoved;

    public Predator(Island island, String weightKey, String maxAmountOnLocationKey, String movementSpeedKey, String amountOfFoodToEatKey) {
        super(island, weightKey, maxAmountOnLocationKey, movementSpeedKey, amountOfFoodToEatKey);
        this.saturation = 1;
        this.isAlive = true;
        this.timesMoved = 0;
    }

    @Override
    public void reproduce() {
        if (currentLocation.getPredators().size() > 1 && saturation == getMaxSaturation()) {
            currentLocation.addPredator(new Wolf(this.getCurrentIsland()));
            saturation = 1;
        }
    }

    @Override
    public void die() {
        if (!currentLocation.getPredators().isEmpty() && currentLocation.getPredators().contains(this)) {
            this.isAlive = false;
            currentLocation.removePredator(this);
        }
    }

    @Override
    public void move() {
        if(currentLocation.getPredators().contains(this)){
            while(this.isAlive){
                if(timesMoved > 2){
                    this.die();
                }
                int dx = ThreadLocalRandom.current().nextInt(-getMovementSpeed(), getMovementSpeed()+1);
                int dy = ThreadLocalRandom.current().nextInt(-getMovementSpeed(), getMovementSpeed()+1);

                int newX = Math.max(0, currentLocation.getCoordinatesOfLocation().getX() + dx);
                int newY = Math.max(0, currentLocation.getCoordinatesOfLocation().getY() + dy);
                while(newY > this.getCurrentIsland().getISLAND_HEIGHT() || newX > this.getCurrentIsland().getISLAND_WIDTH()){
                    if(newY > this.getCurrentIsland().getISLAND_HEIGHT()){
                        newY -= this.getCurrentIsland().getISLAND_HEIGHT();
                    }
                    if(newX > this.getCurrentIsland().getISLAND_WIDTH()){
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
            Animal target;
            synchronized (currentLocation.getLock()) {
                if (currentLocation.getHerbivores().isEmpty()) {
                    break;
                }
                target = currentLocation.getHerbivores().removeFirst();
            }
            saturation = Math.min(saturation + target.getWeight(), getMaxSaturation());
        }
    }
}

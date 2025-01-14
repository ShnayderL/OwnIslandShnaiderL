public class Sheep extends Animal {
    private final Location currentLocation = this.getCurrentIsland().getLocation(this.getX(), this.getY());

    public Sheep(Island island) {
        super(island, "sheepWeight", "sheepMaxAmountOnLocation", "sheepMovementSpeed", "sheepAmountOfFoodToEat");
    }

    @Override
    public void move() {

    }

    @Override
    public void eat() {

    }

    @Override
    public void reproduce() {
        if (currentLocation.getHerbivores().size() > 1) {
            currentLocation.addHerbivores(new Sheep(this.getCurrentIsland()));
        }
    }

    @Override
    public void die() {
        if (!currentLocation.getHerbivores().isEmpty()) {
            this.getCurrentIsland().getLocation(this.getX(), this.getY()).removeHerbivores(this);
        }
    }
}

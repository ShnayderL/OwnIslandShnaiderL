public class Wolf extends Animal {
    private final Location currentLocation = this.getCurrentIsland().getLocation(this.getX(), this.getY());

    public Wolf(Island island) {
        super(island, "woolfWeight", "woolfMaxAmountOnLocation", "woolfMovementSpeed", "woolfAmountOfFoodToEat");
    }

    @Override
    public void reproduce() {
        if (currentLocation.getPredators().size() > 1) {
            currentLocation.addPredator(new Wolf(this.getCurrentIsland()));
        }
    }

    @Override
    public void die() {
        if (!currentLocation.getPredators().isEmpty()) {
            currentLocation.removePredator(this);
        }
    }

    @Override
    public void move() {

    }

    @Override
    public void eat() {

    }
}

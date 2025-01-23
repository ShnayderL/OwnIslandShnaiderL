public class Wolf extends Predator {

    public Wolf(Island island, Location location) {
        super(island, "wolfWeight", "wolfMovementSpeed", "wolfMaxSaturation", location);
    }
    @Override
    public void reproduce() {
        if (!isAlive()) return;
        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getPredators().size() > 1 && getSaturation() == this.getMaxSaturation()) {
                getCurrentLocation().getPredators().add(new Wolf(getCurrentIsland(), getCurrentLocation()));
                setSaturation(1);
            }
        }
        this.getCurrentIsland().increasePredatorsBorn(1);
    }
    @Override
    public String toString() {
        return "\uD83D\uDC3A";
    }
}

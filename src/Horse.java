public class Horse extends Herbivorous {
    public Horse(Island island, Location location) {
        super(island, "horseWeight", "horseMovementSpeed", "horseMaxSaturation", location);
        super.setEatProperties("boa.horse", "bear.horse", "eagle.horse",
                "wolf.horse", "fox.horse", "mouse.horse", "boar.horse", "duck.horse");
    }
    @Override
    public void reproduce() {
        if (!isAlive()) return;

        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getHerbivores().size() > 1 && getSaturation() == this.getMaxSaturation()) {
                getCurrentLocation().getHerbivores().add(new Horse(getCurrentIsland(), getCurrentLocation()));
                setSaturation(1);
                this.getCurrentIsland().increaseHerbivoresBorn(1);
            }
        }
    }
    @Override
    public String toString() {
        return "\uD83D\uDC0E";
    }
}

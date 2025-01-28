public class Buffalo extends Herbivorous {
    public Buffalo(Island island, Location location) {
        super(island, "buffaloWeight", "buffaloMovementSpeed", "buffaloMaxSaturation", location);
        super.setEatProperties("boa.buffalo", "bear.buffalo", "eagle.buffalo",
                "wolf.buffalo", "fox.buffalo", "mouse.buffalo", "boar.buffalo", "duck.buffalo");
    }
    @Override
    public void reproduce() {
        if (!isAlive()) return;

        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getHerbivores().size() > 1 && getSaturation() == this.getMaxSaturation()) {
                getCurrentLocation().getHerbivores().add(new Buffalo(getCurrentIsland(), getCurrentLocation()));
                setSaturation(1);
                this.getCurrentIsland().increaseHerbivoresBorn(1);
            }
        }
    }
    @Override
    public String toString() {
        return "\uD83D\uDC11";
    }
}

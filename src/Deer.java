public class Deer extends Herbivorous {
    public Deer(Island island, Location location) {
        super(island, "deerWeight", "deerMovementSpeed", "deerMaxSaturation", location);
        super.setEatProperties("boa.deer", "bear.deer",
                "eagle.deer", "wolf.deer",
                "fox.deer", "mouse.deer", "boar.deer", "duck.deer");
    }
    @Override
    public void reproduce() {
        if (!isAlive()) return;

        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getHerbivores().size() > 1 && getSaturation() == this.getMaxSaturation()) {
                getCurrentLocation().getHerbivores().add(new Deer(getCurrentIsland(), getCurrentLocation()));
                setSaturation(1);
                this.getCurrentIsland().increaseHerbivoresBorn(1);
            }
        }
    }
    @Override
    public String toString() {
        return "\uD83E\uDD8C";
    }
}

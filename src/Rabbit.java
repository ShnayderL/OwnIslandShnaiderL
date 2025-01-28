public class Rabbit extends Herbivorous {
    public Rabbit(Island island, Location location) {
        super(island, "rabbitWeight", "rabbitMovementSpeed", "rabbitMaxSaturation", location);
        super.setEatProperties("boa.rabbit", "bear.rabbit", "eagle.rabbit", "wolf.rabbit",
                "fox.rabbit", "mouse.rabbit", "boar.rabbit", "duck.rabbit");
    }
    @Override
    public void reproduce() {
        if (!isAlive()) return;

        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getHerbivores().size() > 1 && getSaturation() == this.getMaxSaturation()) {
                getCurrentLocation().getHerbivores().add(new Rabbit(getCurrentIsland(), getCurrentLocation()));
                setSaturation(1);
                this.getCurrentIsland().increaseHerbivoresBorn(1);
            }
        }
    }
    @Override
    public String toString() {
        return "\uD83D\uDC07";
    }
}

public class Sheep extends Herbivorous {
    public Sheep(Island island, Location location) {
        super(island, "sheepWeight", "sheepMovementSpeed", "sheepMaxSaturation", location);
        super.setEatProperties("boa.sheep", "bear.sheep", "eagle.sheep",
                "wolf.sheep", "fox.sheep", "mouse.sheep", "boar.sheep", "duck.sheep");
    }
    @Override
    public void reproduce() {
        if (!isAlive()) return;

        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getHerbivores().size() > 1 && getSaturation() == this.getMaxSaturation()) {
                getCurrentLocation().getHerbivores().add(new Sheep(getCurrentIsland(), getCurrentLocation()));
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

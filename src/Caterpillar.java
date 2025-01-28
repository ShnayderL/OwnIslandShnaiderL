public class Caterpillar extends Herbivorous {
    public Caterpillar(Island island, Location location) {
        super(island, "caterpillarWeight", "caterpillarMovementSpeed", "caterpillarMaxSaturation", location);
        super.setEatProperties("boa.caterpillar", "bear.caterpillar", "eagle.caterpillar", "wolf.caterpillar",
                "fox.caterpillar", "mouse.caterpillar", "boar.caterpillar", "duck.caterpillar");
    }
    @Override
    public void reproduce() {
        if (!isAlive()) return;

        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getHerbivores().size() > 1 && getSaturation() == this.getMaxSaturation()) {
                getCurrentLocation().getHerbivores().add(new Caterpillar(getCurrentIsland(), getCurrentLocation()));
                setSaturation(1);
                this.getCurrentIsland().increaseHerbivoresBorn(1);
            }
        }
    }
    @Override
    public String toString() {
        return "\uD83D\uDC1B";
    }
}

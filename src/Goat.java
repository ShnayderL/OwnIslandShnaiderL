public class Goat extends Herbivorous {
    public Goat(Island island, Location location) {
        super(island, "goatWeight", "goatMovementSpeed", "goatMaxSaturation", location);
        super.setEatProperties("boa.goat", "bear.goat", "eagle.goat", "wolf.goat",
                "fox.goat", "mouse.goat", "boar.goat", "duck.goat");
    }
    @Override
    public void reproduce() {
        if (!isAlive()) return;

        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getHerbivores().size() > 1 && getSaturation() == this.getMaxSaturation()) {
                getCurrentLocation().getHerbivores().add(new Goat(getCurrentIsland(), getCurrentLocation()));
                setSaturation(1);
                this.getCurrentIsland().increaseHerbivoresBorn(1);
            }
        }
    }
    @Override
    public String toString() {
        return "\uD83D\uDC10";
    }
}

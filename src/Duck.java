import java.util.concurrent.ThreadLocalRandom;

public class Duck extends Herbivorous {
    public Duck(Island island, Location location) {
        super(island, "duckWeight", "duckMovementSpeed", "duckMaxSaturation", location);
        super.setEatProperties("boa.duck", "bear.duck", "eagle.duck",
                "wolf.duck", "fox.duck", "mouse.duck", "boar.duck", "duck.duck");
    }
    @Override
    public void reproduce() {
        if (!isAlive()) return;

        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getHerbivores().size() > 1 && getSaturation() == this.getMaxSaturation()) {
                getCurrentLocation().getHerbivores().add(new Duck(getCurrentIsland(), getCurrentLocation()));
                setSaturation(1);
                this.getCurrentIsland().increaseHerbivoresBorn(1);
            }
        }
    }
    @Override
    public void eat() {
        super.eat();
        while (getSaturation() < getMaxSaturation()) {
            int chance = ThreadLocalRandom.current().nextInt(1, 100);
            Herbivorous target;

            synchronized (getCurrentLocation().getLock()) {
                if (getCurrentLocation().getHerbivores().isEmpty()) {
                    break;
                }
                target = getCurrentLocation().getHerbivores().getFirst();
                if(target.getChanceToBeEatenByDuck() == 0 || chance > target.getChanceToBeEatenByDuck()){
                    break;
                }
                target.die();
                setSaturation(Math.min(getSaturation() + target.getWeight(), getMaxSaturation()));
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String toString() {
        return "\uD83E\uDD86";
    }
}

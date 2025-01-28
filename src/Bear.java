import java.util.concurrent.ThreadLocalRandom;

public class Bear extends Predator{
    public Bear(Island island, Location location) {
        super(island, "bearWeight", "bearMovementSpeed", "bearMaxSaturation", location);
        super.setEatProperties("boa.bear", "bear.bear", "eagle.bear");
    }
    @Override
    public void reproduce() {
        if (!isAlive()) return;
        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getPredators().size() > 1 && getSaturation() == this.getMaxSaturation()) {
                getCurrentLocation().getPredators().add(new Bear(getCurrentIsland(), getCurrentLocation()));
                setSaturation(1);
                this.getCurrentIsland().increasePredatorsBorn(1);
            }
        }
    }
    @Override
    public void eat() {
        if (!isAlive()) return;
        while (getSaturation() < getMaxSaturation()) {
            int chance = ThreadLocalRandom.current().nextInt(1, 100);
            Herbivorous target;

            synchronized (getCurrentLocation().getLock()) {
                if (getCurrentLocation().getHerbivores().isEmpty()) {
                    break;
                }
                target = getCurrentLocation().getHerbivores().getFirst();
                if(target.getChanceToBeEatenByBear() == 0 || chance > target.getChanceToBeEatenByBear()){
                    break;
                }
                target.die();
                setSaturation(Math.min(getSaturation() + target.getWeight(), getMaxSaturation()));
            }
        }
        while (getSaturation() < getMaxSaturation()) {
            int chance = ThreadLocalRandom.current().nextInt(1, 100);
            Predator target;

            synchronized (getCurrentLocation().getLock()) {
                if (getCurrentLocation().getHerbivores().isEmpty()) {
                    break;
                }
                target = getCurrentLocation().getPredators().getFirst();
                if(target.getChanceToBeEatenByBear() == 0 || chance > target.getChanceToBeEatenByBear()){
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
        return "\uD83D\uDC3B";
    }
}

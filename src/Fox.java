import java.util.concurrent.ThreadLocalRandom;

public class Fox extends Predator {
    public Fox(Island island, Location location) {
        super(island, "foxWeight", "foxMovementSpeed", "foxMaxSaturation", location);
        super.setEatProperties("boa.fox", "bear.fox", "eagle.fox");
    }
    @Override
    public void reproduce() {
        if (!isAlive()) return;
        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getPredators().size() > 1 && getSaturation() == this.getMaxSaturation()) {
                getCurrentLocation().getPredators().add(new Fox(getCurrentIsland(), getCurrentLocation()));
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
                if(target.getChanceToBeEatenByFox() == 0 || chance > target.getChanceToBeEatenByFox()){
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
        return "\uD83E\uDD8A";
    }
}
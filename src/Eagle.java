import java.util.concurrent.ThreadLocalRandom;

public class Eagle extends Predator{
    public Eagle(Island island, Location location) {
        super(island, "eagleWeight", "eagleMovementSpeed", "eagleMaxSaturation", location);
        super.setEatProperties("boa.eagle", "bear.eagle", "eagle.eagle");
    }
    @Override
    public void reproduce() {
        if (!isAlive()) return;
        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getPredators().size() > 1 && getSaturation() == this.getMaxSaturation()) {
                getCurrentLocation().getPredators().add(new Eagle(getCurrentIsland(), getCurrentLocation()));
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
                if(target.getChanceToBeEatenByEagle() == 0 || chance > target.getChanceToBeEatenByEagle()){
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
                if(target.getChanceToBeEatenByEagle() == 0 || chance > target.getChanceToBeEatenByEagle()){
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
        return "\uD83E\uDD85";
    }
}

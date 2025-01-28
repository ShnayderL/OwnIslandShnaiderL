import java.util.concurrent.ThreadLocalRandom;

public class Boar extends Herbivorous {
    public Boar(Island island, Location location) {
        super(island, "boarWeight", "boarMovementSpeed", "boarMaxSaturation", location);
        super.setEatProperties("boa.boar", "bear.boar", "eagle.boar", "wolf.boar",
                "fox.boar", "mouse.boar", "boar.boar", "duck.boar");
    }
    @Override
    public void reproduce() {
        if (!isAlive()) return;

        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getHerbivores().size() > 1 && getSaturation() == this.getMaxSaturation()) {
                getCurrentLocation().getHerbivores().add(new Boar(getCurrentIsland(), getCurrentLocation()));
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
                if(target.getChanceToBeEatenByBoar() == 0 || chance > target.getChanceToBeEatenByBoar()){
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
        return "\uD83D\uDC10";
    }
}

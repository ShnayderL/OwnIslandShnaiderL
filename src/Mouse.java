import java.util.concurrent.ThreadLocalRandom;

public class Mouse extends Herbivorous {
    public Mouse(Island island, Location location) {
        super(island, "mouseWeight", "mouseMovementSpeed", "mouseMaxSaturation", location);
        super.setEatProperties("boa.mouse", "bear.mouse", "eagle.mouse",
                "wolf.mouse", "fox.mouse", "mouse.mouse", "boar.mouse", "duck.mouse");
    }
    @Override
    public void reproduce() {
        if (!isAlive()) return;

        synchronized (getCurrentLocation().getLock()) {
            if (getCurrentLocation().getHerbivores().size() > 1 && getSaturation() == this.getMaxSaturation()) {
                getCurrentLocation().getHerbivores().add(new Mouse(getCurrentIsland(), getCurrentLocation()));
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
                if(target.getChanceToBeEatenByMouse() == 0 || chance > target.getChanceToBeEatenByMouse()){
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
        return "\uD83D\uDC01";
    }
}

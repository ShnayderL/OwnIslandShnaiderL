import java.util.concurrent.TimeUnit;

public class Simulation {
    public static void main(String[] args) {
        Island island = new Island(100, 20);
        island.simulate(25, TimeUnit.SECONDS, 4);
    }
}

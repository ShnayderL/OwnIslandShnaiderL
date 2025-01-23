import java.util.HashMap;
import java.util.Map;

public class Island {
    private final int ISLAND_WIDTH;
    private final int ISLAND_HEIGHT;
    private final Map<Point, Location> locations;
    private int predatorsDied = 0;
    private int predatorsBorn = 0;
    private int herbivoresBorn = 0;
    private int herbivoresDied = 0;

    public Island(int islandWidth, int islandHeight) {
        ISLAND_WIDTH = islandWidth;
        ISLAND_HEIGHT = islandHeight;
        this.locations = new HashMap<>();
    }

    public void addLocation(int x, int y, Location location) {
        locations.put(new Point(x, y), location);
    }

    public int getISLAND_WIDTH() {
        return ISLAND_WIDTH;
    }

    public int getISLAND_HEIGHT() {
        return ISLAND_HEIGHT;
    }

    public void increasePredatorsDied(int predatorsDied) {
        this.predatorsDied += predatorsDied;
    }

    public void increasePredatorsBorn(int predatorsBorn) {
        this.predatorsBorn += predatorsBorn;
    }
    public void increaseHerbivoresDied(int herbivoresDied) {
        this.herbivoresDied += herbivoresDied;
    }

    public void increaseHerbivoresBorn(int herbivoresBorn) {
        this.herbivoresBorn += herbivoresBorn;
    }


    public Location getLocation(Point point) {
        Location location = locations.get(point);
        if (location == null) {
            throw new IllegalArgumentException("No location found at point: " + point);
        }
        return location;
    }

    private void overpopulation() {
        for (Location location : locations.values()) {
            synchronized (location.getLock()) {
                // Видаляємо надлишок рослин
                while (location.getPlants().size() > 100) { // Поріг кількості
                    location.getPlants().removeFirst().die();
                }
                // Видаляємо надлишок травоїдних
                while (location.getHerbivores().size() > 50) { // Поріг кількості
                    location.getHerbivores().removeFirst().die();
                }
                // Видаляємо надлишок хижаків
                while (location.getPredators().size() > 20) { // Поріг кількості
                    location.getPredators().removeFirst().die();
                }
            }
        }
    }

    public String showPopulation() {
        int herbivores = 0;
        int plants = 0;
        int predators = 0;
        for (Point point : locations.keySet()) {
            plants += locations.get(point).getPlants().size();
            herbivores += locations.get(point).getHerbivores().size();
            predators += locations.get(point).getPredators().size();
        }
        return "Острів налічує: " + getTotalPopulation()
                + " тварин. З яких Хижаків: " + predators + ", Травоїдних: " + herbivores + ", Рослин: " + plants + ".\n"
                + "Хижаків померло: " + predatorsDied + ", народилось: " + predatorsBorn + ".\n"
                + "Травоїдних померло " + herbivoresDied + ", народилось: " + herbivoresBorn;
    }

    public int getTotalPopulation() {
        int sum = 0;
        for (Point point : locations.keySet()) {
            sum += locations.get(point).getPlants().size();
            sum += locations.get(point).getHerbivores().size();
            sum += locations.get(point).getPredators().size();
        }
        if (sum > 10_000_000) {
            overpopulation();
        }
        return sum;
    }

    public Map<Point, Location> getLocations() {
        return locations;
    }
}
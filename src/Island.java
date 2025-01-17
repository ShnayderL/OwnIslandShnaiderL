import java.awt.Point; // Використовуємо клас Point для координат
import java.util.HashMap;
import java.util.Map;

public class Island {
    private final int ISLAND_WIDTH;
    private final int ISLAND_HEIGHT;
    private final Map<Point, Location> locations;

    public static void main(String[] args) {
        System.out.println(new Sheep(new Island(100, 20)).getMaxSaturation());
    }
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

    public Location getLocation(int x, int y) {
        return locations.get(new Point(x, y));
    }

    public Map<Point, Location> getLocations() {
        return locations;
    }
}
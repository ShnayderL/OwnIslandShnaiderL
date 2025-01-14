import java.awt.Point; // Використовуємо клас Point для координат
import java.util.HashMap;
import java.util.Map;

public class Island {
    public static void main(String[] args) {
        System.out.println(new Sheep(new Island()));
    }

    private final Map<Point, Location> locations;

    public Island() {
        this.locations = new HashMap<>();
    }

    public void addLocation(int x, int y, Location location) {
        locations.put(new Point(x, y), location);
    }

    public Location getLocation(int x, int y) {
        return locations.get(new Point(x, y));
    }

    public Map<Point, Location> getLocations() {
        return locations;
    }
}
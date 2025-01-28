import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Island {
    private final int ISLAND_WIDTH;
    private final int ISLAND_HEIGHT;
    private final Map<Point, Location> locations;
    private int predatorsDied = 0;
    private int predatorsBorn = 0;
    private int herbivoresBorn = 0;
    private int herbivoresDied = 0;
    private String initialStats = "";

    public Island(int islandWidth, int islandHeight) {
        ISLAND_WIDTH = islandWidth;
        ISLAND_HEIGHT = islandHeight;
        this.locations = new HashMap<>();
    }
    public void simulate(int days, TimeUnit unit, int dayLength) {
        System.out.println("Запускаємо симуляцію на "+ days +" днів, острова розміром "
                + getISLAND_WIDTH() + " на " + getISLAND_HEIGHT()
                + " локацій. \nОстрів містить таких тварин як: \n"
                + "Вовки, Удави, Лисиці, Ведмеді, Орли, Коні, Олені, Кролики, Миші, Кози, Вівці, Кабани, Буйволи, Качки, Гусениці");
        initializeIsland();

        ExecutorService executor = Executors.newFixedThreadPool(6);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        List<Region> regions = divideIslandIntoRegions();
        long simulationDuration = unit.toSeconds(days * dayLength);

        List<Future<?>> tasks = new ArrayList<>();
        for (Region region : regions) {
            tasks.add(executor.submit(() -> simulateRegion(region, simulationDuration)));
        }

        final int[] currentDay = {1};
        final long startTime = System.currentTimeMillis();
        Runnable statsTask = () -> {
            if (System.currentTimeMillis() - startTime >= simulationDuration * 1000) {
                System.out.println("Simulation has ended, stopping stats task.");
                scheduler.shutdown();
                return; // Stop the task
            }

            System.out.println("День " + currentDay[0]);
            System.out.println(showPopulation());
            currentDay[0]++;
        };

        scheduler.scheduleAtFixedRate(statsTask, 0, dayLength, unit);
        stopSimulation(executor, scheduler, simulationDuration);
    }


    private void stopSimulation(ExecutorService executor, ScheduledExecutorService scheduler, long simulationDuration) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(simulationDuration, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(0, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
        System.out.println("\nWaiting simulation to stop...");
        for (Point point : locations.keySet()) {
            locations.get(point).shutdown();
        }
        System.out.println("Simulation completed!");
    }

    public int getISLAND_WIDTH() {
        return ISLAND_WIDTH;
    }

    public int getISLAND_HEIGHT() {
        return ISLAND_HEIGHT;
    }
    private void simulateRegion(Region region, long durationSeconds) {
        long endTime = System.currentTimeMillis() + durationSeconds * 1000;
        List<Location> regionLocations = getLocationsForRegion(region);

        while (System.currentTimeMillis() < endTime) {
            for (Location location : regionLocations) {
                location.simulateLife(); // Імітація життя в локації
            }
        }
    }

    private List<Region> divideIslandIntoRegions() {
        List<Region> regions = new ArrayList<>();
        int midX = ISLAND_WIDTH / 2;
        int midY = ISLAND_HEIGHT / 2;

        regions.add(new Region(0, midX, 0, midY)); // Верхній лівий
        regions.add(new Region(midX, ISLAND_WIDTH, 0, midY)); // Верхній правий
        regions.add(new Region(midX, ISLAND_WIDTH, midY, ISLAND_HEIGHT)); // Нижній правий
        regions.add(new Region(0, midX, midY, ISLAND_HEIGHT)); // Нижній лівий

        return regions;
    }

    private List<Location> getLocationsForRegion(Region region) {
        List<Location> regionLocations = new ArrayList<>();
        for (int x = region.startX; x < region.endX; x++) {
            for (int y = region.startY; y < region.endY; y++) {
                Point point = new Point(x, y);
                if (locations.containsKey(point)) {
                    regionLocations.add(locations.get(point));
                }
            }
        }
        return regionLocations;
    }
    private void initializeIsland() {
        Location location;
        for (int x = 0; x < ISLAND_WIDTH; x++) {
            for (int y = 0; y < ISLAND_HEIGHT; y++) {
                location = new Location(this, new Point(x, y));
                addLocation(x, y, location);
                location.init();
            }
        }
        System.out.println("Острів заселено " + getTotalPopulation() + " тваринами. \n");
    }
    private void addLocation(int x, int y, Location location) {
        locations.put(new Point(x, y), location);
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
    private String showPopulation() {
        int herbivores = 0;
        int plants = 0;
        int predators = 0;
        for (Point point : locations.keySet()) {
            plants += locations.get(point).getPlants().size();
            herbivores += locations.get(point).getHerbivores().size();
            predators += locations.get(point).getPredators().size();
        }
        int newPredatorsBorn = predatorsBorn;
        predatorsBorn = 0;
        int newPredatorsDied = predatorsDied;
        predatorsDied = 0;
        int newHerbivoresDied = herbivoresDied;
        herbivoresDied = 0;
        int newherbivoresBorn = herbivoresBorn;
        herbivoresBorn = 0;
        return "Острів налічує: " + getTotalPopulation()
                + " істот. З яких Хижаків: " + predators + ", Травоїдних: " + herbivores + ", Рослин: " + plants + ".\n"
                + "Хижаків померло: " + newPredatorsDied + ", народилось: " + newPredatorsBorn + ".\n"
                + "Травоїдних померло " + newHerbivoresDied + ", народилось: " + newherbivoresBorn + "\n";
    }

    public int getTotalPopulation() {
        int sum = 0;
        for (Point point : locations.keySet()) {
            sum += locations.get(point).getPlants().size();
            sum += locations.get(point).getHerbivores().size();
            sum += locations.get(point).getPredators().size();
        }
        return sum;
    }
    public Map<Point, Location> getLocations() {
        return locations;
    }
}

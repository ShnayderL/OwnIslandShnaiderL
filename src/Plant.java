public class Plant implements Creature{
    private int x;
    private int y;
    private boolean eaten = false;
    private final Island currentIsland;

    public Plant(Island island){
        this.currentIsland = island;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if(x > 0){
            this.x = x;
        } else{
            throw new IllegalArgumentException("x must be greater than zero");
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if(y > 0){
            this.y = y;
        } else{
            throw new IllegalArgumentException("y must be greater than zero");
        }
    }

    public void setEaten(boolean eat){
        this.eaten = true;
    }
    public boolean isEaten(){
        return this.eaten;
    }
    @Override
    public void reproduce() {
        if(currentIsland.getLocation(this.getX(), this.getY()).getPlants().size() > 1){
            currentIsland.getLocation(this.getX(), this.getY()).getPlants().add(new Plant(currentIsland));
        }
    }

    @Override
    public void die() {
        currentIsland.getLocation(this.getX(), this.getY()).getPlants().remove(this);
    }
}

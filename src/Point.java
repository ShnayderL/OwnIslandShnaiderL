import java.security.InvalidParameterException;
import java.util.Objects;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y){
        if(x > 0 && y > 0){
            this.x = x;
            this.y = y;
        } else if(x < 0){
            throw new InvalidParameterException("x must be grater than zero");
        } else if(y < 0){
            throw new InvalidParameterException("y must be grater than zero");
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point that = (Point) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

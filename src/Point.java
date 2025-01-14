import javax.naming.directory.InvalidAttributesException;
import java.security.InvalidParameterException;

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
}

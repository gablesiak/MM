package project;

import java.io.Serializable;

public class DisplayPoint implements Serializable {

    public int x;
    public int y;

    public DisplayPoint(int a, int b) {
        this.x = a;
        this.y = b;
    }

    @Override
    public String toString() {
        return "Pkt{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DisplayPoint displayPoint = (DisplayPoint) o;

        if (x != displayPoint.x) return false;
        return y == displayPoint.y;

    }

    @Override
    public int hashCode() {
        return x + y;
    }
}

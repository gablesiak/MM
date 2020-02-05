package project;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

public class CellColor implements Serializable {
    private static final CellColor BLACK = new CellColor(0, 0, 0);
    private static final CellColor WHITE = new CellColor(255, 255, 255);
    public int R = 128;
    public int G = 128;
    public int B = 128;

    public CellColor() {

    }

    public CellColor(int r, int g, int b) {
        R = r;
        G = g;
        B = b;
    }

    public static CellColor White() {
        return WHITE;
    }

    public static CellColor Black() {
        return BLACK;
    }

    @Override
    public String toString() {
        return "Color{" +
                "R=" + R +
                ", G=" + G +
                ", B=" + B +
                "}";
    }

    public static CellColor create() {
        Random r = new Random();

        CellColor c = new CellColor();
        c.R = r.nextInt(250);
        c.G = r.nextInt(250);
        c.B = r.nextInt(250);
        return c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CellColor)) return false;
        CellColor cellColor = (CellColor) o;
        return R == cellColor.R &&
                G == cellColor.G &&
                B == cellColor.B;
    }

    @Override
    public int hashCode() {
        return R | G << 8 | B << 16;
    }
}

package project;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class GridMap {
    private final int w;
    private final int h;

    private CellColor[][] cellColor;
    private final Set<CellColor> colors = new HashSet<>();

    public GridMap(int w, int h) {
        this.w = w;
        this.h = h;
        this.cellColor = new CellColor[this.w][this.h];
    }

    public void initialize() {
        for (int i = 0; i < Core.Config.points; i++) {
            DisplayPoint p = randomizePkt();
            CellColor fieldColor = randomizeColor();
            colors.add(fieldColor);
            cellColor[p.x][p.y] = fieldColor;
        }
    }

    private CellColor randomizeColor() {
        CellColor cell = CellColor.create();

        if (colors.contains(cell)) {
            return randomizeColor();
        }
        return cell;
    }

    public DisplayPoint randomizePkt() {
        Random r = new Random();

        int x = r.nextInt(this.w);
        int y = r.nextInt(this.h);

        if (cellColor[x][y] != null) {
            return randomizePkt();
        }

        return new DisplayPoint(x, y);
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public boolean isWhite(int x, int y) {
        return cellColor[x][y] == null || Objects.equals(cellColor[x][y], CellColor.White());
    }

    public CellColor find(int x, int y) {
        if (x < 0 || x >= this.w) {
            return null;
        }

        if (y < 0 || y >= this.h) {
            return null;
        }

        return cellColor[x][y];
    }

    public void assign(int x, int y, CellColor me) {
        if (x < 0 || x >= this.w) {
            return;
        }

        if (y < 0 || y >= this.h) {
            return;
        }

        colors.add(me);
        this.cellColor[x][y] = me;
    }

    public void clear() {
        this.colors.clear();
        clear(new HashSet<>());
    }

    public void clear(Set<CellColor> restrictedAreas) {
        CellColor[][] newMap = new CellColor[this.w][this.h];

        System.out.println("Clear grid");

        for (int x = 0; x < getW(); x++) {
            for (int y = 0; y < getH(); y++) {
                if (this.cellColor[x][y] != null && restrictedAreas.contains(this.cellColor[x][y]))
                    newMap[x][y] = this.cellColor[x][y];
            }
        }

        this.cellColor = newMap;
    }
}

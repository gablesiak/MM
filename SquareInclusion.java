package project.inclu;

import project.CellColor;
import project.Core;
import project.DisplayPoint;
import project.GridMap;

public class SquareInclusion implements Inclusion {
    @Override
    public void make(GridMap gridMap, DisplayPoint displayPoint) {
        DisplayPoint pkt = displayPoint;

        int ref = Core.Config.sizeInclu / 2 + Core.Config.sizeInclu % 2;

        for (int x = -ref; x <= ref; x++) {
            for (int y = -ref; y <= ref; y++) {
                gridMap.assign(
                        x + pkt.x,
                        y + pkt.y,
                        CellColor.Black()
                );
            }
        }
    }
}

package project.inclu;

import project.CellColor;
import project.Core;
import project.DisplayPoint;
import project.GridMap;

public class RoundInclusion implements project.inclu.Inclusion {
    @Override
    public void make(GridMap gridMap, DisplayPoint displayPoint) {
        DisplayPoint pkt = displayPoint;

        for (int x = -Core.Config.sizeInclu; x <= Core.Config.sizeInclu; x++) {
            for (int y = -Core.Config.sizeInclu; y <= Core.Config.sizeInclu; y++) {
                if (Math.sqrt(y * y + x * x) < Core.Config.sizeInclu) {
                    gridMap.assign(x + pkt.x,
                            y + pkt.y,
                            CellColor.Black());
                }
            }
        }
    }
}

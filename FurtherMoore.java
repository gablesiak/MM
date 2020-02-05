package project.alg;

import project.CellColor;
import project.DisplayPoint;
import project.GridMap;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FurtherMoore implements GrowthMethod {
    @Override
    public int growth(GridMap gridMap, Set<CellColor> restrictedAreas) {
        int changed = 0;

        HashMap<DisplayPoint, CellColor> pkts = new HashMap<>();

        for (int x = 0; x < gridMap.getW(); x++) {
            for (int y = 0; y < gridMap.getH(); y++) {
                if (!gridMap.isWhite(x, y)) {
                    continue;
                }
                changed++;

                CellColor upLeft = gridMap.find(x - 1, y - 1);
                CellColor upRight = gridMap.find(x + 1, y - 1);

                CellColor downLeft = gridMap.find(x - 1, y + 1);
                CellColor downRight = gridMap.find(x + 1, y + 1);

                List<CellColor> toCheck = new ArrayList<>();

                if (!restrictedAreas.contains(upLeft) && upLeft != null) {
                    toCheck.add(upLeft);
                }
                if (!restrictedAreas.contains(upRight) && upRight != null) {
                    toCheck.add(upRight);
                }
                if (!restrictedAreas.contains(downLeft) && downLeft != null) {
                    toCheck.add(downLeft);
                }
                if (!restrictedAreas.contains(downRight) && downRight != null) {
                    toCheck.add(downRight);
                }

                Map<CellColor, Long> groupped = toCheck.stream()
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

                Optional<Map.Entry<CellColor, Long>> max = groupped.entrySet().stream()
                        .max((key, value) -> value.getValue().intValue());

                if (max.isPresent()) {
                    pkts.put(new DisplayPoint(x, y), max.get().getKey());
                }
            }
        }

        pkts.forEach((pkt, cellColor) -> {
            gridMap.assign(pkt.x, pkt.y, cellColor);
        });

        return changed;
    }
}

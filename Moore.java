package project.alg;

import project.CellColor;
import project.DisplayPoint;
import project.GridMap;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Moore implements GrowthMethod {
    @Override
    public int growth(GridMap gridMap, Set<CellColor> restrictedAreas) {
        int toChange = 0;

        HashMap<DisplayPoint, CellColor> pkts = new HashMap<>();

        for (int x = 0; x < gridMap.getW(); x++) {
            for (int y = 0; y < gridMap.getH(); y++) {
                if (!gridMap.isWhite(x, y)) {
                    continue;
                }
                toChange++;
                CellColor up = gridMap.find(x, y - 1);
                CellColor upLeft = gridMap.find(x - 1, y - 1);
                CellColor upRight = gridMap.find(x + 1, y - 1);

                CellColor down = gridMap.find(x, y + 1);
                CellColor downLeft = gridMap.find(x - 1, y + 1);
                CellColor downRight = gridMap.find(x + 1, y + 1);

                CellColor left = gridMap.find(x - 1, y);
                CellColor right = gridMap.find(x + 1, y);


                List<CellColor> toCheck = new ArrayList<>();

                if (!restrictedAreas.contains(up) && up != null) {
                    toCheck.add(up);
                }
                if (!restrictedAreas.contains(down) && down != null) {
                    toCheck.add(down);
                }
                if (!restrictedAreas.contains(left) && left != null) {
                    toCheck.add(left);
                }
                if (!restrictedAreas.contains(right) && right != null) {
                    toCheck.add(right);
                }

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

        return toChange;
    }
}

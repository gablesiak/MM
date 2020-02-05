package project.alg;

import project.CellColor;
import project.GridMap;

import java.util.Set;

public interface GrowthMethod {
    int growth(GridMap gridMap, Set<CellColor> restrictedAreas);
}

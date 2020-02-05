package project;

import project.alg.GrowthMethod;

import java.util.Set;

public class GrainGrowth {
    private final GridMap gridMap;
    private final GrowthMethod method;

    public GrainGrowth(GridMap gridMap, GrowthMethod method) {
        this.gridMap = gridMap;
        this.method = method;
    }

    public boolean iterate(Set<CellColor> restrictedAreas) {
        return this.method.growth(this.gridMap, restrictedAreas) > 0;
    }
}

package dataschemer.plan;

import java.util.Set;

public class Assortment {
    private Style style;
    private Set<Demand> demand;

    public Assortment(Style style, Set<Demand> demand) {
        this.style = style;
        this.demand = demand;
    }

    public Style getStyle() {
        return style;
    }

    public Set<Demand> getDemand() {
        return demand;
    }
}

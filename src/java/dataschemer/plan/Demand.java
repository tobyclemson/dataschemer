package dataschemer.plan;

public class Demand {
    private String id;
    private Integer quantity;

    public Demand(String id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }
}

package dataschemer.plan;

public class Plan {
    private Assortment assortment;
    private Fulfillment fulfillment;

    public Plan(Assortment assortment, Fulfillment fulfillment) {
        this.assortment = assortment;
        this.fulfillment = fulfillment;
    }

    public Assortment getAssortment() {
        return assortment;
    }

    public Fulfillment getFulfillment() {
        return fulfillment;
    }
}

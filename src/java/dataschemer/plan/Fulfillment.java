package dataschemer.plan;

public class Fulfillment {
    private PlanningSeason planningSeason;
    private DistributionGroup distributionGroup;
    private ReceiptMonth receiptMonth;

    public Fulfillment(
            PlanningSeason planningSeason,
            DistributionGroup distributionGroup,
            ReceiptMonth receiptMonth) {
        this.planningSeason = planningSeason;
        this.distributionGroup = distributionGroup;
        this.receiptMonth = receiptMonth;
    }

    public PlanningSeason getPlanningSeason() {
        return planningSeason;
    }

    public DistributionGroup getDistributionGroup() {
        return distributionGroup;
    }

    public ReceiptMonth getReceiptMonth() {
        return receiptMonth;
    }
}

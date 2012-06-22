package dataschemer.plan;

public class PlanningSeason {
    private String name;
    private String year;

    public PlanningSeason(String name, String year) {
        this.name = name;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }
}

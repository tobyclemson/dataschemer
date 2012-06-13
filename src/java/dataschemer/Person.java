package dataschemer;

public class Person {
    private final Age age;
    private final Name name;

    public Person(Age age, Name name) {
        this.age = age;
        this.name = name;
    }

    public Age getAge() {
        return age;
    }

    public Name getName() {
        return name;
    }
}

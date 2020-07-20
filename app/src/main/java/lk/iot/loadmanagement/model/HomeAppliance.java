package lk.iot.loadmanagement.model;

public class HomeAppliance {


    private int id;
    private String name;

    public HomeAppliance(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public HomeAppliance() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HomeAppliance{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

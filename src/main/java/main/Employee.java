package main;

public class Employee {
    private int ID;
    private String title;
    private String name;
    private String phoneNumber;

    public Employee(int id, String title, String name, String phoneNumber) {
        ID = id;
        this.title = title;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getID() {
        return ID;
    }
}

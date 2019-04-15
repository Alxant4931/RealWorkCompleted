package ru.alxant.converter;

/**
 * Created by Alxant on 10.03.2018.
 */
public class Address {
    private String street;
    private String home;
    //private String housing; // корпус
    private String apartment;

    public Address(String street, String home, String apartment) {
        this.street = street;
        this.home = home;
        this.apartment = apartment;
    }

    public String getStreet() {
        return street;
    }

    public String getHome() {
        return home;
    }

    public String getApartment() {
        return apartment;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", home='" + home + '\'' +
                ", apartment='" + apartment + '\'' +
                '}';
    }
}


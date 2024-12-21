package services;
import entities.*;
import enums.*;

import java.io.Serializable;
import java.util.Date;

public class OldService implements Serializable {

    public Coordinates createCoordinates(double x, long y) {
        Coordinates coordinates = new Coordinates();
        coordinates.setX(x);
        coordinates.setY(y);
        return coordinates;
    }

    public Address createAddress(String zipCode, Location town) {
        Address address = new Address();
        address.setZipCode(zipCode);
        address.setTown(town);
        return address;
    }

    public Organization createOrganization(String name,Address address, Float annualTurnover, int employeesCount, String fullName, Double rating) {
        Organization organization = new Organization();
        organization.setName(name);
        organization.setOfficialAddress(address);
        organization.setAnnualTurnover(annualTurnover);
        organization.setEmployeesCount(employeesCount);
        organization.setFullName(fullName);
        organization.setRating(rating);
        return organization;
    }

    public Location createLocation(Integer x, Integer y, float z, String name) {
        Location location = new Location();
        location.setX(x);
        location.setY(y);
        location.setZ(z);
        location.setName(name);
        return location;
    }

    public Person createPerson(String name, Color eyeColor, Color hairColor, Location location, float height, Float weight, String passportID) {
        Person person = new Person();
        person.setName(name);
        person.setEyeColor(eyeColor);
        person.setHairColor(hairColor);
        person.setLocation(location);
        person.setHeight(height);
        person.setWeight(weight);
        person.setPassportID(passportID);
        return person;
    }

    public Product createProduct(String name, Coordinates coordinates,UnitOfMeasure unitOfMeasure, Organization manufacturer, int price, int manufactureCost, float rating, String partNumber, Person owner) {
        Product product = new Product();
        product.setName(name);
        product.setCoordinates(coordinates);
        product.setCreationDate(new Date()); // Дата создания всегда автоматически устанавливается
        product.setUnitOfMeasure(unitOfMeasure);
        product.setUnitOfMeasure(null); // Это может быть null
        product.setManufacturer(manufacturer);
        product.setPrice(price);
        product.setManufactureCost(manufactureCost);
        product.setRating(rating);
        product.setPartNumber(partNumber);
        product.setOwner(owner);
        return product;
    }
}


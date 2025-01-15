// PersonBean.java
package beans;

import entities.Person;
import entities.Location;
import enums.Color;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import services.PersonService;
import services.LocationService;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named
@RequestScoped
public class PersonBean implements Serializable {
    private Person person = new Person();
    private Long selectedLocationId;
    private String message;
    private String messageStyle;

    @Inject
    private PersonService personService;

    @Inject
    private LocationService locationService;

    private List<Location> existingLocations;

    public void loadLocations() {
        this.existingLocations = locationService.findAll();
    }

    public String savePerson() {
        try {
            person.setLocation(locationService.findById(selectedLocationId));
            personService.save(person);
            this.message = "Объект Person успешно сохранён!";
            this.messageStyle = "text-success";
        } catch (Exception e) {
            this.message = "Ошибка при сохранении объекта Person: ";
            this.messageStyle = "text-danger";
        }
        return null;
    }

    // Геттеры и сеттеры
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<Color> getColorOptions() {
        return Arrays.asList(Color.values());
    }

    public List<Location> getExistingLocations() {
        if (existingLocations == null) {
            loadLocations();
        }
        return existingLocations;
    }

    public void setExistingLocations(List<Location> existingLocations) {
        this.existingLocations = existingLocations;
    }

    public Long getSelectedLocationId() {
        return selectedLocationId;
    }

    public void setSelectedLocationId(Long selectedLocationId) {
        this.selectedLocationId = selectedLocationId;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageStyle() {
        return messageStyle;
    }
}

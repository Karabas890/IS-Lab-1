package beans;

import entities.Person;
import enums.Color;
import entities.Location;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import services.PersonService;
import services.LocationService;

import java.util.Arrays;
import java.util.List;

@Named
@RequestScoped
public class PersonBean {
    private Person person = new Person();

    @Inject
    private PersonService personService;

    @Inject
    private LocationService locationService;

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
        return locationService.findAll();
    }

    public String savePerson() {
        try {
            personService.save(person);
            System.out.println("Объект Person успешно сохранён: " + person.getName());
            return "/createProduct.xhtml?faces-redirect=true"; // Возврат на страницу создания продукта
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении объекта Person: " + e.getMessage());
            return null; // Остаёмся на текущей странице при ошибке
        }
    }
}


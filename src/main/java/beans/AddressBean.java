package beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import entities.Address;
import entities.Location;
import services.AddressService;
import services.LocationService;
import java.io.Serializable;
import java.util.List;

@Named("addressBean")
@SessionScoped
public class AddressBean implements Serializable {
    private Address address = new Address(); // Новый адрес для создания
    private List<Location> existingLocations; // Список доступных локаций

    @Inject
    private AddressService addressService;

    @Inject
    private LocationService locationService;

    public AddressBean() {
        // Конструктор по умолчанию
    }

    /**
     * Метод для сохранения нового адреса.
     *
     * @return Переход на страницу списка адресов или другую целевую страницу
     */
    public String saveAddress() {
        try {
            addressService.save(address);
            address = new Address(); // Сброс формы
            return "createOrganization.xhtml?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Оставаться на той же странице в случае ошибки
        }
    }

    /**
     * Метод для загрузки существующих локаций.
     */
    public void loadLocations() {
        this.existingLocations = locationService.findAll();
    }
    // Метод для получения адреса по ID

    // Геттеры и сеттеры

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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
}


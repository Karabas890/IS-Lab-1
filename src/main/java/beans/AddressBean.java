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
    private Long selectedLocationId = 1L; // ID выбранной локации
    private String message; // Сообщение для пользователя
    private boolean success; // Флаг успеха операции

    @Inject
    private AddressService addressService;

    @Inject
    private LocationService locationService;

    public AddressBean() {
        // Конструктор по умолчанию
    }

    public String saveAddress() {
        try {
            addressService.save(address); // Сохраняем адрес
            this.message = "Адрес успешно сохранен!";
            this.success = true;
            System.out.println("Address Saved: " + address.getId());
        } catch (Exception e) {
            this.message = "Ошибка при сохранении адреса: " + e.getMessage();
            this.success = false;
            e.printStackTrace();
        }
        return null; // Оставаться на текущей странице
    }

    public void loadLocations() {
        this.existingLocations = locationService.findAll();
    }



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

    public Long getSelectedLocationId() {
        return selectedLocationId;
    }

    public void setSelectedLocationId(Long selectedLocationId) {
        this.selectedLocationId = selectedLocationId;
        this.address.setTown(locationService.findById(selectedLocationId));

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

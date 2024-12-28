package beans;

import entities.Location;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import services.LocationService;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class LocationBean implements Serializable {

    private Location location;
    private List<Location> existingLocations;

    @Inject
    private LocationService locationService;

    @PostConstruct
    public void init() {
        location = new Location(); // Создание нового объекта Location
        loadExistingLocations();   // Загрузка существующих локаций
    }

    public void saveLocation() {
        try {
            locationService.save(location); // Сохранение объекта через LocationService
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Успех", "Локация успешно создана."));
            resetForm(); // Сброс формы
            loadExistingLocations(); // Обновление списка существующих локаций
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Не удалось создать локацию: " + e.getMessage()));
        }
    }

    private void resetForm() {
        location = new Location(); // Создание новой пустой локации для следующего ввода
    }

    private void loadExistingLocations() {
        existingLocations = locationService.findAll(); // Загрузка всех существующих локаций
    }

    // Getters и Setters
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Location> getExistingLocations() {
        return existingLocations;
    }

    public void setExistingLocations(List<Location> existingLocations) {
        this.existingLocations = existingLocations;
    }
}

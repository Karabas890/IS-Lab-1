package beans;

import entities.Location;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import services.LocationService;

import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class LocationBean implements Serializable {

    private Location location;
    private List<Location> existingLocations;
    private String message;        // Сообщение для отображения
    private String messageStyle;   // CSS-класс для сообщения

    @Inject
    private LocationService locationService;

    @PostConstruct
    public void init() {
        location = new Location(); // Создание нового объекта Location
        loadExistingLocations();   // Загрузка существующих локаций
    }

    public String saveLocation() {
        try {
            System.out.println("Начало создания локации");
            locationService.save(location); // Сохранение объекта через LocationService
            System.out.println("Локация успешно сохранена");
            message = "Локация успешно создана.";  // Сообщение об успехе
            messageStyle = "alert-success";  // Стиль для успешного сообщения

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Успех", message));

            //resetForm(); // Сброс формы
            //loadExistingLocations(); // Обновление списка существующих локаций
            return null; // Остаёмся на текущей странице для отображения сообщения
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении локации: ");
            message = "Не удалось создать локацию: " + e.getMessage();  // Сообщение об ошибке
            messageStyle = "alert-danger";  // Стиль для сообщения об ошибке

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", message));
            return null; // Остаёмся на текущей странице для отображения ошибки
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

    public String getMessage() {
        return message;
    }

    public String getMessageStyle() {
        return messageStyle;
    }
}

package beans;
import entities.Coordinates;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import services.CoordinatesService;

import java.io.Serializable;

@Named
@RequestScoped
public class CoordinatesBean implements Serializable {
    private Coordinates coordinate = new Coordinates();
    private String message;        // Сообщение для отображения
    private String messageStyle;   // CSS-класс для сообщения

    @Inject
    private CoordinatesService coordinatesService;

    public Coordinates getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinates coordinate) {
        this.coordinate = coordinate;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageStyle() {
        return messageStyle;
    }

    public String saveCoordinate() {
        try {
            // Сохранение координат через сервис
            System.out.println("Начало создания координат");
            coordinatesService.save(coordinate);
            System.out.println("Координаты успешно сохранены: X = " + coordinate.getX() + ", Y = " + coordinate.getY());

            // Устанавливаем сообщение об успехе
            message = "Координаты успешно сохранены!";
            messageStyle = "alert-success"; // Стиль Bootstrap для успешного сообщения
            return null; // Остаёмся на текущей странице для отображения сообщения
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении координат: " + e.getMessage());

            // Устанавливаем сообщение об ошибке
            message = "Ошибка при сохранении координат: " + e.getMessage();
            messageStyle = "alert-danger"; // Стиль Bootstrap для сообщения об ошибке
            return null; // Остаёмся на текущей странице для отображения ошибки
        }
    }
}

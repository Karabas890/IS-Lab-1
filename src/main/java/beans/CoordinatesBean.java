package beans;
import entities.Coordinates;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import services.CoordinatesService;

@Named
@RequestScoped
public class CoordinatesBean {
    private Coordinates coordinate = new Coordinates();

    @Inject
    private CoordinatesService coordinatesService;

    public Coordinates getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinates coordinate) {
        this.coordinate = coordinate;
    }

    public String saveCoordinate() {
        try {
            // Сохранение координат через сервис
            coordinatesService.save(coordinate);
            System.out.println("Координаты успешно сохранены: X = " + coordinate.getX() + ", Y = " + coordinate.getY());
            return "/createProduct.xhtml?faces-redirect=true"; // Возврат на страницу создания продукта
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении координат: " + e.getMessage());
            return null; // Остаёмся на текущей странице при ошибке
        }
    }
}

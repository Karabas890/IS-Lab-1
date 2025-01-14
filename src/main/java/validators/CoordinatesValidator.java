package validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import entities.Coordinates;

@FacesValidator(value = "coordinatesValidator", managed = true) // Регистрация валидатора
public class CoordinatesValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        System.out.println("Coordinates: " + value);

        // Проверка, является ли значение числом (для обоих координат x и y)
        if (value == null) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Значение не может быть пустым."
            ));
        }

        // Проверка для координаты X (должна быть double)
        if (component.getId().equals("xCoord")) {
            if (!(value instanceof Double)) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Координата X должна быть числом с плавающей запятой (double)."
                ));
            }

            Double coordinateX = (Double) value;
            if (coordinateX > 648) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'X' не может быть больше 648."
                ));
            }
        }

        // Проверка для координаты Y (должна быть long)
        if (component.getId().equals("yCoord")) {
            if (!(value instanceof Long)) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Координата Y должна быть целым числом (long)."
                ));
            }

            Long coordinateY = (Long) value;
            if (coordinateY < 0) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Y' не может быть отрицательным."
                ));
            }
        }
    }
}
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
        // Проверяем, является ли объект экземпляром Coordinates
        if (!(value instanceof Coordinates)) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Переданный объект не является допустимым экземпляром класса Coordinates."
            ));
        }

        Coordinates coordinates = (Coordinates) value;

        // Проверяем поле x
        if (coordinates.getX() > 648) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'X' не может быть больше 648."
            ));
        }

        // Проверяем поле y
        if (coordinates.getY() < 0) { // Условие на значение 'y' уточните, если есть ограничения
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'Y' не может быть отрицательным."
            ));
        }
    }
}

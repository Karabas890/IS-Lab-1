package validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator(value = "locationValidator", managed = true)
public class LocationValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        // Получаем имя компонента, чтобы понять, какое поле мы валидируем
        String clientId = component.getClientId(context);

        // Проверка для координаты X
        if (clientId.contains(":x")) {
            if (value == null || !(value instanceof Integer)) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Координата X должна быть целым числом."
                ));
            }
        }

        // Проверка для координаты Y
        if (clientId.contains(":y")) {
            if (value == null || !(value instanceof Integer)) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Координата Y должна быть целым числом."
                ));
            }
        }

        // Проверка для координаты Z
        if (clientId.contains(":z")) {
            if (value == null || !(value instanceof Float)) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Координата Z должна быть числом с плавающей точкой."
                ));
            }
        }

        // Проверка для имени
        if (clientId.contains(":name")) {
            if (value == null || value.toString().trim().isEmpty()) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Название локации не может быть пустым."
                ));
            }
        }
    }
}

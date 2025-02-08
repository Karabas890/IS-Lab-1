package validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator(value = "addressValidator", managed = true) // Регистрация валидатора
public class AddressValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String clientId = component.getClientId(context);

        // Проверка для поля zipCode
        if (clientId.contains(":zipCode")) {
            if (value == null || value.toString().trim().isEmpty()) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Почтовый индекс' не может быть пустым."
                ));
            }


        }

        // Проверка для поля town (Location ID)
        if (clientId.contains(":town")) {
            if (value == null) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Город' не может быть пустым."
                ));
            }

            // Проверка на наличие ID локации (Если town это объект, то проверяем его ID)
            Long locationId = (Long) value;
            if (locationId == null || locationId <= 0) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Выберите валидный город."
                ));
            }
        }
    }
}

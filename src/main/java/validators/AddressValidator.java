package validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import entities.Address;

@FacesValidator(value = "addressValidator", managed = true) // Регистрация валидатора
public class AddressValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        // Проверяем, является ли объект экземпляром Address
        if (!(value instanceof Address)) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Переданный объект не является допустимым адресом."
            ));
        }

        Address address = (Address) value;

        // Проверяем zipCode
        if (address.getZipCode() == null || address.getZipCode().trim().isEmpty()) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'Почтовый индекс' не может быть пустым."
            ));
        }

        // Проверяем town (если требуется более сложная логика, можно добавить её)
        if (address.getTown() == null) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'Город' не может быть пустым."
            ));
        }
    }
}

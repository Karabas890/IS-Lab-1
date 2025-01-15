package validators;

import entities.Product;
import entities.Coordinates;
import entities.Organization;
import entities.Person;
import entities.User;
import enums.UnitOfMeasure;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator(value = "productValidator", managed = true)
public class ProductValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String clientId = component.getClientId(context);

        // Check the name field
        if (clientId.contains(":name")) {
            if (value == null || value.toString().trim().isEmpty()) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Название продукта' не может быть пустым."
                ));
            }
        }

        // Check coordinates field
        if (clientId.contains(":coordinates")) {
            if (value == null) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Координаты' не может быть пустым."
                ));
            }
        }

        // Check creationDate field
        if (clientId.contains(":creationDate")) {
            if (value == null) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Дата создания' не может быть пустым."
                ));
            }
        }

        // Check unitOfMeasure field
        if (clientId.contains(":unitOfMeasure")) {
            if (value == null) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Единица измерения' не может быть пустым."
                ));
            }

            try {
                UnitOfMeasure unitOfMeasure = (UnitOfMeasure) value;
                if (unitOfMeasure == null) {
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Ошибка валидации",
                            "Неверная единица измерения."
                    ));
                }
            } catch (Exception e) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Неверное значение для единицы измерения."
                ));
            }
        }

        // Check manufacturer field
        if (clientId.contains(":manufacturer")) {
            if (value == null) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Производитель' не может быть пустым."
                ));
            }
        }

        // Check price field
        if (clientId.contains(":price")) {
            if (value == null || ((Integer) value <= 0)) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Цена' должно быть больше 0."
                ));
            }
        }

        // Check manufactureCost field
        if (clientId.contains(":manufactureCost")) {
            if (value == null || ((Integer) value <= 0)) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Себестоимость' должно быть больше 0."
                ));
            }
        }

        // Check rating field
        if (clientId.contains(":rating")) {
            if (value == null || ((Float) value <= 0)) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Рейтинг' должно быть больше 0."
                ));
            }
        }

        // Check partNumber field
        if (clientId.contains(":partNumber")) {
            if (value != null && value.toString().length() > 67) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Длина поля 'Номер детали' не может превышать 67 символов."
                ));
            }
        }

        // Check owner field
        if (clientId.contains(":owner")) {
            if (value == null) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Владелец' не может быть пустым."
                ));
            }
        }

        // Check user field
        if (clientId.contains(":user")) {
            User user = (User) value;
            if (user == null) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Пользователь' не может быть пустым."
                ));
            }
        }
    }
}

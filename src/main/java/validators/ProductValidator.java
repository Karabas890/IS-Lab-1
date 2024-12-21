package validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import entities.Product;

@FacesValidator(value = "productValidator", managed = true) // Регистрация валидатора
public class ProductValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        // Проверяем, является ли объект экземпляром Product
        if (!(value instanceof Product)) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Переданный объект не является допустимым экземпляром класса Product."
            ));
        }

        Product product = (Product) value;

        // Проверка поля id
        if (product.getId() <= 0) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'id' должно быть больше 0."
            ));
        }

        // Проверка поля name
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'name' не может быть пустым или null."
            ));
        }

        // Проверка поля coordinates
        if (product.getCoordinates() == null) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'coordinates' не может быть null."
            ));
        }

        // Проверка поля creationDate
        if (product.getCreationDate() == null) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'creationDate' не может быть null."
            ));
        }

        // Проверка поля price
        if (product.getPrice() <= 0) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'price' должно быть больше 0."
            ));
        }

        // Проверка поля rating
        if (product.getRating() <= 0) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'rating' должно быть больше 0."
            ));
        }

        // Проверка поля partNumber
        if (product.getPartNumber() != null && product.getPartNumber().length() > 67) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Длина строки в поле 'partNumber' не должна превышать 67 символов."
            ));
        }

        // Проверка поля owner
        if (product.getOwner() == null) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'owner' не может быть null."
            ));
        }
    }
}

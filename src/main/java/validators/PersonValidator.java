package validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import entities.Person;

@FacesValidator(value = "personValidator", managed = true) // Регистрация валидатора
public class PersonValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        // Проверяем, является ли объект экземпляром Person
        if (!(value instanceof Person)) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Переданный объект не является допустимым экземпляром класса Person."
            ));
        }

        Person person = (Person) value;

        // Проверяем поле name
        if (person.getName() == null || person.getName().trim().isEmpty()) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'Имя' не может быть пустым."
            ));
        }

        // Проверяем поле location
        if (person.getLocation() == null) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'Локация' не может быть пустым."
            ));
        }

        // Проверяем поле height
        if (person.getHeight() <= 0) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'Рост' должно быть больше 0."
            ));
        }

        // Проверяем поле weight (если указано)
        if (person.getWeight() != null && person.getWeight() <= 0) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'Вес' должно быть больше 0."
            ));
        }

        // Проверяем поле passportID
        String passportID = person.getPassportID();
        if (passportID == null || passportID.trim().isEmpty()) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'Паспортный ID' не может быть пустым."
            ));
        }
        if (passportID.length() < 6 || passportID.length() > 33) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'Паспортный ID' должно содержать от 6 до 33 символов."
            ));
        }

        // Уникальность passportID должна проверяться в другом месте (например, в базе данных)
    }
}

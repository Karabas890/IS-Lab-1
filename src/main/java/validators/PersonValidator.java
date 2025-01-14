package validators;

import entities.Person;
import enums.Color;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator(value = "personValidator", managed = true) // Регистрация валидатора
public class PersonValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String clientId = component.getClientId(context);

        // Проверка для поля name (имя)
        if (clientId.contains(":name")) {
            if (value == null || value.toString().trim().isEmpty()) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Имя' не может быть пустым."
                ));
            }
        }

        // Проверка для поля eyeColor (Цвет глаз)
        if (clientId.contains(":eyeColor")) {
            if (value == null) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Цвет глаз' должно быть выбрано."
                ));
            }

            // Проверка, что выбрано допустимое значение из перечисления
            try {
                Color eyeColor = (Color) value;
                if (eyeColor == null) {
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Ошибка валидации",
                            "Неверный цвет глаз."
                    ));
                }
            } catch (Exception e) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Неверное значение для цвета глаз."
                ));
            }
        }

        // Проверка для поля hairColor (Цвет волос)
        if (clientId.contains(":hairColor")) {
            if (value == null) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Цвет волос' должно быть выбрано."
                ));
            }

            // Проверка, что выбрано допустимое значение из перечисления
            try {
                Color hairColor = (Color) value;
                if (hairColor == null) {
                    throw new ValidatorException(new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Ошибка валидации",
                            "Неверный цвет волос."
                    ));
                }
            } catch (Exception e) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Неверное значение для цвета волос."
                ));
            }
        }

        // Проверка для поля height (рост)
        if (clientId.contains(":height")) {
            if (value == null || ((Float) value <= 0)) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Рост' должно быть положительным числом."
                ));
            }
        }

        // Проверка для поля weight (вес)
        if (clientId.contains(":weight")) {
            if (value != null && ((Float) value <= 0)) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Вес' должно быть положительным числом."
                ));
            }
        }

        // Проверка для поля passportID (ID паспорта)
        if (clientId.contains(":passportID")) {
            if (value == null || value.toString().trim().isEmpty()) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'ID паспорта' не может быть пустым."
                ));
            }

            String passportID = value.toString().trim();
            // Проверка на формат ID паспорта (например, длина 33 символа)
            if (passportID.length() > 33) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "ID паспорта не должен содержать более 33 символов."
                ));
            }
            if (passportID.length() < 6) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "ID паспорта должен содержать не менее 6 символов."
                ));
            }
        }

        // Проверка для поля location (местоположение)
        if (clientId.contains(":location")) {
            if (value == null) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Расположение' должно быть выбрано."
                ));
            }
        }
    }
}

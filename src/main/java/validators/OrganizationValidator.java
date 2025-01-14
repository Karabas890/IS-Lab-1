package validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import entities.Organization;
import enums.OrganizationType;

@FacesValidator(value = "organizationValidator", managed = true) // Регистрация валидатора
public class OrganizationValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String clientId = component.getClientId(context);

        // Проверка поля Name
        if (clientId.contains(":name")) {
            String name = (String) value;
            if (name == null || name.trim().isEmpty()) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Название организации' не может быть пустым."
                ));
            }
        }

        // Проверка поля Annual Turnover
        if (clientId.contains(":annualTurnover")) {
            Number annualTurnover = (Number) value;  // Используем Number, чтобы поддерживать как Float, так и Double
            if (annualTurnover == null) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Годовой оборот' должно быть указано."
                ));
            }
            if (annualTurnover.doubleValue() <= 0) {  // Используем doubleValue для обработки как Float, так и Double
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Годовой оборот' должно быть больше 0."
                ));
            }
        }

        // Проверка поля Employees Count
        if (clientId.contains(":employeesCount")) {
            Integer employeesCount = (Integer) value;
            if (employeesCount == null || employeesCount <= 0) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Количество сотрудников' должно быть больше 0."
                ));
            }
        }

        // Проверка поля Full Name
        if (clientId.contains(":fullName")) {
            String fullName = (String) value;
            if (fullName == null || fullName.trim().isEmpty()) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Полное название' не может быть пустым."
                ));
            }
        }

        // Проверка поля Rating
        if (clientId.contains(":rating")) {
            Number rating = (Number) value;  // Используем Number
            if (rating == null) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Рейтинг' должно быть указано."
                ));
            }
            if (rating.doubleValue() <= 0) {  // Используем doubleValue для корректной работы с типами
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Рейтинг' должно быть больше 0."
                ));
            }
        }

        // Проверка поля Type (Тип организации)
        if (clientId.contains(":type")) {
            OrganizationType type = (OrganizationType) value;
            if (type == null) {
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Ошибка валидации",
                        "Поле 'Тип организации' должно быть указано."
                ));
            }
        }
    }

}

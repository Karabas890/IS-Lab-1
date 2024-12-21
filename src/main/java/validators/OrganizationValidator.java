package validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import entities.Organization;

@FacesValidator(value = "organizationValidator", managed = true) // Регистрация валидатора
public class OrganizationValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        // Проверяем, является ли объект экземпляром Organization
        if (!(value instanceof Organization)) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Переданный объект не является допустимым экземпляром класса Organization."
            ));
        }

        Organization organization = (Organization) value;

        // Проверяем поле id
        if (organization.getId() <= 0) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'ID' должно быть больше 0."
            ));
        }

        // Проверяем поле name
        if (organization.getName() == null || organization.getName().trim().isEmpty()) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'Имя организации' не может быть пустым."
            ));
        }

        // Проверяем поле annualTurnover
        if (organization.getAnnualTurnover() == null || organization.getAnnualTurnover() <= 0) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'Годовой оборот' должно быть указано и быть больше 0."
            ));
        }

        // Проверяем поле employeesCount
        if (organization.getEmployeesCount() <= 0) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'Количество сотрудников' должно быть больше 0."
            ));
        }

        // Проверяем поле fullName
        if (organization.getFullName() == null || organization.getFullName().trim().isEmpty()) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'Полное название' не может быть пустым."
            ));
        }

        // Проверяем поле rating
        if (organization.getRating() == null || organization.getRating() <= 0) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Ошибка валидации",
                    "Поле 'Рейтинг' должно быть указано и быть больше 0."
            ));
        }

        // Поле type и поле officialAddress могут быть null, проверка не требуется
    }
}

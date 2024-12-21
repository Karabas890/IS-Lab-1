package validators;

import controller.ProductController;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;

@FacesValidator(value = "locationValidator", managed = true) // Регистрация валидатора
public class LocationValidator implements Validator {

    @Inject
    private ProductController productController;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Integer locationX = (Integer) component.getAttributes().get("locationX");
        Integer locationY = (Integer) component.getAttributes().get("locationY");
        String locationName = (String) component.getAttributes().get("locationName");

        // Проверяем, что все необходимые поля заполнены
        if (locationX == null || locationY == null || locationName == null || locationName.trim().isEmpty()) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Validation Error",
                    "Please enter both X and Y coordinates, and a valid name for the location."
            ));
        }
    }
}

package beans;
import entities.Organization;
import enums.OrganizationType;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import services.OrganizationService;

import java.util.Arrays;
import java.util.List;

@Named
@RequestScoped
public class OrganizationBean {
    private Organization organization = new Organization();

    @Inject
    private OrganizationService organizationService;

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<OrganizationType> getOrganizationTypes() {
        return Arrays.asList(OrganizationType.values());
    }

    public String saveOrganization() {
        try {
            organizationService.save(organization);
            System.out.println("Организация успешно сохранена: " + organization.getName());
            return "/createProduct.xhtml?faces-redirect=true"; // Возврат на страницу создания продукта
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении организации: " + e.getMessage());
            return null; // Остаёмся на текущей странице при ошибке
        }
    }
}

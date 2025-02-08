package beans;

import entities.Address;
import entities.Coordinates;
import entities.Organization;
import enums.OrganizationType;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import services.OrganizationService;
import services.AddressService;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named
@RequestScoped
public class OrganizationBean implements Serializable {
    private Organization organization = new Organization();
    private Long selectedAddressId = 1L;
    private String message;
    private String messageStyle;

    @Inject
    private OrganizationService organizationService;
    @Inject
    private AddressService addressService;

    private List<Address> existingAddresses;

    public void loadAddresses() {
        this.existingAddresses = addressService.findAll();
    }

    public String saveOrganization() {
        try {
            organizationService.save(organization);
            this.message = "Организация успешно сохранена!";
            this.messageStyle = "text-success";
            organization = new Organization();  // Создаем новый объект, чтобы не обновлялся старый
        } catch (Exception e) {
            this.message = "Ошибка при сохранении организации: " + e.getMessage();
            this.messageStyle = "text-danger";
        }
        return null;
    }

    // Геттеры и сеттеры
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<OrganizationType> getOrganizationTypes() {
        return Arrays.asList(OrganizationType.values());
    }

    public List<Address> getExistingAddresses() {
        if (existingAddresses == null) {
            loadAddresses();
        }
        return existingAddresses;
    }

    public void setExistingAddresses(List<Address> existingAddresses) {
        this.existingAddresses = existingAddresses;
    }

    public Long getSelectedAddressId() {
        return selectedAddressId;
    }

    public void setSelectedAddressId(Long selectedAddressId) {
        this.selectedAddressId = selectedAddressId;
        this.organization.setOfficialAddress(addressService.findById(selectedAddressId));
    }

    public String getMessage() {
        return message;
    }

    public String getMessageStyle() {
        return messageStyle;
    }
}

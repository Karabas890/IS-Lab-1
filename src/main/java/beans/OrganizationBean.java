package beans;

import entities.Address;
import entities.Location;
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
    private Long selectedAddressId = 1L; // По умолчанию 1, если адрес не выбран, Хранение ID выбранного адреса
    private Address organizationAddress;  // Ссылка на выбранный объект Address
    @Inject
    private OrganizationService organizationService;
    @Inject
    private AddressService addressService;

    // Поле для хранения существующих адресов
    private List<Address> existingAddresses;
    public void loadAddresses() {
        this.existingAddresses = addressService.findAll();
    }

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
        System.out.println("setSelectedAddressId: ");
        this.selectedAddressId = selectedAddressId;
        onAddressChanged(); // Обновление адреса после изменения ID
    }
    public Address getOrganizationAddress() {
        return organizationAddress;
    }

    public void setOrganizationAddress(Address organizationAddress) {
        this.organizationAddress = organizationAddress;
    }




    public String saveOrganization() {
        try {
            organizationService.save(organization);
            System.out.println("Organization Saved: " + organization.getName());
            return "/createProduct.xhtml?faces-redirect=true"; // Возврат на страницу создания продукта
        } catch (Exception e) {
            System.err.println("Error while saving organization: " + e.getMessage());
            return null; // Остаёмся на текущей странице при ошибке
        }
    }
    // Метод для получения объекта Address по ID
    public Address getAddressById(Long addressId) {
        if (addressId != null) {
            return addressService.findById(addressId);  // Не нужно преобразование в int
        }
        return null;
    }



    // Когда selectedAddressId изменяется, получаем объект Address
    public void onAddressChanged() {
        System.out.println("onAddressChanged: ");
        this.organizationAddress = getAddressById(selectedAddressId);  // Преобразуем ID в объект Address
        System.out.println("onAddressChanged: "+this.organizationAddress);

        if (organization != null) {
            organization.setOfficialAddress(this.organizationAddress); // Присваиваем адрес организации
        }
    }

}

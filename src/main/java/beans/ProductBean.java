package beans;

import entities.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.resource.spi.work.SecurityContext;
import lombok.Getter;
import lombok.Setter;
import services.*;
import enums.UnitOfMeasure;
import enums.OrganizationType;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Named
@SessionScoped
@Getter
@Setter
public class ProductBean implements Serializable {

    private List<Product> productList;
    private long productId;
    private String message;
    private String messageStyle;

    //@Inject
    //private SecurityContext securityContext; // Введение SecurityContext для получения текущего пользователя

    @Inject
    private ProductService productService;

    @Inject
    private CoordinatesService coordinatesService;

    @Inject
    private OrganizationService organizationService;

    @Inject
    private PersonService personService; // Добавляем сервис для Person
    @Inject
    private User currentUser; // Текущий пользователь, который создаёт продукт

    private Product product = new Product();
    @Inject
    private UserBean userBean;

    private boolean useExistingCoordinates;
    private boolean useExistingOrganization;
    private boolean useExistingPerson; // Переключатель для использования существующего Person


    private List<UnitOfMeasure> unitOfMeasureValues;
    private String nameFilter;
    private String partNumberFilter;
    private Comparator<Product> comparator = Comparator.comparing(Product::getId); // Метод сравнения по умолчанию
    private boolean sortOrderName = true;
    private boolean sortOrderDate = true;
    private boolean sortOrderPrice = true;
    private boolean sortOrderManufactureCost = true;
    private boolean sortOrderRating = true;
    private boolean sortOrderId = true;
    private boolean renderManufacture;
    private boolean renderOwner;
    private Product selectedProduct;

    private Long selectedCoordinates=1L;
    private int selectedOrganization=1;
    private Long selectedPerson=1L; // Выбранный существующий Person
    @PostConstruct
    public void init() {
        product = new Product();
        unitOfMeasureValues = Arrays.asList(UnitOfMeasure.values()); // Assuming UnitOfMeasure is an Enum

    }

    // Other methods
    public void setMessage(String message, String messageStyle) {
        this.message = message;
        this.messageStyle = messageStyle;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageStyle() {
        return messageStyle;
    }


    public List<UnitOfMeasure> getUnitOfMeasureValues() {
        return unitOfMeasureValues;
    }

    public void setUnitOfMeasureValues(List<UnitOfMeasure> unitOfMeasureValues) {
        this.unitOfMeasureValues = unitOfMeasureValues;
    }
    // Получение списка продуктов
    public List<Product> getProductList() {
        productList = productService.findAll();
        System.out.println("productList: "+ productList);
        return productList.stream()
                .filter(product -> nameFilter == null || nameFilter.isEmpty() || this.product.getName().equalsIgnoreCase(nameFilter)) // Фильтруем по имени, если nameFilter задан
                .filter(product -> partNumberFilter == null || partNumberFilter.isEmpty() || this.product.getPartNumber().equalsIgnoreCase(partNumberFilter))
                .sorted(comparator)
                .collect(Collectors.toList());
    }
    private void resetSortOrders(String currentSort) {
        if (!"name".equals(currentSort)) {
            sortOrderName = true;
        }
        if (!"date".equals(currentSort)) {
            sortOrderDate = true;
        }
        if (!"price".equals(currentSort)) {
            sortOrderPrice = true;
        }
        if (!"manufactureCost".equals(currentSort)) {
            sortOrderManufactureCost = true;
        }
        if (!"rating".equals(currentSort)) {
            sortOrderRating = true;
        }
        if (!"id".equals(currentSort)) {
            sortOrderId = true;
        }
    }
    public void sortByName() {
        resetSortOrders("name");
        comparator = sortOrderName
                ? Comparator.comparing(Product::getName)
                : Comparator.comparing(Product::getName).reversed();
        sortOrderName = !sortOrderName; // Переключаем порядок сортировки
    }

    public void sortByDate() {
        resetSortOrders("date");
        comparator = sortOrderDate
                ? Comparator.comparing(Product::getCreationDate)
                : Comparator.comparing(Product::getCreationDate).reversed();
        sortOrderDate = !sortOrderDate;
    }

    public void sortByPrice() {
        resetSortOrders("price");
        comparator = sortOrderPrice
                ? Comparator.comparing(Product::getPrice)
                : Comparator.comparing(Product::getPrice).reversed();
        sortOrderPrice = !sortOrderPrice;
    }

    public void sortByManufactureCost() {
        resetSortOrders("manufactureCost");
        comparator = sortOrderManufactureCost
                ? Comparator.comparing(Product::getManufactureCost)
                : Comparator.comparing(Product::getManufactureCost).reversed();
        sortOrderManufactureCost = !sortOrderManufactureCost;
    }

    public void sortByRating() {
        resetSortOrders("rating");
        comparator = sortOrderRating
                ? Comparator.comparing(Product::getRating)
                : Comparator.comparing(Product::getRating).reversed();
        sortOrderRating = !sortOrderRating;
    }

    public void sortById() {
        resetSortOrders("id");
        comparator = sortOrderId
                ? Comparator.comparing(Product::getId)
                : Comparator.comparing(Product::getId).reversed();
        sortOrderId = !sortOrderId;
    }
    // Получение продукта по ID
    public void getProductById() {
        product = productService.findById(productId);
    }
    // Обновление существующего продукта
    public void updateProduct() {
        productService.update(product);
    }

    // Удаление продукта
    public void deleteProduct(long id) {
        productService.delete(id);
    }


    public String editProduct(Long productId) {
        selectedProduct = productService.findById(productId); // Загружаем продукт по ID

        System.out.println("Selected product for edit: "+ selectedProduct);

        return "editProduct.xhtml?faces-redirect=true";
    }
    // Метод для извлечения ID из URL и загрузки продукта




    public void deleteDialogContent() {
        // Показать сообщение о невозможности удаления, если есть связанные объекты
        if (productService.hasRelatedObjects(product.getId())) {
            // Показываем сообщение
        }
    }
    // Список существующих объектов Person
    public List<Person> getExistingPersons() {
        System.out.println("getExistingPersons"); return personService.findAll();
    }

    // Getters и Setters для Person
    public boolean isUseExistingPerson() {
        return useExistingPerson;
    }

    public void setUseExistingPerson(boolean useExistingPerson) {
        this.useExistingPerson = useExistingPerson;
    }

    public Long getSelectedPerson() {
        System.out.println("getSelectedPerson method called."); return selectedPerson;
    }

    public void setSelectedPerson(Long selectedPerson) {
        System.out.println("setSelectedPerson method called."); this.selectedPerson = selectedPerson;
    }
    //public void loadManufacturer() {
      //  renderManufacture = true;
     //   renderOwner = false;
     //   manufacturer = organizationService.findById(selectedOrganization);
     //   loadAddress();
  //  }


    public void saveProduct() {
        try {

            product.setCoordinates(coordinatesService.findById(selectedCoordinates));
            product.setManufacturer(organizationService.findById(selectedOrganization));
            product.setOwner(personService.findById(selectedPerson));


            // Присваиваем текущего пользователя
            User currentUser = userBean.getCurrentUser();
            if (currentUser != null) {
                product.setUser(currentUser);
            }

            // Сохраняем продукт
            productService.save(product);
            product = new Product();  // Создаем новый объект, чтобы не обновлялся старый


            // Устанавливаем сообщение об успехе
            setMessage("Продукт успешно создан!", "alert-success");
        } catch (Exception e) {
            setMessage("Ошибка при создании продукта: " + e.getMessage(), "alert-danger");
        }
    }
    public void updateEditProduct() {
        try {


            // Сохраняем обновленный продукт через сервис
            productService.update(selectedProduct);

            // Устанавливаем сообщение об успешном обновлении
            setMessage("Продукт успешно обновлен!", "alert-success");
        } catch (Exception e) {
            // Обработка ошибок и вывод сообщения об ошибке
            setMessage("Ошибка при обновлении продукта: " + e.getMessage(), "alert-danger");
        }
    }



    public void sigma() {
        System.out.println("sigma moment: ");
    }

    // Сброс полей формы
    public void resetForm() {
        product = new Product();
        useExistingCoordinates = false;
        useExistingOrganization = false;
        useExistingPerson = false;
        selectedCoordinates = null;
        selectedOrganization = Integer.parseInt(null);
        selectedPerson = null;
    }

    // Остальные методы остаются без изменений

    public User getCurrentUser() {
        // Присваиваем текущего пользователя
        User currentUser = userBean.getCurrentUser();
        return currentUser;

    }
    public void setCurrentUser(User user) {
        currentUser = user;
    }
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public boolean isUseExistingCoordinates() {
        return useExistingCoordinates;
    }

    public void setUseExistingCoordinates(boolean useExistingCoordinates) {
        this.useExistingCoordinates = useExistingCoordinates;
    }

    public boolean isUseExistingOrganization() {
        return useExistingOrganization;
    }

    public void setUseExistingOrganization(boolean useExistingOrganization) {
        this.useExistingOrganization = useExistingOrganization;
    }
    public float calculateTotalRating() {
        return productService.getTotalRating();
    }


    public Long getSelectedCoordinates() {
        return selectedCoordinates;
    }

    public void setSelectedCoordinates(Long selectedCoordinates) {
        this.selectedCoordinates = selectedCoordinates;
    }

    public int getSelectedOrganization() {
        return selectedOrganization;
    }

    public void setSelectedOrganization(int selectedOrganization) {
        this.selectedOrganization = selectedOrganization;
    }

    public List<Coordinates> getExistingCoordinates() {
        return coordinatesService.findAll();
    }

    public List<Organization> getExistingOrganizations() {
        return organizationService.findAll();
    }

    public List<UnitOfMeasure> getUnitOfMeasures() {
        return Arrays.asList(UnitOfMeasure.values());
    }

    public List<OrganizationType> getOrganizationTypes() {
        return Arrays.asList(OrganizationType.values());
    }
}

package beans;

import entities.*;
import enums.Role;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
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
import java.util.ArrayList;
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
    private String opMessage;
    private String messageStyle;
    private String opMessageStyle;

    //@Inject
    //private SecurityContext securityContext; // Введение SecurityContext для получения текущего пользователя

    @Inject
    private ProductService productService;

    @Inject
    private CoordinatesService coordinatesService;

    @Inject
    private OrganizationService organizationService;
    @Inject
    private AddressService addressService;

    @Inject
    private PersonService personService; // Добавляем сервис для Person
    @Inject
    private LocationService locationService; // Добавляем сервис для Person

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
    private Person clickedPerson;      // Выбранный объект Person
    private Product clickedProduct;    // Выбранный продукт
    private Location clickedLocation;
    private Address clickedAddress;
    private Organization clickedManufacturer;
    private float totalRating;
    private List<Object[]> groupedByPartNumber;
    private Float ratingThreshold=0.0f;
    private Integer minPrice; // Минимальная цена
    private Integer maxPrice; // Максимальная цена
    private Float discountPercentage=0.0f; // Процент скидки
    private List<Product> productsToUpdate= new ArrayList<>(); // <-- Добавлено;

    private List<Product> filteredProducts;
    private List<Product> paginatedProducts; // Продукты на текущей странице
    private int currentPage = 1; // Текущая страница
    private int rowsPerPage = 5; // Количество строк на страницу
    private int totalRows; // Общее количество строк
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
                .filter(product -> nameFilter == null || nameFilter.isEmpty() || this.product.getName().toLowerCase().contains(nameFilter.toLowerCase())) // Фильтруем по имени, если nameFilter задан
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
        User currentUser = userBean.getCurrentUser();
        productService.update(product);
        //productService.createHistory(product,currentUser,"UPDATE");
    }

    // Удаление продукта
    public void deleteProduct(long id) {
        // Присваиваем текущего пользователя
        System.out.println("deleteProduct");
        Product product=productService.findById(id);
        if (product== null) {
            System.out.println("deleteProduct: product is null");
        }else {
            System.out.println("deleteProduct: product is not null");
            // if (currentUser != null) {
            //    product.setUser(currentUser);
            // }
            User currentUser = userBean.getCurrentUser();
            productService.createHistory(product, currentUser, "DELETE");
            productService.delete(id);
        }
    }


    public String editProduct(Long productId) {
        selectedProduct = productService.findById(productId); // Загружаем продукт по ID

        System.out.println("Selected product for edit: "+ selectedProduct);
        //User currentUser = userBean.getCurrentUser();
        //productService.createHistory(product,currentUser,"UPDATE");
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
            productService.createHistory(product,currentUser,"CREATE");
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
            User currentUser = userBean.getCurrentUser();
            productService.createHistory(selectedProduct,currentUser,"UPDATE");

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
    public void calculateTotalRating() {
        System.out.println("calculateTotalRating");
        this.totalRating= productService.getTotalRating().floatValue();
        System.out.println("TotalRating:" +this.totalRating);
    }
    public void groupByPartNumber() {
        System.out.println("groupByPartNumber");
        List<Object[]> grouped = productService.groupByPartNumber();
        groupedByPartNumber = new ArrayList<>();
        for (Object[] group : grouped) {
            groupedByPartNumber.add(group);
        }
    }
    // Метод для фильтрации продуктов по рейтингу
    public void filterProductsByRating() {
        if (ratingThreshold != null ) {
            filteredProducts = productService.getProductsAboveRating(ratingThreshold);
        }
    }
    // Метод для фильтрации продуктов по диапазону цен
    public void filterProductsByPrice() {
        if (minPrice != null && maxPrice != null && minPrice <= maxPrice) {
            filteredProducts = productService.getProductsInPriceRange(minPrice, maxPrice);
        }
    }
    // Метод для применения скидки ко всем продуктам или продуктам, принадлежащим пользователю
    public void applyDiscountToProducts() {
        if (discountPercentage == null || discountPercentage <= 0 || discountPercentage > 100) {
            opMessage = "Введите корректное значение скидки (от 0 до 100)";
            opMessageStyle = "red"; // красный цвет для ошибки
            return;
        }

        if (Role.ADMIN.equals(userBean.getCurrentUser().getRole())) {
            productsToUpdate = productService.findAll();
        } else if (Role.USER.equals(userBean.getCurrentUser().getRole())) {
            productsToUpdate = productService.getProductsByUser(userBean.getCurrentUser());
        }

        if (productsToUpdate == null || productsToUpdate.isEmpty()) {
            opMessage = "Нет продуктов, к которым можно применить скидку";
            opMessageStyle = "red"; // красный цвет для ошибки
            return;
        }

        int updatedCount = 0;
        for (Product product : productsToUpdate) {
            int newPrice = Math.round(product.getPrice() - (product.getPrice() * discountPercentage / 100));
            product.setPrice(newPrice);
            productService.update(product);
            updatedCount++;
        }

        opMessage = "Скидка успешно применена к " + updatedCount + " товар(ам)!";
        opMessageStyle = "green"; // зеленый цвет для успешного выполнения
    }


    public void validateDiscountPercentage(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }

        try {
            float discount = Float.parseFloat(value.toString());


        } catch (NumberFormatException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Введите корректное число для процента скидки.", null);
            throw new ValidatorException(message);
        }
    }
    public void validatePrice(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }

        try {
            Integer price = Integer.parseInt(value.toString());

            // Проверка, что цена больше 0
            if (price < 0) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Цена должна быть больше 0", null);
                throw new ValidatorException(message);
            }

        } catch (NumberFormatException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Введите корректное число", null);
            throw new ValidatorException(message);
        }
    }

    public void validateRating(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }

        try {
            Float rating = Float.parseFloat(value.toString());

            // Проверка, что рейтинг больше 0
            if (rating < 0) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Рейтинг должен быть больше 0", null);
                throw new ValidatorException(message);
            }

        } catch (NumberFormatException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Введите корректное число", null);
            throw new ValidatorException(message);
        }
    }
    // Метод для загрузки информации о Person по OwnerId
    public void loadPersonInfo(Long ownerId) {
        // Находим продукт по OwnerId
        System.out.println("loadPersonInfo, ownerId: "+ownerId);
        clickedPerson = personService.findById(ownerId);
        System.out.println("loadPersonInfo, clickedPerson:" + clickedPerson);

        // Если Person не найден, выводим сообщение
        if (clickedPerson == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Персона с данным OwnerId не найдена.", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }
    // Метод для загрузки информации о Manufacturer по ID
    public void loadManufacturerInfo(int manufacturerId) {
        // Находим производителя по manufacturerId
        System.out.println("loadManufacturerInfo, manufacturerId: " + manufacturerId);
        clickedManufacturer = organizationService.findById(manufacturerId);
        System.out.println("loadManufacturerInfo, clickedManufacturer: " + clickedManufacturer);

        // Если производитель не найден, выводим сообщение
        if (clickedManufacturer == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Производитель с данным ID не найден.", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }

    // Метод для загрузки информации о Location по ID локации
    public void loadLocationInfo(Long locationId) {
        // Находим локацию по locationId
        System.out.println("loadLocationInfo, locationId: " + locationId);
        clickedLocation = locationService.findById(locationId);
        System.out.println("loadLocationInfo, clickedLocation: " + clickedLocation);

        // Если локация не найдена, выводим сообщение
        if (clickedLocation == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Локация с данным ID не найдена.", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }
    // Метод для загрузки информации об Address по ID
    public void loadAddressInfo(Long addressId) {
        System.out.println("loadAddressInfo, addressId: " + addressId);
        clickedAddress = addressService.findById(addressId);
        System.out.println("loadAddressInfo, clickedAddress: " + clickedAddress);

        if (clickedAddress == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Адрес с данным ID не найден.", null);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
    }
    public void loadProducts() {
        System.out.println("loadProducts");
        productList = productService.findAll().stream()
                .filter(product -> nameFilter == null || nameFilter.isEmpty() || product.getName().toLowerCase().contains(nameFilter.toLowerCase()))
                .filter(product -> partNumberFilter == null || partNumberFilter.isEmpty() || product.getPartNumber().equalsIgnoreCase(partNumberFilter))
                .sorted(comparator)
                .collect(Collectors.toList());
        totalRows = productList.size();
        updatePaginatedProducts();
    }
    private void updatePaginatedProducts() {
        //System.out.println("updatePaginatedProducts");
        int fromIndex = (currentPage - 1) * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, totalRows);
        paginatedProducts = productList.subList(fromIndex, toIndex);
        System.out.println("updatePaginatedProducts, paginatedProducts:" + paginatedProducts);
    }
    public void previousPage() {

        if (currentPage > 1) {
            currentPage--;
            updatePaginatedProducts();
        }
        System.out.println("previousPage, currentPage"+ currentPage);
    }
    public void nextPage() {
       // System.out.println("nextPage");
        if (currentPage < getTotalPages()) {
            currentPage++;
            updatePaginatedProducts();
        }
        System.out.println("nextPage, currentPage"+ currentPage);
    }
    public int getTotalPages() {
        return (int) Math.ceil((double) totalRows / rowsPerPage);
    }
    public List<Product> getPaginatedProducts() {
        loadProducts(); // Загружаем свежие данные из БД
        return paginatedProducts;
    }
    public Person getClickedPerson() {
        return clickedPerson;
    }

    public void setClickedPerson(Person clickedPerson) {
        this.clickedPerson = clickedPerson;
    }
    public Product getClickedProduct() {
        return clickedProduct;
    }

    public void setClickedProduct(Product clickedProduct) {
        this.clickedProduct = clickedProduct;
    }

    // Геттеры и сеттеры для discountPercentage и productsToUpdate
    public Float getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public List<Product> getProductsToUpdate() {
        return productsToUpdate;
    }

    public void setProductsToUpdate(List<Product> productsToUpdate) {
        this.productsToUpdate = productsToUpdate;
    }
    // Геттеры и сеттеры для minPrice, maxPrice, filteredProducts
    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    // Геттеры и сеттеры для ratingThreshold и filteredProducts
    public float getRatingThreshold() {
        return ratingThreshold;
    }

    public void setRatingThreshold(float ratingThreshold) {
        this.ratingThreshold = ratingThreshold;
    }

    public List<Product> getFilteredProducts() {
        return filteredProducts;
    }

    public void setFilteredProducts(List<Product> filteredProducts) {
        this.filteredProducts = filteredProducts;
    }
    public List<Object[]> getGroupedByPartNumber() {
        return groupedByPartNumber;
    }

    public void setGroupedByPartNumber(List<Object[]> groupedByPartNumber) {
        this.groupedByPartNumber = groupedByPartNumber;
    }

    // Геттер и сеттер
    public float getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(float totalRating) {
        this.totalRating = totalRating;
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

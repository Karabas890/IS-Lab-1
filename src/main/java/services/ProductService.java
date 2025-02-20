package services;

import WebSocket.DataWebSocket;
import entities.*;
import enums.Color;
import enums.OrganizationType;
import enums.UnitOfMeasure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductService implements Serializable{

    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private UserService userService;
    @Inject
    private PersonService personService;
    Set<String> seenPassportIDs = new HashSet<>();  // Для хранения уникальных passportID
    Set<String> seenPartNumbers = new HashSet<>();  // Для хранения уникальных PartNumber


    // Создание нового продукта
    @Transactional
    public void create(Product product) {
        entityManager.persist(product);
        DataWebSocket.broadcast("save product " + product.getName());
    }

    // Получение всех продуктов
    public List<Product> findAll() {
        return entityManager.createQuery("SELECT p FROM Product p", Product.class).getResultList();
    }

    // Получение продукта по ID
    public Product findById(long id) {
        return entityManager.find(Product.class, id);
    }

    // Обновление существующего продукта
    @Transactional
    public void update(Product product) {
        entityManager.merge(product);
        //createHistory(product, userService.findByUsername(userService.getCurrentUserName()), "UPDATE");
        DataWebSocket.broadcast("update product " + product.getName());
    }
    //Сохранение продукта
    @Transactional
    public void save(Product product) {
        if (product.getId() == null) {
            entityManager.persist(product); // Новый объект -> persist

            DataWebSocket.broadcast("save product " + product.getName());
        } else {
            entityManager.merge(product);   // Существующий объект -> merge
            DataWebSocket.broadcast("save product "+product.getName());
            //DataWebSocket.broadcast("save product " + product.getName());
        }
    }

    // Удаление продукта
    @Transactional
    public void delete(long id) {
        Product product = findById(id);
        if (product != null) {
            entityManager.remove(product);
            DataWebSocket.broadcast("delete product "+product.getName());
        }
    }
    // Метод для записи в историю изменений
    @Transactional
    public void createHistory(Product product, User user, String operationType) {
        // Загружаем product из базы, если он ещё не сохранён
       /* if (product.getId() != null) {
            System.out.println("createHistory: do merge");
            product = entityManager.merge(product);
        } else {
            System.out.println("createHistory: do persist");
            entityManager.persist(product);
        }
        */
        if (product.getId() == null) {
            System.out.println("createHistory: product is null");
        }else {
            System.out.println("createHistory: product is not null");
            ProductHistory productHistory = new ProductHistory();
            productHistory.setProductId(product.getId());
            productHistory.setUser(user);
            productHistory.setOperationType(operationType);
            productHistory.setOperationDate(new Date()); // Устанавливаем текущую дату
            entityManager.persist(productHistory); // Сохраняем историю
        }
    }

    // Проверка на связанные объекты перед удалением
    public boolean hasRelatedObjects(long productId) {
        // Проверка логики на связанные объекты (например, через внешние ключи)
        return false; // На основе конкретной логики для связи объектов
    }
    // Метод для вычисления суммы всех значений rating
    public Double getTotalRating() {
        TypedQuery<Double> query = entityManager.createQuery("SELECT SUM(p.rating) FROM Product p", Double.class);
        return query.getSingleResult();
    }
    public List<Object[]> groupByPartNumber() {
        TypedQuery<Object[]> query = entityManager.createQuery(
                "SELECT p.partNumber, COUNT(p) FROM Product p GROUP BY p.partNumber", Object[].class);
        return query.getResultList();
    }
    // Метод для фильтрации продуктов по рейтингу
    public List<Product> getProductsAboveRating(Float ratingThreshold) {
        TypedQuery<Product> query = entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.rating > :ratingThreshold", Product.class);
        query.setParameter("ratingThreshold", ratingThreshold);
        return query.getResultList();
    }
    // Метод для получения продукции в заданном диапазоне цен
    public List<Product> getProductsInPriceRange(Integer minPrice, Integer maxPrice) {
        TypedQuery<Product> query = entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice", Product.class);
        query.setParameter("minPrice", minPrice);
        query.setParameter("maxPrice", maxPrice);
        return query.getResultList();
    }
    // Метод для получения продуктов конкретного пользователя
    public List<Product> getProductsByUser(User user) {
        TypedQuery<Product> query = entityManager.createQuery(
                "SELECT p FROM Product p WHERE p.user = :user", Product.class);
        query.setParameter("user", user);
        return query.getResultList();
    }
    @Transactional
    public List<Product> parseCsv(BufferedReader reader) throws IOException {
        seenPassportIDs = new HashSet<>();  // Для хранения уникальных passportID
        seenPartNumbers = new HashSet<>();  // Для хранения уникальных PartNumber
        List<Product> products = new ArrayList<>();
        List<String> lines = reader.lines().collect(Collectors.toList());
        System.out.println("parseCsv");

        int lineNumber = 1; // Для вывода номера строки в случае ошибки

        for (String line : lines) {
            String[] values = line.split(";"); // или ","
            System.out.println("parseCsv, values:"+values.length);
            if (values.length < 29) {
                throw new IllegalArgumentException("Ошибка: строка " + lineNumber + " содержит недостаточно данных.");
                //continue;
            }
            if (values.length > 29) {
                throw new IllegalArgumentException("Ошибка: строка " + lineNumber + " содержит больше данных, чем необходимо.");
                //continue;
            }

            try {
                Product product = new Product();
                if (values[0] == null || values[0].isEmpty()) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": 'name' не может быть пустым.");
                }
                product.setName(values[0]);

                // Проверка на цену
                try {
                    int price = Integer.parseInt(values[1]);
                    if (price <= 0) {
                        throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": цена должна быть больше 0.");
                    }
                    product.setPrice(price);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": неверный формат поля 'Price'.");
                }

                // Проверка на стоимость производства
                try {
                    product.setManufactureCost(Integer.parseInt(values[2]));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": неверный формат поля 'ManufactureCost'.");
                }

                // Проверка на рейтинг
                try {
                    float rating = Float.parseFloat(values[3]);
                    if (rating <= 0) {
                        throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": рейтинг должен быть больше 0.");
                    }
                    product.setRating(rating);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": неверный формат поля 'Rating'.");
                }

                try {
                    String partNumber = values[4].isEmpty() ? null : values[4];

                    if (partNumber != null) {
                        // Проверка длины строки PartNumber
                        if (partNumber.length() > 67) {
                            throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": длина 'PartNumber' не должна превышать 67 символов.");
                        }

                        // Проверка уникальности PartNumber
                        if (seenPartNumbers.contains(partNumber) || isPartNumberExists(partNumber)) {
                            throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": 'PartNumber' должно быть уникальным.");
                        }

                        // Добавляем в набор для проверки уникальности
                        seenPartNumbers.add(partNumber);
                    }

                    product.setPartNumber(partNumber);

                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": неверный формат поля 'PartNumber' - " + e.getMessage());
                }

                // Парсинг UnitOfMeasure
                try {
                    product.setUnitOfMeasure(values[5].isEmpty() ? null : UnitOfMeasure.valueOf(values[5]));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": неверное значение 'UnitOfMeasure'.");
                }

                // Вложенный объект Coordinates
                Coordinates coordinates = new Coordinates();
                try {
                    double x = Double.parseDouble(values[6]);
                    if (x > 648) {
                        throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": значение 'X' координат не может превышать 648.");
                    }
                    coordinates.setX(x);
                    coordinates.setY(Long.parseLong(values[7]));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": неверный формат координат.");
                }
                product.setCoordinates(coordinates);

                // Вложенный объект Organization (Производитель)
                Organization manufacturer = new Organization();
                manufacturer.setName(values[8]);

                // Проверка на пустое имя организации
                if (manufacturer.getName() == null || manufacturer.getName().isEmpty()) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": название 'Organization' не может быть пустым.");
                }

                try {
                    float annualTurnover = Float.parseFloat(values[9]);
                    if (annualTurnover <= 0) {
                        throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": 'annualTurnover' должно быть больше 0.");
                    }
                    manufacturer.setAnnualTurnover(annualTurnover);

                    int employeesCount = Integer.parseInt(values[10]);
                    if (employeesCount <= 0) {
                        throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": 'employeesCount' должно быть больше 0.");
                    }
                    manufacturer.setEmployeesCount(employeesCount);

                    double rating = Double.parseDouble(values[12]);
                    if (rating <= 0) {
                        throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": 'rating' должно быть больше 0.");
                    }
                    manufacturer.setRating(rating);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": неверный формат числового значения в Organization.");
                }

                manufacturer.setFullName(values[11]);

                // Парсинг OrganizationType
                try {
                    manufacturer.setType(values[13].isEmpty() ? null : OrganizationType.valueOf(values[13]));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": неверное значение 'OrganizationType'.");
                }

                // Вложенный объект Address (Официальный адрес производителя)
                Address address = new Address();
                address.setZipCode(values[14]);

                // Вложенный объект Location (Город производителя)
                Location location = new Location();
                try {
                    location.setX(Integer.parseInt(values[15]));
                    location.setY(Integer.parseInt(values[16]));
                    location.setZ(Float.parseFloat(values[17]));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": неверный формат координат города.");
                }
                location.setName(values[18]);

                address.setTown(location);
                manufacturer.setOfficialAddress(address);
                product.setManufacturer(manufacturer);

                // Вложенный объект Person (Владелец продукта)
                Person owner = new Person();
                if (values[19] == null || values[19].isEmpty()) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": Поле 'name' у человека не может быть пустым.");
                }
                owner.setName(values[19]);

                try {
                    float height = Float.parseFloat(values[20]);
                    if (height <= 0) {
                        throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": 'height' должно быть больше 0.");
                    }
                    owner.setHeight(height);

                    String weightStr = values[21];
                    if (!weightStr.isEmpty()) {
                        float weight = Float.parseFloat(weightStr);
                        if (weight <= 0) {
                            throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": 'weight' должно быть больше 0.");
                        }
                        owner.setWeight(weight);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": неверный формат числовых значений в Person.");
                }

                try {
                    String passportID = values[22];

                    // Проверка длины строки passportID
                    if (passportID.length() > 33) {
                        throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": длина 'passportID' не должна превышать 33 символов.");
                    }
                    if (passportID.length() < 6) {
                        throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": длина 'passportID' должна быть не меньше 6 символов.");
                    }

                    // Проверка уникальности passportID
                    if (seenPassportIDs.contains(passportID) || personService.isPassportIDExists(passportID)) {
                        throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": 'passportID' должно быть уникальным.");
                    }

                    // Добавляем в набор для проверки уникальности
                    seenPassportIDs.add(passportID);

                    owner.setPassportID(passportID);

                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": неверный формат поля 'passportID' - " + e.getMessage());
                }

                // Парсинг Color (глаза и волосы)
                try {
                    owner.setEyeColor(values[23].isEmpty() ? null : Color.valueOf(values[23]));
                    owner.setHairColor(values[24].isEmpty() ? null : Color.valueOf(values[24]));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": неверное значение 'Color'.");
                }

                // Вложенный объект Location (Местоположение владельца)
                Location ownerLocation = new Location();
                try {
                    ownerLocation.setX(Integer.parseInt(values[25]));
                    ownerLocation.setY(Integer.parseInt(values[26]));
                    ownerLocation.setZ(Float.parseFloat(values[27]));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Ошибка в строке " + lineNumber + ": неверный формат координат владельца.");
                }
                ownerLocation.setName(values[28]);

                owner.setLocation(ownerLocation);
                product.setOwner(owner);

                // Добавляем продукт в список
                products.add(product);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage());
            } catch (Exception e) {

                throw new IllegalArgumentException("Неизвестная ошибка в строке " + lineNumber + ": " + e.getMessage());
                //e.printStackTrace();
            }

            lineNumber++;
        }
        return products;
    }


    @Transactional
    public void importProducts(List<Product> products) {
        //ImportHistory importEntry = new ImportHistory();
        //importEntry.setStatus("IN_PROGRESS");
        //importEntry.setUsername(userService.getCurrentUserName());
        //Integer addedCount=0;
        try {
            for (Product product : products) {
                entityManager.persist(product);
                //addedCount++;
                createHistory(product,product.getUser(),"IMPORT");
            }
           // importEntry.setStatus("SUCCESS");
            //importEntry.setCountAdded(addedCount);
           // entityManager.merge(importEntry);
            DataWebSocket.broadcast("importProducts");
            // Выброс исключения для тестирования
            //throw new RuntimeException("Тестовая ошибка после операций с БД, но до соохранения в MinIO");
        } catch (Exception e) {
            //importEntry.setStatus("FAILED");
            DataWebSocket.broadcast("importProducts");
            throw new RuntimeException("Ошибка при сохранении продуктов: " + e.getMessage());
        }
    }
    @Transactional
    public void saveImportHistory(ImportHistory importEntry) {
        entityManager.merge(importEntry);
    }
    @Transactional
    public ImportHistory createImportHistory(ImportHistory importHistory) {
        entityManager.persist(importHistory);
        return importHistory;  // Возвращаем сохраненный объект
    }
    @Transactional
    public void updateImportHistory(ImportHistory importHistory) {
        entityManager.merge(importHistory);
    }


    public List<ImportHistory> getAllImportHistory() {
        return entityManager.createQuery("SELECT i FROM ImportHistory i ORDER BY i.id DESC", ImportHistory.class)
                .getResultList();
    }

    public List<ImportHistory> getUserImportHistory(String username) {
        return entityManager.createQuery("SELECT i FROM ImportHistory i WHERE i.username = :username ORDER BY i.id DESC", ImportHistory.class)
                .setParameter("username", username)
                .getResultList();
    }
    public boolean isPartNumberExists(String partNumber) {
        List<Product> products = entityManager.createQuery("SELECT p FROM Product p WHERE p.partNumber = :partNumber", Product.class)
                .setParameter("partNumber", partNumber)
                .getResultList();
        return !products.isEmpty();  // Если список не пустой, значит такой паспорт уже существует
    }




}
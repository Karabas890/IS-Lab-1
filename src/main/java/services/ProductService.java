package services;

import WebSocket.DataWebSocket;
import entities.Person;
import entities.Product;
import entities.User;
import entities.ProductHistory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class ProductService implements Serializable{

    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private UserService userService;

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
            DataWebSocket.broadcast("save product " + product.getName());
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
        // ✅ Загружаем product из базы, если он ещё не сохранён
        if (product.getId() != null) {
            product = entityManager.merge(product);
        } else {
            entityManager.persist(product);
        }
        ProductHistory productHistory = new ProductHistory();
        productHistory.setProduct(product);
        productHistory.setUser(user);
        productHistory.setOperationType(operationType);
        productHistory.setOperationDate(new Date()); // Устанавливаем текущую дату
        entityManager.persist(productHistory); // Сохраняем историю
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



}
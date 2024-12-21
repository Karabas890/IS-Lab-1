package dao;

import entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.util.List;

public class ProductDAO {

    @PersistenceContext
    private EntityManager entityManager;

    // Создание нового продукта
    @Transactional
    public void create(Product product) {
        entityManager.persist(product);
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
    }

    // Удаление продукта
    @Transactional
    public void delete(long id) {
        Product product = findById(id);
        if (product != null) {
            entityManager.remove(product);
        }
    }

    // Проверка на связанные объекты перед удалением
    public boolean hasRelatedObjects(long productId) {
        // Проверка логики на связанные объекты (например, через внешние ключи)
        return false; // На основе конкретной логики для связи объектов
    }
}
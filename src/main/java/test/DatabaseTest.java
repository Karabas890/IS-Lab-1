package test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import util.JPAFactory;

public class DatabaseTest {

    private static EntityManagerFactory factory;
    private static EntityManager entityManager;

    public static void main(String[] args) {
        // Настройка и тест подключения
        setup();
        testConnection();
        tearDown();
    }

    public static void setup() {
        try {
            // Используем фабрику, которую мы создали в JPAFactory
            factory = JPAFactory.getFactory();
            if (factory == null) {
                System.out.println("EntityManagerFactory не инициализирован");
                return;
            }

            // Создаем EntityManager для взаимодействия с базой данных
            entityManager = factory.createEntityManager();
            if (entityManager == null) {
                System.out.println("EntityManager не инициализирован");
                return;
            }

            System.out.println("Подключение успешно установлено");
        } catch (Exception e) {
            System.out.println("Ошибка при настройке: " + e.getMessage());
        }
    }

    public static void testConnection() {
        try {
            // Проверяем подключение путем выполнения простого запроса
            if (entityManager != null) {
                Object result = entityManager.createQuery("SELECT 1").getResultList();
                if (result != null) {
                    System.out.println("Тест подключения успешен");
                } else {
                    System.out.println("Не удалось выполнить запрос");
                }
            } else {
                System.out.println("EntityManager не инициализирован для выполнения запроса");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении запроса: " + e.getMessage());
        }
    }

    public static void tearDown() {
        try {
            // После выполнения тестов закрываем EntityManager и EntityManagerFactory
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
                System.out.println("EntityManager закрыт");
            }
            if (factory != null && factory.isOpen()) {
                factory.close();
                System.out.println("EntityManagerFactory закрыт");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при завершении: " + e.getMessage());
        }
    }
}

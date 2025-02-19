package services;

import entities.User;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public class UserService implements Serializable {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(User user) {
        if (user.getId() == null) { // Если ID отсутствует, создаем новую запись
            entityManager.persist(user);
        } else { // Если ID есть, обновляем существующую
            entityManager.merge(user);
        }
    }

    public User findByUsernameAndPassword(String username, String password) {
        List<User> users = entityManager.createQuery(
                        "SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultList();

        return users.isEmpty() ? null : users.get(0);
    }

    public User findByUsername(String username) {
        List<User> users = entityManager.createQuery(
                        "SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();

        return users.isEmpty() ? null : users.get(0);
    }

    public String getCurrentUserName() {// Логика получения имени текущего пользователя, например, через сессию или безопасность
// Возможно использование SecurityContext или другой системы аутентификации
//return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        System.out.println("getCurrentUser moment in Service: ");
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            System.out.println("facesContext not null: ");
            String currentUserName = (String) facesContext.getExternalContext().getSessionMap().get("username");
            System.out.println("found currentUserName: ");
            return currentUserName;
        }
        System.out.println("facesContext is null: ");
        return null;
    }
    // Метод для обновления существующего пользователя
    @Transactional
    public void update(User user) {
        // Проверяем, существует ли пользователь в базе данных
        if (user != null && user.getId() != null) {
            entityManager.merge(user); // Сохраняем изменения пользователя
        } else {
            throw new IllegalArgumentException("User or user ID cannot be null");
        }
    }
    public List<User> findUsersWithPendingAdminRights() {
        System.out.println("findUsersWithPendingAdminRights in Service: ");
        return entityManager.createQuery("SELECT u FROM User u WHERE u.requestAdminRights = true", User.class)
                .getResultList();
    }

}


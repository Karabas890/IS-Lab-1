package services;

import entities.User;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.util.List;

public class UserService implements Serializable {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(User user) {
        entityManager.persist(user);
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
    public String getCurrentUserName() {
        // Логика получения имени текущего пользователя, например, через сессию или безопасность
        // Возможно использование SecurityContext или другой системы аутентификации
        return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }

}

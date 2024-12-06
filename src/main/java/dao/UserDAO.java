package dao;

import entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.util.List;

public class UserDAO implements Serializable {
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
}

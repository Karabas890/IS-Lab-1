package services;

import entities.Person;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;

import java.io.Serializable;
import java.util.List;

@Stateless
public class PersonService implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Person> findAll() {
        return entityManager.createQuery("SELECT p FROM Person p", Person.class).getResultList();
    }

    public Person findById(Long id) {
        return entityManager.find(Person.class, id);
    }

    @Transactional
    public void save(Person person) {
        try {
            entityManager.persist(person);
        } catch (jakarta.persistence.PersistenceException e) {
            // Проверяем, является ли причиной ConstraintViolationException
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new RuntimeException("Нарушение уникальности данных:");
            }
            throw new RuntimeException("Ошибка при сохранении объекта Person: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении объекта Person: " + e.getMessage(), e);
        }
    }


    public void update(Person person) {
        entityManager.merge(person);
    }

    public void delete(Long id) {
        Person person = findById(id);
        if (person != null) {
            entityManager.remove(person);
        }
    }
}

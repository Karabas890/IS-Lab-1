package services;

import entities.Person;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PersonService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Person> findAll() {
        return entityManager.createQuery("SELECT p FROM Person p", Person.class).getResultList();
    }

    public Person findById(int id) {
        return entityManager.find(Person.class, id);
    }

    public void save(Person person) {
        entityManager.persist(person);
    }

    public void update(Person person) {
        entityManager.merge(person);
    }

    public void delete(int id) {
        Person person = findById(id);
        if (person != null) {
            entityManager.remove(person);
        }
    }
}

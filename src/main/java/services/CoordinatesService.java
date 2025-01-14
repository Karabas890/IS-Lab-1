package services;

import entities.Coordinates;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;

@Stateless
public class CoordinatesService implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Coordinates> findAll() {
        return entityManager.createQuery("SELECT c FROM Coordinates c", Coordinates.class).getResultList();
    }

    public Coordinates findById(Long id) {
        return entityManager.find(Coordinates.class, id);
    }

    public void save(Coordinates coordinates) {
        entityManager.persist(coordinates);
    }

    public void update(Coordinates coordinates) {
        entityManager.merge(coordinates);
    }

    public void delete(Long id) {
        Coordinates coordinates = findById(id);
        if (coordinates != null) {
            entityManager.remove(coordinates);
        }
    }
}

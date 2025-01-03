package services;

import entities.Coordinates;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CoordinatesService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Coordinates> findAll() {
        return entityManager.createQuery("SELECT c FROM Coordinates c", Coordinates.class).getResultList();
    }

    public Coordinates findById(int id) {
        return entityManager.find(Coordinates.class, id);
    }

    public void save(Coordinates coordinates) {
        entityManager.persist(coordinates);
    }

    public void update(Coordinates coordinates) {
        entityManager.merge(coordinates);
    }

    public void delete(int id) {
        Coordinates coordinates = findById(id);
        if (coordinates != null) {
            entityManager.remove(coordinates);
        }
    }
}

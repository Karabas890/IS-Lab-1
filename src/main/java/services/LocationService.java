package services;

import entities.Location;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;

@Stateless
public class LocationService implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Location> findAll() {
        return entityManager.createQuery("SELECT l FROM Location l", Location.class).getResultList();
    }

    public Location findById(Long id) {
        return entityManager.find(Location.class, id);
    }

    public void save(Location location) {
        entityManager.persist(location);
    }

    public void update(Location location) {
        entityManager.merge(location);
    }

    public void delete(Long id) {
        Location location = findById(id);
        if (location != null) {
            entityManager.remove(location);
        }
    }
}

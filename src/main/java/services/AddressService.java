package services;

import entities.Address;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;
import java.util.LongSummaryStatistics;

@Stateless
public class AddressService implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Address> findAll() {
        return entityManager.createQuery("SELECT a FROM Address a", Address.class).getResultList();
    }

    public Address findById(Long id) {
        return entityManager.find(Address.class, id);
    }

    public void save(Address address) {
        entityManager.persist(address);
    }

    public void update(Address address) {
        entityManager.merge(address);
    }

    public void delete(Long id) {
        Address address = findById(id);
        if (address != null) {
            entityManager.remove(address);
        }
    }
}

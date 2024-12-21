package services;

import entities.Organization;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class OrganizationService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Organization> findAll() {
        return entityManager.createQuery("SELECT o FROM Organization o", Organization.class).getResultList();
    }

    public Organization findById(int id) {
        return entityManager.find(Organization.class, id);
    }

    public void save(Organization organization) {
        entityManager.persist(organization);
    }

    public void update(Organization organization) {
        entityManager.merge(organization);
    }

    public void delete(int id) {
        Organization organization = findById(id);
        if (organization != null) {
            entityManager.remove(organization);
        }
    }
}

package com.example.lunchtimeboot.restaurant.repository;

import com.example.lunchtimeboot.restaurant.entity.Restaurant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Restaurant> searchByName(String search) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Restaurant> cq = cb.createQuery(Restaurant.class);

        Root<Restaurant> root = cq.from(Restaurant.class);
        cq.select(root);
        cq.where(
                cb.like(root.get("name"), "%" + search + "%")
        );

        List<Restaurant> restaurants = em.createQuery(cq).setMaxResults(Constants.MAX_PAGE_SIZE).getResultList();

        return restaurants;
    }
}

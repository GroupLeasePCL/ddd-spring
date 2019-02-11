package com.example.lunchtimeboot.user.repository;

import com.example.lunchtimeboot.user.entity.User;
import com.example.lunchtimeboot.user.entity.UserFavouriteRestaurant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> searchByName(String search) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        Root<User> root = cq.from(User.class);
        cq.select(root);
        cq.where(
                cb.like(root.get("name"), "%" + search + "%")
        );

        List<User> users = em.createQuery(cq).setMaxResults(Constants.MAX_PAGE_SIZE).getResultList();

        return users;
    }

    @Override
    public List<User> findByFavouriteRestaurantId(UUID favouriteRestaurantId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> user = cq.from(User.class);
        Join<User, UserFavouriteRestaurant> fav = user.join("favouriteRestaurants", JoinType.INNER);
        fav.on(cb.equal(fav.get("restaurantId"), favouriteRestaurantId));

        List<User> users = em.createQuery(cq).getResultList();

        return users;
    }
}

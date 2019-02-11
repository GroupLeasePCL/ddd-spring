package com.example.lunchtimeboot.user.entity;

import com.example.lunchtimeboot.infrastructure.ddd.BaseAggregate;
import com.example.lunchtimeboot.user.event.UserFavedEvent;
import com.example.lunchtimeboot.user.event.UserRegisteredEvent;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class User extends BaseAggregate {

    @Column(nullable = false)
    private String name;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String mobile;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<UserFavouriteRestaurant> favouriteRestaurants = new ArrayList<>();

    public User() {
    }

    public User(UUID id, String name, String email, String mobile) {
        this.id = id;
        this.name = name;
        this.email = email.toLowerCase();
        this.mobile = mobile;

        addEvent(new UserRegisteredEvent(id, name, email, mobile));
    }

    public User fav(String name, UUID restaurantId) throws UserGuardException {
        UUID newFavId = UUID.randomUUID();
        UserFavouriteRestaurant newFavouriteRestaurant = new UserFavouriteRestaurant(newFavId, name, restaurantId);
        guardFav(newFavouriteRestaurant);

        favouriteRestaurants.add(newFavouriteRestaurant);
        newFavouriteRestaurant.setUser(this);

        addEvent(new UserFavedEvent(id, this.name, restaurantId));

        return this;
    }

    private void guardFav(UserFavouriteRestaurant newFavourite) throws UserGuardException {
        // user already fav
        UUID newFavRestaurantId = newFavourite.getRestaurantId();

        for (UserFavouriteRestaurant favourite: favouriteRestaurants) {
            if (favourite.getRestaurantId().equals(newFavRestaurantId)) {
                throw UserGuardException.createFavAlreadyFavedRestaurant(this, newFavourite);
            }
        }
    }

//    public User unFav(UserFavouriteRestaurant wasFavourite) {
//        // removing faved restaurant in application layer
//
//        addEvent(new UserUnfavedEvent(this));
//
//        return this;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<UserFavouriteRestaurant> getFavouriteRestaurants() {
        return favouriteRestaurants;
    }

    public void setFavouriteRestaurants(List<UserFavouriteRestaurant> favouriteRestaurants) {
        this.favouriteRestaurants = favouriteRestaurants;
    }
}

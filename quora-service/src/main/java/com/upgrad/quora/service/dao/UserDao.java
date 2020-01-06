package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetch a single user by given id from the database.
     *
     * @param userId Id of the user whose information is to be retrieved.
     * @return User details if exist in the DB else null.
     */
    public UserEntity getUserById(final String userId) {
        try {
            return entityManager.createNamedQuery("userByUserId", UserEntity.class).setParameter("userId", userId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    public UserEntity getUserByUserName(final String username) {
        try {
            return entityManager.createNamedQuery("userByUserName", UserEntity.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserAuthEntity createAuthToken(final UserAuthEntity userAuthEntity) {
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }

    public void updateUserAuthEntity(final UserAuthEntity updatedUserAuthEntity) {
        entityManager.merge(updatedUserAuthEntity);
    }

    public UserAuthEntity getUserAuthToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthByAccessToken", UserAuthEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
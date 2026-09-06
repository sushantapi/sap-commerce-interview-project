package com.sushant.electronics.dao.impl;

import com.sushant.electronics.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * SAP Commerce interview practice:
 *
 * In SAP Commerce, FlexibleSearch is normally used by a DAO to query
 * the Commerce type system, for example:
 * SELECT {p:pk} FROM {Product AS p} WHERE {p:code} = ?code
 *
 * Spring Boot has no FlexibleSearch engine. This class demonstrates the
 * same DAO responsibility using JPQL through EntityManager.
 */
@Repository
public class ProductFlexibleSearchDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Product> findByCode(String code) {
        List<Product> products = entityManager.createQuery(
                        "SELECT p FROM Product p WHERE p.code = :code",
                        Product.class)
                .setParameter("code", code)
                .setMaxResults(1)
                .getResultList();

        return products.stream().findFirst();
    }

    public List<Product> findActiveProductsByMinimumPrice(BigDecimal minimumPrice) {
        return entityManager.createQuery(
                        "SELECT p FROM Product p " +
                        "WHERE p.active = true AND p.price >= :minimumPrice " +
                        "ORDER BY p.price ASC",
                        Product.class)
                .setParameter("minimumPrice", minimumPrice)
                .getResultList();
    }

    public List<Product> searchByNameOrCode(String query) {
        return entityManager.createQuery(
                        "SELECT p FROM Product p " +
                        "WHERE LOWER(p.code) LIKE LOWER(:query) " +
                        "OR LOWER(p.name) LIKE LOWER(:query) " +
                        "ORDER BY p.name ASC",
                        Product.class)
                .setParameter("query", "%" + query.trim() + "%")
                .getResultList();
    }
}

package com.sushant.electronics.dao.impl;

import com.sushant.electronics.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * SAP Commerce interview practice.
 *
 * Spring Boot has no FlexibleSearch engine. This DAO demonstrates the same
 * querying responsibilities using parameterized JPQL through EntityManager.
 * Actual SAP Commerce FlexibleSearch works against the Commerce type system.
 */
@Repository
public class ProductFlexibleSearchDao {

    @PersistenceContext
    private EntityManager entityManager;

    /** Equivalent to: SELECT {p:pk} FROM {Product AS p} WHERE {p:code} = ?code */
    public Optional<Product> findByCode(String code) {
        List<Product> products = entityManager.createQuery(
                        "SELECT p FROM Product p WHERE p.code = :code", Product.class)
                .setParameter("code", code)
                .setMaxResults(1)
                .getResultList();
        return products.stream().findFirst();
    }

    /** Equivalent to a parameterized name LIKE query. */
    public List<Product> findByName(String name) {
        return entityManager.createQuery(
                        "SELECT p FROM Product p " +
                        "WHERE LOWER(p.name) LIKE LOWER(:name) " +
                        "ORDER BY p.name ASC", Product.class)
                .setParameter("name", "%" + name.trim() + "%")
                .getResultList();
    }

    /** Active products above a parameterized price, ordered by price. */
    public List<Product> findActiveProductsByMinimumPrice(BigDecimal minimumPrice) {
        return entityManager.createQuery(
                        "SELECT p FROM Product p " +
                        "WHERE p.active = true AND p.price >= :minimumPrice " +
                        "ORDER BY p.price ASC", Product.class)
                .setParameter("minimumPrice", minimumPrice)
                .getResultList();
    }

    /** Search code or name using a parameterized LIKE expression. */
    public List<Product> searchByNameOrCode(String query) {
        return entityManager.createQuery(
                        "SELECT p FROM Product p " +
                        "WHERE LOWER(p.code) LIKE LOWER(:query) " +
                        "OR LOWER(p.name) LIKE LOWER(:query) " +
                        "ORDER BY p.name ASC", Product.class)
                .setParameter("query", "%" + query.trim() + "%")
                .getResultList();
    }

    /** JOIN equivalent using the Product -> Category relationship. */
    public List<Product> findByCategoryCode(String categoryCode) {
        return entityManager.createQuery(
                        "SELECT p FROM Product p " +
                        "JOIN p.category c " +
                        "WHERE c.code = :categoryCode " +
                        "ORDER BY p.name ASC", Product.class)
                .setParameter("categoryCode", categoryCode)
                .getResultList();
    }

    /** Explicit pagination using first-result/max-results. */
    public List<Product> findActiveProductsPage(BigDecimal minimumPrice, int page, int size) {
        return entityManager.createQuery(
                        "SELECT p FROM Product p " +
                        "WHERE p.active = true AND p.price >= :minimumPrice " +
                        "ORDER BY p.price ASC", Product.class)
                .setParameter("minimumPrice", minimumPrice)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public long countActiveProducts(BigDecimal minimumPrice) {
        return entityManager.createQuery(
                        "SELECT COUNT(p) FROM Product p " +
                        "WHERE p.active = true AND p.price >= :minimumPrice", Long.class)
                .setParameter("minimumPrice", minimumPrice)
                .getSingleResult();
    }
}

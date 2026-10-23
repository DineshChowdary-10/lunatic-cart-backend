package com.lunaticcart.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lunaticcart.entity.Product;

public interface ProductRepository extends JpaRepository<Product,Long>{
	List<Product> findByNameContainingIgnoreCase(String name);
	List<Product> findByCategoryIgnoreCase(String category);
	List<Product> findByStockQuantityLessThanEqual(Integer quantity);
	List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
	

@Modifying
@Query("""
    UPDATE Product p
    SET p.stockQuantity = p.stockQuantity - :quantity
    WHERE p.id = :productId
      AND p.stockQuantity >= :quantity
    """)
int reduceStockIfAvailable(
        @Param("productId") Long productId,
        @Param("quantity") Integer quantity
);


}

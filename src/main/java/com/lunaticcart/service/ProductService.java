package com.lunaticcart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.lunaticcart.entity.Product;
import com.lunaticcart.repository.ProductRepository;
import com.lunaticcart.exception.ResourceNotFoundException;

@Service
public class ProductService {
	
	private ProductRepository productRepository;
	
	public ProductService(ProductRepository productRepository)
	{
		this.productRepository = productRepository;
	}
	
	public Product createProduct(Product product)
	{
		return productRepository.save(product);
	}
	
	public List<Product> getAllProducts(Sort sort) {
	    return productRepository.findAll(sort);
	}
	

public Product getProductById(Long id) {
    return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
}


public Product updateProduct(Long id, Product updatedProduct) {

    Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    

    existingProduct.setName(updatedProduct.getName());
    existingProduct.setDescription(updatedProduct.getDescription());
    existingProduct.setPrice(updatedProduct.getPrice());
    existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
    existingProduct.setCategory(updatedProduct.getCategory());

    return productRepository.save(existingProduct);
   
    
}

public void deleteProduct(Long id) {

    Product existingProduct = productRepository.findById(id)
            .orElseThrow(() ->  new ResourceNotFoundException("Product not found with id: " + id));

    productRepository.delete(existingProduct);
}

public List<Product> searchProductsByName(String name) {
    return productRepository.findByNameContainingIgnoreCase(name);
}

public Page<Product> getProducts(Pageable pageable) {
    return productRepository.findAll(pageable);
}

public List<Product> getProductsByCategory(String category) {
    return productRepository.findByCategoryIgnoreCase(category);
}

public Boolean isProductInStock(Long id) {
    Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Product not found with id: " + id));

    return product.getStockQuantity() > 0;
}

public Boolean isProductLowStock(Long id) {
    Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Product not found with id: " + id));

    return product.getStockQuantity() <= 5;
}

public List<Product> getLowStockProducts() {
    return productRepository.findByStockQuantityLessThanEqual(5);
}

public Product updateStock(Long id, Integer quantity) {

    if (quantity < 0) {
        throw new IllegalArgumentException(
                "Stock quantity cannot be negative");
    }

    Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Product not found with id: " + id));

    product.setStockQuantity(quantity);

    return productRepository.save(product);
}

public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
    if (minPrice < 0 || maxPrice < minPrice) {
        throw new IllegalArgumentException("Invalid price range");
    }

    return productRepository.findByPriceBetween(minPrice, maxPrice);
}


@Transactional
public Product reduceStock(Long productId, Integer quantity) {

    if (quantity == null || quantity <= 0) {
        throw new IllegalArgumentException(
                "Quantity must be greater than zero"
        );
    }

    Integer updatedRows = productRepository.reduceStockIfAvailable(
            productId,
            quantity
    );

    if (updatedRows == 0) {
        Product product = getProductById(productId);

        throw new IllegalArgumentException(
                "Insufficient stock for product: " + product.getName()
        );
    }

    return getProductById(productId);
}


}

package com.lunaticcart.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.lunaticcart.entity.Product;
import com.lunaticcart.service.ProductService;



@RestController
@RequestMapping("/api/products")

public class ProductController {
	
	private final ProductService productService;
	
	public ProductController(ProductService productService)
	{
		this.productService = productService;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Product createProduct(@Valid @RequestBody Product product)
	{
		return productService.createProduct(product);
	}
	
	@GetMapping
	
	public List<Product> getAllProducts(
	        @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "asc") String direction) {

	    Sort sort = direction.equalsIgnoreCase("desc")
	            ? Sort.by(sortBy).descending()
	            : Sort.by(sortBy).ascending();

	    return productService.getAllProducts(sort);
	}
	

	@GetMapping("/{id:[0-9]+}")
@ResponseStatus(HttpStatus.OK)
public Product getProductById(@PathVariable Long id) {
    return productService.getProductById(id);
}


@PutMapping("/{id}")
@ResponseStatus(HttpStatus.OK)
public Product updateProduct(
        @PathVariable Long id,
        @Valid @RequestBody Product product) {

    return productService.updateProduct(id, product);
}


@DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
}

@GetMapping("/search")
public List<Product> searchProducts(@RequestParam String name) {
    return productService.searchProductsByName(name);
}

@GetMapping("/page")
public Page<Product> getProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {

    Pageable pageable = PageRequest.of(page, size);
    return productService.getProducts(pageable);
}

@GetMapping("/category/{category}")
public List<Product> getProductsByCategory(
        @PathVariable String category) {
    return productService.getProductsByCategory(category);
}

@GetMapping("/{id}/stock")
public Boolean checkProductStock(@PathVariable Long id) {
    return productService.isProductInStock(id);
}

@GetMapping("/{id}/low-stock")
public Boolean checkLowStock(@PathVariable Long id) {
    return productService.isProductLowStock(id);
}

@GetMapping("/low-stock")
public List<Product> getLowStockProducts() {
    return productService.getLowStockProducts();
}

@PutMapping("/{id}/stock")
public Product updateStock(
        @PathVariable Long id,
        @RequestParam Integer quantity) {

    return productService.updateStock(id, quantity);
}

@GetMapping("/price-range")
public List<Product> getProductsByPriceRange(
        @RequestParam Double minPrice,
        @RequestParam Double maxPrice) {

    return productService.getProductsByPriceRange(minPrice, maxPrice);
}











}

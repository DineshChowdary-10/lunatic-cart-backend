package com.lunaticcart.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "products")

public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Product name is required")
	private String name;

	private String description;

	@NotNull(message = "Product price is required")
	@PositiveOrZero(message = "Product price cannot be negative")
	private Double price;

	@NotNull(message = "Stock quantity is required")
	@PositiveOrZero(message = "Stock quantity cannot be negative")
	private Integer stockQuantity;
	
	private String category;
	
	

	public Product()
	{
		
	}

	public Product(Long id, String name, String description, Double price, Integer stockQuantity) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.stockQuantity = stockQuantity;
	}

	public Product(Long id, @NotBlank(message = "Product name is required") String name, String description,
			@NotNull(message = "Product price is required") @PositiveOrZero(message = "Product price cannot be negative") Double price,
			@NotNull(message = "Stock quantity is required") @PositiveOrZero(message = "Stock quantity cannot be negative") Integer stockQuantity,
			String category) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.category = category;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	
	
	
	

}

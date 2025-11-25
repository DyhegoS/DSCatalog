package com.formacaospring.dscatalog.tests;

import java.time.Instant;

import com.formacaospring.dscatalog.dto.ProductDTO;
import com.formacaospring.dscatalog.entities.Category;
import com.formacaospring.dscatalog.entities.Product;

public class Factory {
	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "good phone", 800.0, "http://img.com.br", Instant.parse("2025-11-25T03:00:00Z"));
		product.getCategories().add(new Category(2L, "Electronics"));
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}
}

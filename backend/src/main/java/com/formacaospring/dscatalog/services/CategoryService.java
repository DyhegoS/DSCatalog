package com.formacaospring.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacaospring.dscatalog.entities.Category;
import com.formacaospring.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional
	public List<Category> findAll(){
		List<Category> result = repository.findAll();
		return result.stream().map(x -> new Category(x)).toList();
	}
}

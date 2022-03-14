package com.airtel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import com.airtel.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airtel.advice.CacheableWithRefreshScope;
import com.airtel.model.Product;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@PostConstruct
	public void initDB() {
		List<Product> list=new ArrayList<>();
		for(int i=1;i<=10000;i++) {
			list.add(new Product(i, "product"+i, new Random().nextInt(2000)));
		}
		repository.saveAll(list);
	}
	
	public List<Product> addProduct(List<Product> products) {
		return repository.saveAll(products);
	}

	@CacheableWithRefreshScope
	public Product findProduct(int id) {
		return repository.findById(id);
	}

}

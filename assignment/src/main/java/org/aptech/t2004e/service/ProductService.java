package org.aptech.t2004e.service;

import java.util.List;
import java.util.stream.Collectors;

import org.aptech.t2004e.common.production.dto.ProducDto;
import org.aptech.t2004e.entity.mapper.ProductMapper;
import org.aptech.t2004e.entity.mapper.ProductMapperImpl;
import org.aptech.t2004e.repository.ProductRepository;
import org.keysoft.JpaRepository.main.impl.Pageable;

public class ProductService {
	ProductRepository productRepo = new  ProductRepository();
	ProductMapper productMapper = new ProductMapperImpl();
	
//	public static void main(String[] args) {
//		ProductService  productService = new ProductService() ;
//		ProducDto production = ProducDto.builder()
//				.page(3)
//				.size(2)
//				.build();
//		productService.gets(production).parallelStream().forEach(p -> {
//			System.err.println(p.toString());
//		});
//	}
	public  List<ProducDto> gets(ProducDto criteria) {			
		Pageable pageable = new Pageable(criteria.getPage(), criteria.getSize());
		return productRepo.findAll("",pageable).stream().map(productMapper :: entityToDto).collect(Collectors.toList());
	}
	
}

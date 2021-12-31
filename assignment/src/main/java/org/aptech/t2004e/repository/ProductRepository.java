package org.aptech.t2004e.repository;
import org.aptech.t2004e.entity.Product;
import org.keysoft.JpaRepository.main.impl.JpaRepositoryExecutorImpl;

public class ProductRepository extends JpaRepositoryExecutorImpl<Product>{

	public ProductRepository() {
		super(Product.class);		
	}
	
}

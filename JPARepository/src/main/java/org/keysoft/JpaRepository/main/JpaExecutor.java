package org.keysoft.JpaRepository.main;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import org.keysoft.JpaRepository.anotation.domain.Page;
import org.keysoft.JpaRepository.main.impl.Pageable;

public interface JpaExecutor<T> {
	List<T> findAll();

	Page findAll(String specification,  Pageable p);

	List<T> entityParser(ResultSet resultSet);
	
	
	//True : save successfully , false  : failed
	Boolean save( T entity) ;
	Optional<T> findById(Number id );
	Boolean update (Number id , T obj );
	Boolean delete(Number id );
	
	
	Boolean createTable();
	Boolean updateTable();
	
	

}
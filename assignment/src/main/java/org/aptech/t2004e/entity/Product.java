package org.aptech.t2004e.entity;

import org.keysoft.JpaRepository.anotation.clazz.Entity;
import org.keysoft.JpaRepository.anotation.field.Column;
import org.keysoft.JpaRepository.anotation.field.Id;
import org.keysoft.JpaRepository.consts.SQLDataTypes;

@Entity(tablename = "product_table")
public class Product {
	@Column(name = "name",type =SQLDataTypes.TEXT )
	private String name ;
	
	@Override
	public String toString() {
		return "Product [name=" + name + ", id=" + id + "]";
	}

	@Id
	@Column(name = "id",type =SQLDataTypes.INTEGER )
	private int id ;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Product(String name) {
		super();
		this.name = name;
	}

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}

}

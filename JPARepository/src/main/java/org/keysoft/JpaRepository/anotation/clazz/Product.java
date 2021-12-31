package org.keysoft.JpaRepository.anotation.clazz;

import org.keysoft.JpaRepository.anotation.field.Column;
import org.keysoft.JpaRepository.anotation.field.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tablename  = "product_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id(autoIncrement = true)
	@Column(name = "id" , type = "INT")
	private  Integer id ;
	@Column(name = "name",type = "VARCHAR(50)")
	private  String name ;
	
}

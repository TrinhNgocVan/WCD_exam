package org.keysoft.JpaRepository.main.impl;

import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor
public class Pageable {
	public int page ;
	public int size ;
	public int numberOfElements ;
	public Pageable(int page, int size) {
		super();
		this.page = page;
		this.size = size;
	}
	
}

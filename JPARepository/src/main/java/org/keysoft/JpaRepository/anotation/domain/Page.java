package org.keysoft.JpaRepository.anotation.domain;

import java.util.List;

import org.keysoft.JpaRepository.main.impl.Pageable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class Page<T>  {	
	@SuppressWarnings("rawtypes")
	private List content;
	private Long totalElements;
	private int number;
	private int numberOfElements;
	private long totalPages;
	public int page ;
	public int size ;
	
	private boolean first;
	private boolean last;
}

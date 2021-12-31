package org.keysoft.JpaRepository.main.impl;

import org.keysoft.JpaRepository.anotation.clazz.Entity;

public class Specification<T> {
	private Class<T> clazz ;
	private String className ;
	private String tableName ;
	
	public Specification(Class<T> clazz  ) {
		this.clazz = clazz ;
		this.className = clazz.getSimpleName();
		this.tableName = (clazz.getAnnotation(Entity.class) != null ) ? clazz.getAnnotation(Entity.class).tablename()
																					: this.className ;
	}
	private boolean isEntity() {
		return clazz.isAnnotationPresent(Entity.class);
	}
	
}

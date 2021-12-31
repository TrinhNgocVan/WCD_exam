package org.keysoft.JpaRepository.consts;

public enum SQLDataTypeEnum {
	INTEGER("INT"),
	SMALL_INTEGER("SMALLINT"),
	BIG_INTEGER("BIGINT"),
	FLOAT("FLOAT"),
	DOUBLE("DOUBLE"),
	DATE("DATE"),
	DATETIME("DATETIME"),
	TIME_STAMP("TIMESTAMP"),
	TEXT("TEXT");
	

	public String value ;
	private SQLDataTypeEnum(String value) {
		this.value = value;
	}
	
}

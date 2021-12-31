package org.keysoft.JpaRepository.consts;

public enum SqlStatementEnum {
	CREATE_SCHEMA("CREATE SCHEMA"),
	DROP_SCHEMA("DROP DATABASE"),
	END_STATEMENT (";"),
	END_CREATE_SCHEMA("DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin"),
	GRAVE_ACCENT("`"),
	CREATE_TABLE ("CREATE TABLE"),
	SPACE(" "),
	MULTIPLE_SPACE("       "),
	OPEN_PARENTHESES("("),
   CLOSE_PARENTHESES(")"),
   PRIMARY_KEY ("PRIMARY KEY"),
   AUTO_INCREMENT("AUTO_INCREMENT"),
   COMMA (","),
   INSERT_INTO ("INSERT INTO"),
   VALUES  ("VALUES"),
   APOSTROPHE  ("'"),
   NULL  ("null"),
   SELECT_ASTERISK  ("SELECT *"),
   SELECT_COUNT_ASTERISK("SELECT COUNT(*) AS table_count"),
   FROM  ("FROM"),
   WHERE ( "WHERE"),
   EQUAL  ("="),
   UPDATE  ("UPDATE"),
   SET  ("SET"),
   DELETE  ("DELETE"),
   LIMIT("LIMIT"),
   OFFSET("OFFSET"),
   DOWN_LINE("\n"),
   
   FOREIGN_KEY ("FOREIGN KEY"),
   REFERENCES  ("REFERENCES"),
   DROP  ("DROP"),
   DROP_TABLE ("DROP TABLE"),
   IF_EXISTS("IF EXISTS"),
   LIKE("LIKE"),
   GREATER_THAN(">"),
   LESS_THAN( "<"),
   GREATER_THAN_OR_EQUAL_TO( ">="),
   LESS_THAN_OR_EQUAL_TO( "<="),
   NOT_EQUAL_TO ("!="),
   NOT_NULL("NOT NULL"),
	FALSE("false");
	
	public String value ;
	private SqlStatementEnum(String value) {
		this.value = value;
	}
}

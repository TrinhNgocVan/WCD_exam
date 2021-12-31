package org.keysoft.JpaRepository.main.impl;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.lang.model.UnknownEntityException;

import org.apache.commons.lang3.StringUtils;
import org.keysoft.JpaRepository.anotation.clazz.Entity;
import org.keysoft.JpaRepository.anotation.domain.Page;
import org.keysoft.JpaRepository.anotation.field.Column;
import org.keysoft.JpaRepository.anotation.field.Id;
import org.keysoft.JpaRepository.consts.SQLDataTypeEnum;
import org.keysoft.JpaRepository.consts.SQLDataTypes;
import org.keysoft.JpaRepository.consts.SqlStatementEnum;
import org.keysoft.JpaRepository.helper.DatabaseHelper;
import org.keysoft.JpaRepository.helper.DateTimeHelper;
import org.keysoft.JpaRepository.main.JpaExecutor;

import com.mysql.cj.xdevapi.Statement;

public class JpaRepositoryExecutorImpl<T> extends DatabaseHelper implements JpaExecutor<T>   {	
	private Class<T> clazz ;
	private String className ;
	public String tableName ;
	Field[] fields ;
	public JpaRepositoryExecutorImpl(Class<T> clazz  ) {
		this.clazz = clazz ;
		this.className = clazz.getSimpleName();
		this.tableName = (clazz.getAnnotation(Entity.class) != null ) ? clazz.getAnnotation(Entity.class).tablename()
																					: this.className ;
		this.fields = clazz.getDeclaredFields() ;
	}
	@Override
	public List<T> findAll()  {
		try {
			Connection connection = getConnection();
			StringBuilder statement = new StringBuilder();
			statement.append(SqlStatementEnum.SELECT_ASTERISK.value).append(SqlStatementEnum.SPACE.value).append(SqlStatementEnum.FROM)
						.append(SqlStatementEnum.SPACE.value).append(tableName) ;
			System.out.println("Execute statement  : " + statement);
			/*
			 * Execute command
			 */
			 PreparedStatement preparedStatement = connection.prepareStatement(statement.toString());
			 ResultSet rs = preparedStatement.executeQuery();
			 return  entityParser(rs);
		} catch ( SQLException |IllegalArgumentException e ) {
			e.printStackTrace();
			return null ;
		}	
	}
	@Override
	public Page findAll(String specification ,Pageable p){
		try {
			StringBuilder statement = new StringBuilder();
			statement.append(SqlStatementEnum.SELECT_ASTERISK.value).append(SqlStatementEnum.SPACE.value).append(SqlStatementEnum.FROM)
						.append(SqlStatementEnum.SPACE.value).append(tableName)
						// append filter to sql
						.append(SqlStatementEnum.SPACE.value).append(specification)
						// append paging to sql					
						.append(SqlStatementEnum.DOWN_LINE.value)
						.append(SqlStatementEnum.MULTIPLE_SPACE.value).append(SqlStatementEnum.LIMIT.value).append(SqlStatementEnum.SPACE.value)
						.append(Integer.toString(p.getSize()))
						.append(SqlStatementEnum.SPACE.value).append(SqlStatementEnum.OFFSET.value).append(SqlStatementEnum.SPACE.value)
						.append(Integer.toString(p.getSize()*(p.getPage())));	
			
			StringBuilder countStatement = new StringBuilder() ;
			countStatement.append(SqlStatementEnum.SELECT_COUNT_ASTERISK.value).append(SqlStatementEnum.SPACE.value).append(SqlStatementEnum.FROM)
						.append(SqlStatementEnum.SPACE.value).append(tableName)
							// append filter to sql
						.append(SqlStatementEnum.SPACE.value).append(specification)
						// append paging to sql					
						.append(SqlStatementEnum.DOWN_LINE.value);
			
			/*
			 * Execute command
			 */
			System.out.println("Execute statement  : " + statement);
			 Connection connection = getConnection();
			 PreparedStatement countPrepareStatement = connection.prepareStatement(countStatement.toString());
			 ResultSet countRs = countPrepareStatement.executeQuery();
			 Long count = null ;
			 if(countRs.next()) {
				count = countRs.getLong("table_count") ;
			 }
			 PreparedStatement preparedStatement = connection.prepareStatement(statement.toString());
			 ResultSet rs = preparedStatement.executeQuery();
			 List<T> lsEntitys = entityParser(rs);
			 Page<T> page = new Page<>();
			 page.setContent(lsEntitys);
			 page.setNumber(lsEntitys.size());
			 page.setPage(p.getPage());
			 page.setSize(p.getSize());
			 page.setTotalElements(count);
			 page.setTotalPages(page.getTotalElements()/page.getSize());				
			return page	;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null ;
		}		
	}	
	@Override
	public Boolean save(T  obj) {
		
		// check entity is instance of clazz . If not return entityException . 
		
		try {
			StringBuilder statement  = new StringBuilder() ;
			statement.append(SqlStatementEnum.INSERT_INTO.value).append(SqlStatementEnum.SPACE.value)
					.append(tableName).append(SqlStatementEnum.OPEN_PARENTHESES.value);
			for (Field f : fields) {
				String columnName = f.getName().toLowerCase();
				if(f.isAnnotationPresent(Column.class)) {
					Column c = f.getAnnotation(Column.class);
					if(StringUtils.isNoneEmpty(c.name())) {
						columnName = c.name();
					}
				}
				// id check 
				if(f.isAnnotationPresent(Id.class)) {
					Id currentId = f.getAnnotation(Id.class);
					if(currentId.autoIncrement()) {
						continue;
					}
				}
				statement.append(columnName).append(SqlStatementEnum.COMMA.value).append(SqlStatementEnum.SPACE.value);
			}
			statement.setLength(statement.length() -2 ) ;
			statement.append(SqlStatementEnum.CLOSE_PARENTHESES.value).append(SqlStatementEnum.SPACE.value).append(SqlStatementEnum.VALUES.value)
															.append(SqlStatementEnum.SPACE.value).append(SqlStatementEnum.OPEN_PARENTHESES.value);
			
			for (Field f : fields) {
				if(!f.isAnnotationPresent(Column.class)) {
					continue ;
				}
				Column  c =  f.getAnnotation(Column.class);
				String type = c.type() ;
				f.setAccessible(true);
				Object value = f.get(obj);
				if(type.equals(SQLDataTypes.DATE)) {
					Date date = (Date) value  ;
					value =  DateTimeHelper.convertJavaDateToSqlDate(date);
					}
				if(type.equals(SQLDataTypes.DATETIME) || type.equals(SQLDataTypes.TIME_STAMP)) {
					Date date  =(Date) value ;
					value = DateTimeHelper.convertJavaDateToSqlDateTime(date);
				}
				if(f.isAnnotationPresent(Id.class)){
					Id currentId = (Id) f.getAnnotation(Id.class);
					if(currentId.autoIncrement()) {
						continue ;
					}
				}
//				if(value == null) {
//					statement.append(SqlStatementEnum.NULL.value).append(SqlStatementEnum.COMMA.value).append(SqlStatementEnum.SPACE.value);
//				}
				if(SQLDataTypes.needApostrophe(type)) {
					statement.append(SqlStatementEnum.APOSTROPHE.value);
				}
				statement.append(value);
				if(SQLDataTypes.needApostrophe(type)) {
					statement.append(SqlStatementEnum.APOSTROPHE.value);
				}
				statement.append(SqlStatementEnum.COMMA.value).append(SqlStatementEnum.SPACE.value);
							
			}
			statement.setLength(statement.length() -2 );
			statement.append(SqlStatementEnum.CLOSE_PARENTHESES.value);
			System.err.println("statement : "+ statement);
			Connection connection = getConnection() ;
			connection.createStatement().execute(statement.toString());
			return true ;
		} catch (IllegalAccessException  | UnknownEntityException | SQLException e) {
			e.printStackTrace();
		}
			return false ;

	}
		
	@Override
	public  List<T> entityParser(ResultSet resultSet)  {
		ArrayList<T> lsEntitys = new ArrayList<T>();
		
		try {
			 while (resultSet.next()) {
		            T entity = clazz.getDeclaredConstructor().newInstance();
		            for (Field field : clazz.getDeclaredFields()) {
		                String columnName = field.getName().toLowerCase();
		                if (field.isAnnotationPresent(Column.class)  && StringUtils.isNoneBlank(field.getAnnotation(Column.class).name())) {
		                    Column columnInfor = field.getAnnotation(Column.class);
		                    columnName = columnInfor.name();		                    
		                    field.setAccessible(true);
		                    switch (columnInfor.type()) {
		                        case SQLDataTypes.INTEGER:
		                            field.set(entity, resultSet.getInt(columnName));
		                            break;
		                        case SQLDataTypes.TEXT:
		                            field.set(entity, resultSet.getString(columnName));
		                            break;		 
		                        case SQLDataTypes.VARCHAR255:
		                        	field.set(entity, resultSet.getString(columnName));
		                        	break ;
		                        case SQLDataTypes.VARCHAR50:
		                        	field.set(entity, resultSet.getString(columnName));
		                        	break ;
		                        case SQLDataTypes.DOUBLE:
		                            field.set(entity, resultSet.getDouble(columnName));
		                            break;
		                        case SQLDataTypes.DATE:
		                            field.set(entity, DateTimeHelper.convertSqlDateToJavaDate(resultSet.getDate(columnName)));
		                            break;
		                        case SQLDataTypes.DATETIME:
		                        case SQLDataTypes.TIME_STAMP:
		                            field.set(entity, DateTimeHelper.convertSqlTimeStampToJavaDate(resultSet.getTimestamp(columnName)));
		                            break;
		                    }
		                }
		            }
		         lsEntitys.add(entity); 
			 }
						
		} catch (InstantiationException | IllegalAccessException | SQLException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}     
        return lsEntitys;
    }
	@Override
	public Optional<T> findById(Number id) {
		try {
			Connection connection = getConnection();
			StringBuilder statement = new StringBuilder();
			statement.append(SqlStatementEnum.SELECT_ASTERISK.value).append(SqlStatementEnum.SPACE.value).append(SqlStatementEnum.FROM)
						.append(SqlStatementEnum.SPACE.value).append(tableName) 
						.append(SqlStatementEnum.SPACE.value).append(SqlStatementEnum.WHERE.value).append(SqlStatementEnum.SPACE.value);
			for(Field f : fields) {
				Column c = f.getAnnotation(Column.class);
				if(f.isAnnotationPresent(Id.class)) {
					statement.append(c.name()).append(SqlStatementEnum.SPACE.value).append(SqlStatementEnum.EQUAL.value)
					.append(SqlStatementEnum.SPACE.value);
					if(c.type().equals(SQLDataTypes.INTEGER) | c.type().equals(SQLDataTypes.BIG_INTEGER) | c.type().equals(SQLDataTypes.SMALL_INTEGER)) {
							statement.append(id);
						}else {
							statement.append(SqlStatementEnum.APOSTROPHE.value).append(SqlStatementEnum.SPACE.value)
										.append(id).append(SqlStatementEnum.SPACE.value).append(SqlStatementEnum.APOSTROPHE.value);
						}
					break;
				}				
			}
			System.err.println(statement);
			PreparedStatement prepareStatement  = connection.prepareStatement(statement.toString());
			ResultSet rs = prepareStatement.executeQuery() ;
			return Optional.ofNullable(entityParser(rs).get(0));		
					
		} catch (SQLException  e) {
			e.printStackTrace();
		}
			return null ;
		
	}
	@Override
	public Boolean update(Number id, T obj) {
		try {
			Connection connection = getConnection() ;
			StringBuilder statement  = new StringBuilder() ;
			statement.append(SqlStatementEnum.UPDATE.value).append(SqlStatementEnum.SPACE.value).append(tableName).append(SqlStatementEnum.SPACE.value)
						.append(SqlStatementEnum.SET.value).append(SqlStatementEnum.SPACE.value);
			// id information
			 String idName = "";
	         String idType = "";
			for(Field f : fields){
				if(!f.isAnnotationPresent(Column.class)) {
					continue ;
				}
				f.setAccessible(true);
				Column columnInformation = f.getDeclaredAnnotation(Column.class);
	             String columnName = columnInformation.name();
	             String columnType = columnInformation.type();
	             Object value = f.get(obj);
	             if (columnType.equals(SQLDataTypes.DATE)) {
	                    Date date = (Date) value;
	                    value = DateTimeHelper.convertJavaDateToSqlDate(date);
	                }
	                if (columnType.equals(SQLDataTypes.DATETIME) || columnType.equals(SQLDataTypes.TIME_STAMP)) {
	                    Date date = (Date) value;
	                    value = DateTimeHelper.convertJavaDateToSqlDateTime(date);
	                }
	                if (f.isAnnotationPresent(Id.class)) {
	                    //dont update id
	                    //but get id information
	                    idName = columnName;
	                    idType = columnType;
	                    continue;
	                }
	                statement.append(columnName).append(SqlStatementEnum.SPACE.value).append(SqlStatementEnum.EQUAL.value) ;
	                if (value == null) {
	                    statement.append(SqlStatementEnum.NULL.value);
	                    statement.append(SqlStatementEnum.COMMA.value);
	                    statement.append(SqlStatementEnum.SPACE.value);
	                    continue;
	                }
	                if (!columnType.equals(SQLDataTypes.INTEGER)) {
	                    statement.append(SqlStatementEnum.APOSTROPHE.value);
	                }
	                statement.append(value);
	                if (!columnType.equals(SQLDataTypes.INTEGER)) {
	                	statement.append(SqlStatementEnum.APOSTROPHE.value);
	                }
	                statement.append(SqlStatementEnum.COMMA.value);
	                statement.append(SqlStatementEnum.SPACE.value);		
			}
			statement.setLength(statement.length() - 2);
			statement.append(SqlStatementEnum.SPACE.value);
			statement.append(SqlStatementEnum.WHERE.value);
			statement.append(SqlStatementEnum.SPACE.value);
			statement.append(idName);
			statement.append(SqlStatementEnum.SPACE.value);
			statement.append(SqlStatementEnum.EQUAL.value);
			statement.append(SqlStatementEnum.SPACE.value);
            if (!idType.equals(SQLDataTypes.INTEGER)) {
    			statement.append(SqlStatementEnum.APOSTROPHE.value);
            }
			statement.append(id);
            if (!idType.equals(SQLDataTypes.INTEGER)) {
    			statement.append(SqlStatementEnum.APOSTROPHE.value);
            }
            connection.createStatement().execute(statement.toString());
            System.err.println("Statement : " + statement);
            return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false ;
	}
	@Override
	public Boolean delete(Number id) {
		try {
            Connection connection = getConnection() ; 
            StringBuilder statement = new StringBuilder();
            statement.append(SqlStatementEnum.DELETE.value);
            statement.append(SqlStatementEnum.SPACE.value);
            statement.append(SqlStatementEnum.FROM.value);
            statement.append(SqlStatementEnum.SPACE.value);
            statement.append(tableName);
            statement.append(SqlStatementEnum.SPACE.value);
            statement.append(SqlStatementEnum.WHERE.value);
            //id information
            String idName = "";
            String idType = "";
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Column.class)) {
                    continue;
                }

                field.setAccessible(true);
                Column columnInformation = field.getDeclaredAnnotation(Column.class);
                String columnName = columnInformation.name();
                String columnType = columnInformation.type();
                if (field.isAnnotationPresent(Id.class)) {
                    //dont update id
                    //but get id information
                    idName = columnName;
                    idType = columnType;
                    break;
                }
            }
            statement.append(SqlStatementEnum.SPACE.value);
            statement.append(idName);
            statement.append(SqlStatementEnum.SPACE.value);
            statement.append(SqlStatementEnum.EQUAL.value);
            statement.append(SqlStatementEnum.SPACE.value);
            if (!idType.equals(SQLDataTypes.INTEGER)) {
            	statement.append(SqlStatementEnum.APOSTROPHE.value);
            }
            statement.append(id);
            if (!idType.equals(SQLDataTypes.INTEGER)) {
            	statement.append(SqlStatementEnum.APOSTROPHE.value);
            }
            connection.createStatement().execute(statement.toString());
            System.err.println("Execute Statement : "+ statement );
            return true;
        } catch ( SQLException error) {
            System.out.printf("Delete failed  error: %s \n", error.getMessage());
        }
        return false;
	}
	@Override
	public Boolean createTable() {
		try {
			Connection connection = getConnection() ;
			StringBuilder statement = new StringBuilder() ;
			statement.append(SqlStatementEnum.CREATE_TABLE.value).append(SqlStatementEnum.SPACE.value).append(tableName).append(SqlStatementEnum.SPACE.value)
					.append(SqlStatementEnum.OPEN_PARENTHESES.value).append(SqlStatementEnum.DOWN_LINE.value).append(SqlStatementEnum.SPACE.value);
			for(Field f : fields) {
				if(!f.isAnnotationPresent(Column.class)) {
					continue ;
				}
				f.setAccessible(true);
				Column columnInformation = f.getDeclaredAnnotation(Column.class);
				String columnName = columnInformation.name();
	            String columnType = columnInformation.type();
	            String nullable = columnInformation.nullable();
				if(f.isAnnotationPresent(Id.class)) {
					statement.append(columnName).append(SqlStatementEnum.SPACE.value).append(columnType).append(SqlStatementEnum.SPACE.value).append(SqlStatementEnum.AUTO_INCREMENT.value)
					.append(SqlStatementEnum.SPACE.value).append(SqlStatementEnum.PRIMARY_KEY.value).append(SqlStatementEnum.COMMA.value).append(SqlStatementEnum.DOWN_LINE.value); 
				}else {
					statement.append(columnName).append(SqlStatementEnum.SPACE.value).append(columnType).append(SqlStatementEnum.SPACE.value);
					if(nullable.toLowerCase().equals(SqlStatementEnum.FALSE.value.toLowerCase())) {
						statement.append(SqlStatementEnum.NOT_NULL.value).append(SqlStatementEnum.SPACE)
						.append(SqlStatementEnum.COMMA.value).append(SqlStatementEnum.DOWN_LINE.value); 
					}else {
						statement.append(SqlStatementEnum.COMMA.value).append(SqlStatementEnum.DOWN_LINE.value); 
					}
				}				
			}
			statement.setLength(statement.length() -2 );
			statement.append(SqlStatementEnum.CLOSE_PARENTHESES.value);
			System.err.println("Statement : "+statement);
			connection.createStatement().execute(statement.toString());			
			return true ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false ;
	}
	@Override
	public Boolean updateTable() {
		DatabaseHelper dbHelper = new DatabaseHelper() ;
		dbHelper.dropTable(tableName);
		createTable();
		return true;
	}


	
	
}

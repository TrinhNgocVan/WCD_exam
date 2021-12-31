package org.aptech.t2004e.entity.mapper;

import java.util.List;

import org.aptech.t2004e.common.production.dto.ProducDto;
import org.aptech.t2004e.entity.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {
	ProducDto entityToDto (Product product);
	List<ProducDto> entityToDto(List<Product> lsProduct);
	
}

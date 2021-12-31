package org.aptech.t2004e.entity.mapper;

import java.util.ArrayList;
import java.util.List;
import org.aptech.t2004e.common.production.dto.ProducDto;
import org.aptech.t2004e.common.production.dto.ProducDto.ProducDtoBuilder;
import org.aptech.t2004e.entity.Product;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-12-24T18:16:58+0700",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 1.4.0.v20210708-0430, environment: Java 16.0.2 (Oracle Corporation)"
)
*/
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProducDto entityToDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProducDtoBuilder<?, ?> producDto = ProducDto.builder();

        producDto.id( product.getId() );
        producDto.name( product.getName() );

        return producDto.build();
    }

    @Override
    public List<ProducDto> entityToDto(List<Product> lsProduct) {
        if ( lsProduct == null ) {
            return null;
        }

        List<ProducDto> list = new ArrayList<ProducDto>( lsProduct.size() );
        for ( Product product : lsProduct ) {
            list.add( entityToDto( product ) );
        }

        return list;
    }
}

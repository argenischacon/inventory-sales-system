package com.argenischacon.inventory_sales_system.mapper;

import com.argenischacon.inventory_sales_system.dto.sale.SaleDetailRequestDTO;
import com.argenischacon.inventory_sales_system.dto.sale.SaleDetailResponseDTO;
import com.argenischacon.inventory_sales_system.model.SaleDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { ProductMapper.class })
public interface SaleDetailMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unitPrice", ignore = true) // Handled by service logic
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "product", ignore = true) // Handled by service logic
    @Mapping(target = "sale", ignore = true) // Handled by service logic
    SaleDetail toEntity(SaleDetailRequestDTO dto);

    @Mapping(target = "subTotal", expression = "java(entity.getSubTotal())")
    SaleDetailResponseDTO toResponseDTO(SaleDetail entity);
}

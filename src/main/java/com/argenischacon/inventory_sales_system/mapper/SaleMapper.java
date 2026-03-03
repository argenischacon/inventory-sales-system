package com.argenischacon.inventory_sales_system.mapper;

import com.argenischacon.inventory_sales_system.dto.sale.SaleRequestDTO;
import com.argenischacon.inventory_sales_system.dto.sale.SaleResponseDTO;
import com.argenischacon.inventory_sales_system.model.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { CustomerMapper.class,
        SaleDetailMapper.class })
public interface SaleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ticketNumber", ignore = true) // Handled by service logic
    @Mapping(target = "total", ignore = true) // Handled by service logic
    @Mapping(target = "date", ignore = true) // Handled by service logic (usually now)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "customer", ignore = true) // Handled by service logic
    @Mapping(target = "saleDetails", ignore = true) // Handled by service logic
    Sale toEntity(SaleRequestDTO dto);

    SaleResponseDTO toResponseDTO(Sale entity);
}

package com.argenischacon.inventory_sales_system.mapper;

import com.argenischacon.inventory_sales_system.dto.supplier.SupplierNestedDTO;
import com.argenischacon.inventory_sales_system.dto.supplier.SupplierRequestDTO;
import com.argenischacon.inventory_sales_system.dto.supplier.SupplierResponseDTO;
import com.argenischacon.inventory_sales_system.model.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    Supplier toEntity(SupplierRequestDTO dto);

    SupplierResponseDTO toResponseDTO(Supplier entity);

    SupplierNestedDTO toNestedDTO(Supplier entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    void updateEntityFromDto(SupplierRequestDTO dto, @MappingTarget Supplier entity);
}

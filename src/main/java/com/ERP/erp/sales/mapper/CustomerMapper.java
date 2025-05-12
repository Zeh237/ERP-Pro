package com.ERP.erp.sales.mapper;

import org.springframework.stereotype.Component;
import com.ERP.erp.sales.dto.CustomerDto;
import com.ERP.erp.sales.model.Customer;

@Component
public class CustomerMapper{
    public CustomerDto toDto(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerDto dto = new CustomerDto();

        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        dto.setActive(customer.isActive());
        dto.setCreatedAt(customer.getCreatedAt());

        return dto;
    }

    public Customer toEntity(CustomerDto dto) {
        if (dto == null) {
            return null;
        }

        Customer customer = new Customer();
        customer.setId(dto.getId());

        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customer.setActive(dto.isActive());
        return customer;
    }
}
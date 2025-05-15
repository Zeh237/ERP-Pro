package com.ERP.erp.sales.service;

import com.ERP.erp.sales.dto.CustomerDto;
import com.ERP.erp.sales.mapper.CustomerMapper;
import com.ERP.erp.sales.model.Customer;
import com.ERP.erp.sales.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Transactional
    public CustomerDto createCustomer(CustomerDto customerDto) {
        if (customerRepository.existsByEmail(customerDto.getEmail())) {
            throw new IllegalArgumentException("Customer with email " + customerDto.getEmail() + " already exists.");
        }

        Customer customer = customerMapper.toEntity(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        return customerOptional.map(customerMapper::toDto).orElse(null);
    }

    @Transactional(readOnly = true)
    public Page<CustomerDto> getAllCustomers(Pageable pageable) {
        Page<Customer> customersPage = customerRepository.findAll(pageable);
        return customersPage.map(customerMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> getActiveCustomers() {
        List<Customer> activeCustomers = customerRepository.findByActiveTrue();
        return activeCustomers.stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<CustomerDto> searchCustomersByName(String name, Pageable pageable) {
        Page<Customer> customersPage = customerRepository.findByNameContainingIgnoreCase(name, pageable);
        return customersPage.map(customerMapper::toDto);
    }

    @Transactional(readOnly = true)
    public boolean customerExistsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> getCustomersWithRecentOrders(LocalDate date) {
        List<Customer> customers = customerRepository.findWithRecentOrders(date);
        return customers.stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + id));

        if (customerDto.getName() != null) {
            existingCustomer.setName(customerDto.getName());
        }
        if (customerDto.getEmail() != null) {
            if (!existingCustomer.getEmail().equals(customerDto.getEmail()) && customerRepository.existsByEmail(customerDto.getEmail())) {
                throw new IllegalArgumentException("Customer with email " + customerDto.getEmail() + " already exists.");
            }
            existingCustomer.setEmail(customerDto.getEmail());
        }
        if (customerDto.getPhone() != null) {
            existingCustomer.setPhone(customerDto.getPhone());
        }
        if (customerDto.getAddress() != null) {
            existingCustomer.setAddress(customerDto.getAddress());
        }
        existingCustomer.setActive(customerDto.isActive());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toDto(updatedCustomer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer not found with ID: " + id);
        }
        customerRepository.deleteById(id);
    }
}

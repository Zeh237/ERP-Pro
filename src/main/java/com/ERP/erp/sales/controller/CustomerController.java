package com.ERP.erp.sales.controller;

import com.ERP.erp.sales.dto.CustomerDto;
import com.ERP.erp.sales.service.CustomerService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @GetMapping("/create")
    public String createCustomerForm(Model model, @AuthenticationPrincipal UserDetails userDetails){
        model.addAttribute("customer", new CustomerDto());
        if (userDetails != null) {
            model.addAttribute("user", userDetails);
        }
        return "customers/create";
    }

    @PostMapping("/create")
    public String createCustomer(
            @Valid @ModelAttribute("customer") CustomerDto customerDto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails){
        if (userDetails != null) {
            model.addAttribute("user", userDetails);
        }

        if (result.hasErrors()) {
            logger.warn("Validation errors while creating customer: {}", result.getAllErrors());
            return "customers/create";
        }

        try{
            CustomerDto customer = customerService.createCustomer(customerDto);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Customer '" + customer.getName() + "' created successfully!");
            return "redirect:/customer/list";
        } catch (IllegalArgumentException e) {
            logger.error("Error creating customer: Duplicate email", e);
            model.addAttribute("error", e.getMessage());
            return "customers/create";
        }
        catch (Exception e) {
            logger.error("Error creating customer", e);
            model.addAttribute("error", "An unexpected error occurred while creating the customer.");
            return "customers/create";
        }
    }

    @GetMapping("/list")
    public String listCustomers(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            model.addAttribute("user", userDetails);
        }

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CustomerDto> customerPage = customerService.getAllCustomers(pageable);

        model.addAttribute("customerPage", customerPage);
        model.addAttribute("currentPage", customerPage.getNumber());
        model.addAttribute("totalPages", customerPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        return "customers/list";
    }

    @GetMapping("/search")
    public String searchCustomers(
            Model model,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            model.addAttribute("user", userDetails);
        }

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CustomerDto> customerPage = customerService.searchCustomersByName(name, pageable);

        model.addAttribute("customerPage", customerPage);
        model.addAttribute("currentPage", customerPage.getNumber());
        model.addAttribute("totalPages", customerPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("searchName", name);

        return "customers/list";
    }

    @GetMapping("/{id}")
    public String viewCustomer(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            model.addAttribute("user", userDetails);
        }

        CustomerDto customer = customerService.getCustomerById(id);

        if (customer == null) {
            logger.warn("Customer not found with ID: {}", id);
            return "redirect:/customer/list?error=Customer not found";
        }

        model.addAttribute("customer", customer);
        return "customers/view";
    }

    @GetMapping("/edit/{id}")
    public String editCustomerForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            model.addAttribute("user", userDetails);
        }

        CustomerDto customer = customerService.getCustomerById(id);

        if (customer == null) {
            logger.warn("Customer not found for editing with ID: {}", id);
            return "redirect:/customer/list?error=Customer not found";
        }

        model.addAttribute("customer", customer);
        return "customers/edit";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(
            @PathVariable Long id,
            @Valid @ModelAttribute("customer") CustomerDto customerDto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            model.addAttribute("user", userDetails);
        }

        if (result.hasErrors()) {
            logger.warn("Validation errors while updating customer with ID {}: {}", id, result.getAllErrors());
            model.addAttribute("customer", customerDto);
            return "sales/customers/edit";
        }

        try {
            CustomerDto updatedCustomer = customerService.updateCustomer(id, customerDto);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Customer '" + updatedCustomer.getName() + "' updated successfully!");
            return "redirect:/customer/list";
        } catch (EntityNotFoundException e) {
            logger.error("Error updating customer: Customer with ID {} not found", id, e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/customer/list";
        } catch (IllegalArgumentException e) {
            logger.error("Error updating customer: Duplicate email for ID {}", id, e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("customer", customerDto);
            return "sales/customers/edit";
        }
        catch (Exception e) {
            logger.error("Error updating customer with ID {}", id, e);
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred while updating the customer.");
            return "redirect:/customer/list";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("success", "Customer with ID " + id + " deleted successfully.");
        } catch (EntityNotFoundException e) {
            logger.error("Error deleting customer: Customer with ID {} not found", id, e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            logger.error("Error deleting customer with ID {}", id, e);
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred while deleting the customer.");
        }
        return "redirect:/customer/list";
    }

    @GetMapping("/active")
    public String listActiveCustomers(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            model.addAttribute("user", userDetails);
        }
        List<CustomerDto> activeCustomers = customerService.getActiveCustomers();
        model.addAttribute("customers", activeCustomers);
        return "sales/customers/list";
    }

    @GetMapping("/recent-orders")
    public String listCustomersWithRecentOrders(
            Model model,
            @RequestParam(required = false) Optional<String> sinceDate,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            model.addAttribute("user", userDetails);
        }

        LocalDate date = sinceDate.map(LocalDate::parse).orElse(LocalDate.now().minusDays(30));

        List<CustomerDto> customers = customerService.getCustomersWithRecentOrders(date);
        model.addAttribute("customers", customers);
        model.addAttribute("sinceDate", date);
        return "sales/customers/list";
    }
}

package com.envifo_backend_java.Envifo_backend_java.domain.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.CustomerCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.CustomerDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.RegisterCustomerDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Optional<CustomerCompleteDto> getCustomerWithImages(Long idCLiente);
    boolean existsByIdCliente(Long idCliente);
    Optional<CustomerDto> getByIdCLiente(Long idCliente);
    void registerCustomer(RegisterCustomerDto registerCustomerDto);
    void editCustomer(CustomerDto customerDto, MultipartFile file);
    List<CustomerDto> getAllCustomers();
    void delete(Long idCliente) throws IOException;
}

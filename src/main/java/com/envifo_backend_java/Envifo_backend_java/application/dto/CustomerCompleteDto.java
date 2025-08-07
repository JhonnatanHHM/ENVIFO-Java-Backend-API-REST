package com.envifo_backend_java.Envifo_backend_java.application.dto;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CustomerCompleteDto {

    private Long customerId;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String password;
    private String url;
    private boolean stateCustomer;
    private LocalDateTime registrationDate;
    private RolDto rolCustomer;
    private Optional<StorageDto> images;

    // Constructors

    public CustomerCompleteDto() {
    }

    // Getters and Setters

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isStateCustomer() {
        return stateCustomer;
    }

    public void setStateCustomer(boolean stateCustomer) {
        this.stateCustomer = stateCustomer;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public RolDto getRolCustomer() {
        return rolCustomer;
    }

    public void setRolCustomer(RolDto rolCustomer) {
        this.rolCustomer = rolCustomer;
    }

    public Optional<StorageDto> getImages() {
        return images;
    }

    public void setImages(Optional<StorageDto> images) {
        this.images = images;
    }
}


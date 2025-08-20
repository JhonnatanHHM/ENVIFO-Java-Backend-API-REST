package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.*;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.*;
import com.envifo_backend_java.Envifo_backend_java.domain.service.CustomerService;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.ConflictException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.AlmacenamientoRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.ClientesRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.PermisosRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.RolesRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImple implements CustomerService {

    private ClientesRepository clientesRepository;

    private RolServiceImple rolService;

    private RolesRepository rolesRepository;

    private PermisosRepository permisosRepository;

    private AuthenticationManager authenticationManager;

    private JwtUtils jwtUtils;

    private PasswordEncoder passwordEncoder;

    private AlmacenamientoRepository almacenamientoRepository;

    private StorageService storageService;

    @Autowired
    public CustomerServiceImple(StorageService storageService, AlmacenamientoRepository almacenamientoRepository, ClientesRepository clientesRepository, RolServiceImple rolService, RolesRepository rolesRepository, PermisosRepository permisosRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.clientesRepository = clientesRepository;
        this.rolService = rolService;
        this.rolesRepository = rolesRepository;
        this.permisosRepository = permisosRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.almacenamientoRepository = almacenamientoRepository;
        this.storageService = storageService;
    }

    @Override
    public Optional<CustomerCompleteDto> getCustomerWithImages(Long idCliente) {
        // Obtener cliente
        Optional<ClientesEntity> clienteOpt = clientesRepository.getByIdCliente(idCliente);
        if (clienteOpt.isEmpty()) {
            return Optional.empty();
        }

        ClientesEntity cliente = clienteOpt.get();

        // Obtener la imagen del cliente
        Optional<AlmacenamientoEntity> imagenOpt = almacenamientoRepository
                .findByIdEntidadAndTipoEntidad(idCliente, "cliente");

        Optional<StorageDto> imagenDto = imagenOpt.map(imagen -> {
            StorageDto storageDto = new StorageDto();
            storageDto.setIdFile(imagen.getIdArchivo());
            storageDto.setNameFile(imagen.getNombreArchivo());
            storageDto.setIdEntity(imagen.getIdEntidad());

            try {
                String url = storageService.getPresignedUrl(imagen.getIdArchivo());
                storageDto.setKeyR2(url);
            } catch (Exception e) {
                storageDto.setKeyR2("Error al generar URL");
            }

            return storageDto;
        });

// Construcción del DTO
        CustomerCompleteDto dto = new CustomerCompleteDto();
        dto.setCustomerId(cliente.getIdCliente());
        dto.setName(cliente.getNombre());
        dto.setAddress(cliente.getDireccion());
        dto.setPhone(cliente.getTelefono());
        dto.setEmail(cliente.getEmail());
        dto.setPassword(""); // Nunca devolver contraseña
        dto.setUrl(Optional.ofNullable(cliente.getUrl()).orElse(""));
        dto.setStateCustomer(cliente.isEstado());
        dto.setRegistrationDate(cliente.getFechaRegistro());
        dto.setRolCustomer(convertToRolDto(cliente.getRol()));
        dto.setImages(imagenDto);

        return Optional.of(dto);
    }

    @Override
    public boolean existsByIdCliente(Long idCliente) {
        return clientesRepository.existsById(idCliente);
    }

    @Override
    public Optional<CustomerDto> getByIdCLiente(Long idCliente) {
        ClientesEntity customer = clientesRepository.getByIdCliente(idCliente)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado!"));

        return Optional.of(convertToDto(customer));
    }

    @Override
    public void registerCustomer(RegisterCustomerDto registerCustomerDto) {
        if (clientesRepository.getExistsByEmail(registerCustomerDto.getEmail())) {
            throw new ConflictException("El cliente ya existe!");
        }

        ClientesEntity customer = new ClientesEntity();
        customer.setNombre(registerCustomerDto.getName());
        customer.setDireccion(registerCustomerDto.getAddress());
        customer.setTelefono(registerCustomerDto.getPhone());
        customer.setEmail(registerCustomerDto.getEmail());
        customer.setEstado(true);
        customer.setPassword(passwordEncoder.encode(registerCustomerDto.getPassword()));
        customer.setUrl("");

        RolesEntity rol = rolesRepository.getByName("GLOBAL").orElseGet(() -> {
            RolesEntity rolEntity = new RolesEntity();
            rolEntity.setName("GLOBAL");
            rolEntity.setDescription("Usuario con permisos globales");

            PermisosEntity permisos = crearPermisosPorDefecto();
            PermisosEntity savedPermisos = permisosRepository.save(permisos);

            rolEntity.setPermisos(savedPermisos);
            return rolesRepository.save(rolEntity);
        });

        customer.setRol(rol);
        clientesRepository.saveCustomer(customer);

    }

    @Override
    public void editCustomer(CustomerDto customerDto, MultipartFile file) {
        try {
            ClientesEntity customer = clientesRepository.getByIdCliente(customerDto.getCustomerId())
                    .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

            Optional.ofNullable(customerDto.getName()).ifPresent(customer::setNombre);
            Optional.ofNullable(customerDto.getAddress()).ifPresent(customer::setDireccion);
            Optional.ofNullable(customerDto.getPhone()).ifPresent(customer::setTelefono);
            Optional.ofNullable(customerDto.getEmail()).ifPresent(customer::setEmail);
            Optional.ofNullable(customerDto.getUrl()).ifPresent(customer::setUrl);
            Optional.ofNullable(customerDto.isStateCustomer()).filter(state -> state != customer.isEstado()).ifPresent(customer::setEstado);

            Optional.ofNullable(customerDto.getPassword())
                    .filter(pass -> customer.getPassword() != null && !passwordEncoder.matches(pass, customer.getPassword()))
                    .map(passwordEncoder::encode)
                    .ifPresent(customer::setPassword);



            Optional.ofNullable(customerDto.getRolCustomer()).ifPresent(customer::setRol);


            clientesRepository.saveCustomer(customer);

            // Procesar imagen si se envía
            if (file != null && !file.isEmpty()) {
                Optional<AlmacenamientoEntity> optionalImagen =
                        almacenamientoRepository.findByIdEntidadAndTipoEntidad
                                (customer.getIdCliente(), "cliente");

                if (optionalImagen.isPresent()) {
                    storageService.updateFile(optionalImagen.get().getIdArchivo(), file, "imagenes");
                } else {
                    StorageDto dto = new StorageDto();
                    dto.setIdEntity(customer.getIdCliente());

                    storageService.saveFile(file, dto, "cliente", "imagenes");
                }
            }

        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<ClientesEntity> clientes = clientesRepository.getAll();
        return clientes.stream().map(this::convertToCustomerDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long idCliente) throws IOException {

        Optional<ClientesEntity> customerOpt = clientesRepository.getByIdCliente(idCliente);

        if (!customerOpt.isPresent()) {
            throw new NotFoundException("Cliente no encontrado por ID: " + idCliente);
        }

        // Intentar eliminar la imagen asociada, pero no detener el proceso si falla
        try {
            storageService.deleteFileByEntity(idCliente, "cliente");
        } catch (Exception e) {
            // Puedes registrar el error si lo deseas, pero no interrumpir la ejecución
            System.err.println("No se pudo eliminar la imagen asociada al usuario: " + e.getMessage());
        }

        ClientesEntity customer = customerOpt.get();

        // Inhabilitar el cliente
        customer.setEstado(false);

        clientesRepository.saveCustomer(customer);
    }

    private CustomerDto convertToCustomerDto(ClientesEntity cliente) {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(cliente.getIdCliente());
        dto.setName(cliente.getNombre());
        dto.setAddress(cliente.getDireccion());
        dto.setPhone(cliente.getTelefono());
        dto.setEmail(cliente.getEmail());
        dto.setPassword("");
        dto.setUrl(cliente.getUrl());
        dto.setStateCustomer(cliente.isEstado());
        dto.setRegistrationDate(cliente.getFechaRegistro());
        dto.setRolCustomer(cliente.getRol());

        return dto;
    }


    private RolDto convertToRolDto(RolesEntity rol) {
        RolDto rolDto = new RolDto();
        rolDto.setIdRol(rol.getIdRol());
        rolDto.setName(rol.getName());
        rolDto.setDescription(rol.getDescription());
        rolDto.setPermisos(convertToPermissionsDto(rol.getPermisos()));
        return rolDto;
    }

    private RolesEntity convertToRolesEntity(RolDto rolDto) {
        RolesEntity rolesEntity = new RolesEntity();
        rolesEntity.setIdRol(rolDto.getIdRol());
        rolesEntity.setName(rolDto.getName());
        rolesEntity.setDescription(rolDto.getDescription());
        rolesEntity.setPermisos(convertToPermisosEntity(rolDto.getPermisos()));
        return rolesEntity;
    }

    private CustomerDto convertToDto(ClientesEntity cliente) {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(cliente.getIdCliente());
        dto.setName(cliente.getNombre());
        dto.setAddress(cliente.getDireccion());
        dto.setPhone(cliente.getTelefono());
        dto.setEmail(cliente.getEmail());
        dto.setPassword("");
        dto.setUrl(cliente.getUrl());
        dto.setStateCustomer(cliente.isEstado());
        dto.setRegistrationDate(cliente.getFechaRegistro());
        dto.setRolCustomer(cliente.getRol());

        return dto;
    }

    private PermisosEntity crearPermisosPorDefecto() {
        PermisosEntity permisos = new PermisosEntity();
        permisos.setEditPermisos(true);
        permisos.setVistaUsuarios(true);
        permisos.setEditUsuarios(true);
        permisos.setVistaProyectos(true);
        permisos.setEditProyectos(true);
        permisos.setVistaDisenios3d(true);
        permisos.setEditDisenios3d(true);
        permisos.setVistaMateriales(true);
        permisos.setEditMateriales(true);
        permisos.setVistaInformes(true);
        permisos.setVistaCategorias(true);
        permisos.setEditCategorias(true);
        return permisos;
    }

    private PermissionsDto convertToPermissionsDto (PermisosEntity permisos) {
        PermissionsDto dto = new PermissionsDto();
        dto.setEditPermisos(permisos.isEditPermisos());
        dto.setVistaUsuarios(permisos.isVistaUsuarios());
        dto.setEditUsuarios(permisos.isEditUsuarios());
        dto.setVistaProyectos(permisos.isVistaProyectos());
        dto.setEditProyectos(permisos.isEditProyectos());
        dto.setVistaDisenios3d(permisos.isEditPermisos());
        dto.setEditDisenios3d(permisos.isEditDisenios3d());
        dto.setVistaMateriales(permisos.isVistaMateriales());
        dto.setEditMateriales(permisos.isEditMateriales());
        dto.setVistaInformes(permisos.isEditPermisos());
        dto.setVistaCategorias(permisos.isEditPermisos());
        dto.setEditCategorias(permisos.isEditCategorias());

        return dto;
    }

    private PermisosEntity convertToPermisosEntity(PermissionsDto dto) {
        PermisosEntity permisos = new PermisosEntity();
        permisos.setEditPermisos(dto.isEditPermisos());
        permisos.setVistaUsuarios(dto.isVistaUsuarios());
        permisos.setEditUsuarios(dto.isEditUsuarios());
        permisos.setVistaProyectos(dto.isVistaProyectos());
        permisos.setEditProyectos(dto.isEditProyectos());
        permisos.setVistaDisenios3d(dto.isVistaDisenios3d());
        permisos.setEditDisenios3d(dto.isEditDisenios3d());
        permisos.setVistaMateriales(dto.isVistaMateriales());
        permisos.setEditMateriales(dto.isEditMateriales());
        permisos.setVistaInformes(dto.isVistaInformes());
        permisos.setVistaCategorias(dto.isVistaCategorias());
        permisos.setEditCategorias(dto.isEditCategorias());

        return permisos;
    }

}

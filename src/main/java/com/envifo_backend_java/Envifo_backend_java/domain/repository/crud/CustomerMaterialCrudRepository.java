package com.envifo_backend_java.Envifo_backend_java.domain.repository.crud;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClienteMaterialEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.MaterialesEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CustomerMaterialCrudRepository extends JpaRepository<ClienteMaterialEntity, Long> {

    // Devuelve todos los materiales asociados a un cliente
    @Query("SELECT cm.material FROM ClienteMaterialEntity cm LEFT JOIN FETCH cm.material.textura WHERE cm.cliente.idCliente = :idCliente")
    List<MaterialesEntity> findMaterialesByClienteId(@Param("idCliente") Long idCliente);

    @Modifying
    @Transactional
    @Query("DELETE FROM ClienteMaterialEntity cm WHERE cm.material.idMaterial = :idMaterial")
    void deleteByMaterialId(Long idMaterial);

    @Query("SELECT cm.material FROM ClienteMaterialEntity cm " +
            "WHERE cm.cliente.idCliente = :idCliente " +
            "ORDER BY cm.fechaRegistro DESC")
    List<MaterialesEntity> findUltimoMaterialByCliente(@Param("idCliente") Long idCliente, Pageable pageable);


}

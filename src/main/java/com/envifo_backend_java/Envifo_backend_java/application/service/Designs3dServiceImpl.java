package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.Designs3dDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.Disenios3dEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.Designs3dRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.service.Designs3dService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Optional;

@Service
public class Designs3dServiceImpl implements Designs3dService {

    private final Designs3dRepository designs3dRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public Designs3dServiceImpl(Designs3dRepository designs3dRepository, ObjectMapper objectMapper) {
        this.designs3dRepository = designs3dRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Disenios3dEntity saveDesign(Designs3dDto designDto) {
        Disenios3dEntity disenio = new Disenios3dEntity();

        // Manejar configuracion como JSON
        JsonNode configNode = parseToJsonNode(designDto.getConfiguracion());
        disenio.setConfiguracion(configNode);

        // Manejar materiales: convertir cadena "[6]" o lista a ArrayNode
        JsonNode materialesNode = parseToArrayNode(designDto.getMateriales());
        disenio.setMateriales(materialesNode);

        // Manejar objetos: similar
        JsonNode objetosNode = parseToArrayNode(designDto.getObjetos());
        disenio.setObjetos(objetosNode);

        return designs3dRepository.saveDesign(disenio);
    }

    @Override
    public Disenios3dEntity updateDesign(Designs3dDto designDto) {
        Disenios3dEntity existingDisenio = designs3dRepository.getDesignById(designDto.getIdDisenio())
                .orElseThrow(() -> new RuntimeException("Diseño no encontrado"));

        // Actualizar campos si se proporcionan
        if (designDto.getConfiguracion() != null) {
            existingDisenio.setConfiguracion(parseToJsonNode(designDto.getConfiguracion()));
        }
        if (designDto.getMateriales() != null) {
            existingDisenio.setMateriales(parseToArrayNode(designDto.getMateriales()));
        }
        if (designDto.getObjetos() != null) {
            existingDisenio.setObjetos(parseToArrayNode(designDto.getObjetos()));
        }

        return designs3dRepository.saveDesign(existingDisenio);
    }

    // Método auxiliar para parsear a ObjectNode o cualquier JsonNode
    private JsonNode parseToJsonNode(Object input) {
        if (input == null) return null;
        try {
            if (input instanceof String) {
                return objectMapper.readTree((String) input);
            } else {
                return objectMapper.valueToTree(input);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear JSON: " + e.getMessage());
        }
    }

    // Método auxiliar para parsear a ArrayNode
    private ArrayNode parseToArrayNode(Object input) {
        if (input == null) return objectMapper.createArrayNode();
        try {
            if (input instanceof String) {
                // Si es una cadena como "[6]", parsearla a lista y luego a ArrayNode
                String str = (String) input;
                if (str.trim().startsWith("[") && str.trim().endsWith("]")) {
                    // Parsear la cadena JSON a una lista de objetos
                    java.util.List<?> list = objectMapper.readValue(str, java.util.List.class);
                    ArrayNode arrayNode = objectMapper.createArrayNode();
                    for (Object item : list) {
                        if (item instanceof Number) {
                            arrayNode.add(((Number) item).longValue());
                        } else {
                            arrayNode.add(item.toString());
                        }
                    }
                    return arrayNode;
                } else {
                    // Si no es un arreglo, crear uno vacío o manejar error
                    return objectMapper.createArrayNode();
                }
            } else if (input instanceof java.util.List) {
                ArrayNode arrayNode = objectMapper.createArrayNode();
                for (Object item : (java.util.List<?>) input) {
                    if (item instanceof Number) {
                        arrayNode.add(((Number) item).longValue());
                    } else {
                        arrayNode.add(item.toString());
                    }
                }
                return arrayNode;
            } else {
                return objectMapper.createArrayNode();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear ArrayNode: " + e.getMessage());
        }
    }

    @Override
    public void deleteDesign(Long id) {
        designs3dRepository.deleteDesign(id);
    }

}
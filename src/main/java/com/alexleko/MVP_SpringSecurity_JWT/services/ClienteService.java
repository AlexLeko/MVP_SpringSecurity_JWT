package com.alexleko.MVP_SpringSecurity_JWT.services;

import com.alexleko.MVP_SpringSecurity_JWT.domain.Cliente;
import com.alexleko.MVP_SpringSecurity_JWT.dto.ClienteNewDTO;

import java.util.List;

public interface ClienteService {

    Cliente insert(Cliente cliente);

    List<Cliente> findAll();

    Cliente convertFromDTO(ClienteNewDTO dto);

}

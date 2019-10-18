package com.alexleko.MVP_SpringSecurity_JWT.services;

import com.alexleko.MVP_SpringSecurity_JWT.domain.Cliente;
import com.alexleko.MVP_SpringSecurity_JWT.dto.ClienteNewDTO;

public interface ClienteService {

    Cliente insert(Cliente cliente);

    Cliente convertFromDTO(ClienteNewDTO dto);

}

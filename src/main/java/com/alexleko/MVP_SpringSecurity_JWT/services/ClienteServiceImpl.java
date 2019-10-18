package com.alexleko.MVP_SpringSecurity_JWT.services;

import com.alexleko.MVP_SpringSecurity_JWT.domain.Cliente;
import com.alexleko.MVP_SpringSecurity_JWT.dto.ClienteNewDTO;
import com.alexleko.MVP_SpringSecurity_JWT.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Cliente insert(Cliente cliente){
        cliente.setId(null);
        cliente = clienteRepository.save(cliente);

        return cliente;
    }

    @Override
    public Cliente convertFromDTO(ClienteNewDTO dto){
        Cliente cliente = new Cliente(null, dto.getNome(), dto.getEmail(), passwordEncoder.encode(dto.getSenha()));
        return cliente;
    }

}


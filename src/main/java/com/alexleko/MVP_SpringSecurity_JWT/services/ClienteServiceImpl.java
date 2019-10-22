package com.alexleko.MVP_SpringSecurity_JWT.services;

import com.alexleko.MVP_SpringSecurity_JWT.domain.Cliente;
import com.alexleko.MVP_SpringSecurity_JWT.dto.ClienteNewDTO;
import com.alexleko.MVP_SpringSecurity_JWT.repositories.ClienteRepository;
import com.alexleko.MVP_SpringSecurity_JWT.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


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

    // Teste para validar acesso publico.
    public List<Cliente> findAll(){
        return clienteRepository.findAll();
    }

    // Teste para validar Hole de acesso somente para PERFIL = ADMIN
    public void delete(Integer id){
        Optional<Cliente> cliente = clienteRepository.findById(id);

        try{
            clienteRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException ex){
            throw new DataIntegrityViolationException("Não é possível excluir um cliente com pedidos em aberto.");
        }
    }


    @Override
    public Cliente convertFromDTO(ClienteNewDTO dto){
        Cliente cliente = new Cliente(null, dto.getNome(), dto.getEmail(), passwordEncoder.encode(dto.getSenha()));
        return cliente;
    }



}




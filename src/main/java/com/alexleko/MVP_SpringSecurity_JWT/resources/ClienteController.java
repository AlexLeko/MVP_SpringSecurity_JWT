package com.alexleko.MVP_SpringSecurity_JWT.resources;

import com.alexleko.MVP_SpringSecurity_JWT.domain.Cliente;
import com.alexleko.MVP_SpringSecurity_JWT.dto.ClienteNewDTO;
import com.alexleko.MVP_SpringSecurity_JWT.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO clienteDTO){
        Cliente cliente = clienteService.convertFromDTO(clienteDTO);
        cliente = clienteService.insert(cliente);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cliente.getId())
                .toUri();

        // retorna a URI do endpoint criado.
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<Cliente>> findAll() {
        List<Cliente> list = clienteService.findAll();
        return ResponseEntity.ok().body(list); // status: 200 OK
    }


}
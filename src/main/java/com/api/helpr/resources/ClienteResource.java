package com.api.helpr.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.helpr.domain.Cliente;
import com.api.helpr.domain.dtos.ClienteDTO;
import com.api.helpr.services.ClienteService;

@RestController
@RequestMapping(value = "/service/clientes")
public class ClienteResource {

	@Autowired
	private ClienteService service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<ClienteDTO> findById(@PathVariable Integer id){
		Cliente obj = service.findById(id);
		return ResponseEntity.ok().body(new ClienteDTO(obj));
	}
	
	@GetMapping
	public ResponseEntity<List<ClienteDTO>> findAll(){
		List <Cliente> list = service.findAllClientes();
		List <ClienteDTO> listDTO = list.stream()
				.map(cli -> new ClienteDTO(cli)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
		
		}
	
	@PostMapping
	public ResponseEntity<ClienteDTO> createCliente(@Valid @RequestBody ClienteDTO objDTO){
		Cliente newObj = service.create(objDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
		.buildAndExpand(newObj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value ="/{id}")
	public ResponseEntity<ClienteDTO> updateCliente(@PathVariable Integer id,
			@RequestBody ClienteDTO objDTO){
	Cliente obj = service.update(id,objDTO);
	return ResponseEntity.ok().body(new ClienteDTO(obj));
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity <ClienteDTO> deleteCliente(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
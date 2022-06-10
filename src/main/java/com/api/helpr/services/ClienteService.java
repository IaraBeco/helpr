package com.api.helpr.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.helpr.domain.Cliente;
import com.api.helpr.domain.Pessoa;
import com.api.helpr.domain.dtos.ClienteDTO;
import com.api.helpr.repositories.ClienteRepository;
import com.api.helpr.repositories.PessoaRepository;
import com.api.helpr.services.exceptions.DataIntegrityViolationException;
import com.api.helpr.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repository;

	@Autowired 
	private PessoaRepository pessoaRepository;
	
	public Cliente findById(Integer id) {
	Optional<Cliente> obj = repository.findById(id);
	return obj.orElseThrow(() -> new ObjectNotFoundException("O objeto não foi encontrado; "+ id));
	}
	
	public List<Cliente> findAllClientes() {
		return repository.findAll();
	}
	
	public Cliente create(ClienteDTO objDto) {
		objDto.setId(null);
		validaCpfEEmail(objDto);
		Cliente newObj = new Cliente(objDto);
		return repository.save(newObj);
	}
	
	public Cliente update(Integer id, ClienteDTO objDTO) {
		objDTO.setId(id);;
		Cliente oldObj = findById(id);
		validaCpfEEmail(objDTO);
		oldObj =  new Cliente(objDTO);
		return repository.save(oldObj);
	}
	
	public void delete(Integer id) {
		Cliente clienteObj=findById(id);
		if(clienteObj.getChamados().size() >0){
			throw new DataIntegrityViolationException("O Cliente: "+
					id+" tem chamados no sistema, chamados: "+ clienteObj.getChamados().size());
		}
		repository.deleteById(id);
	}

	private void validaCpfEEmail(ClienteDTO objDto) {

		Optional<Pessoa> obj = pessoaRepository.findByCpf(objDto.getCpf());
		if (obj.isPresent() && obj.get().getId() != objDto.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
		}

		obj = pessoaRepository.findByEmail(objDto.getEmail());
		if (obj.isPresent() && obj.get().getId() != objDto.getId()) {
			throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
		}
	}
}

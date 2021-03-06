package br.com.sale.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sale.domain.Client;
import br.com.sale.repositories.AddressRepository;
import br.com.sale.repositories.ClientRepository;
import br.com.sale.services.exception.DataIntegrityException;
import br.com.sale.services.exception.ObjectNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repo;
	
	@Autowired
	private AddressRepository addRepo;

	@Transactional
	public Client insert(Client client) {
		client.setId(null);
		client = repo.save(client);
		addRepo.saveAll(client.getAddress());
		return client;
	}
	
	public Client update(Client client) {
	Client newEntity = findById(client.getId());
	updateData(newEntity, client);
		 return repo.save(newEntity);
	}

	public void delete(Long id) {
		findById(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("O cliente possui pedidos relacionados, não é possivel exclui-lo");
		}
	}

	public Client findById(Long id) {
		Optional<Client> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Client.class.getName()));
	}

	public List<Client> findAll() {
		return repo.findAll();
	}

	public Page<Client> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	private void updateData(Client newObject, Client obj) {
		newObject.setEmail(obj.getEmail());
		newObject.setName(obj.getName());
	}
}

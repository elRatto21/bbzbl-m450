package com.ronanski11.teezinator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ronanski11.teezinator.dto.ConsumedTeaDto;
import com.ronanski11.teezinator.model.ConsumedTea;
import com.ronanski11.teezinator.model.Role;
import com.ronanski11.teezinator.model.Tea;
import com.ronanski11.teezinator.repository.TeaRepository;
import com.ronanski11.teezinator.security.AuthenticationService;
import com.ronanski11.teezinator.service.TeaService;

@RestController
@RequestMapping("/api/tea")
public class TeaController {

	@Autowired
	private TeaService teaService;

	@Autowired
	AuthenticationService auth;

	@Autowired
	TeaRepository t;

	@PostMapping()
	public ResponseEntity<?> addTea(@RequestBody ConsumedTeaDto consumedTeaDto) {
		String username = auth.getUsername();
		return new ResponseEntity<>(
				"tea with id " + this.teaService.addTea(consumedTeaDto, username).getId() + " added", HttpStatus.OK);
	}

	@GetMapping("/getall")
	public List<Tea> helloWorld() {
		return teaService.getAllTeas();
	}

	@GetMapping("/getTea")
	public Tea getTeaById(@RequestParam(name = "teaId", required = true) String teaId) {
		return teaService.findById(teaId);
	}

	@GetMapping("/getUserRole")
	public ResponseEntity<Role> getUserRole() {
		return ResponseEntity.ok(teaService.getUserRole(auth.getUsername()));
	}

	@GetMapping("/getAllUsernames")
	public ResponseEntity<List<String>> getAllUsernames() {
		if (teaService.getUserRole(auth.getUsername()) != Role.ADMIN) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		} else {
			return ResponseEntity.ok(teaService.getAllUsernames());
		}
	}

	@GetMapping("getMultipleById")
	public ResponseEntity<List<Tea>> getMultipleById(@RequestParam String[] ids) {
		return ResponseEntity.ok(teaService.getMultipleById(ids));
	}

	@GetMapping("/getCTea")
	public ConsumedTea getCTeaById(@RequestParam(name = "cTeaId", required = true) String cTeaId) {
		return teaService.findCTeaById(cTeaId);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<ConsumedTea> updateConsumedTea(@PathVariable String id, @RequestBody ConsumedTea cTea) {
		cTea.setId(id);

		ConsumedTea updatedCTea = teaService.updateCTea(cTea);

		return ResponseEntity.ok(updatedCTea);
	}
	
	@DeleteMapping("")
	public ResponseEntity<HttpStatus> deleteCTea(@RequestParam String cTeaId) {
		teaService.deleteCTeaById(cTeaId);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
	
	@DeleteMapping("delete")
	public ResponseEntity<HttpStatus> deleteTea(@RequestParam String teaId) {
		teaService.deleteTeaById(teaId);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}

}

package is.lessons.spring.pizzeria.restcontroller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import is.lessons.spring.pizzeria.model.Pizza;
import is.lessons.spring.pizzeria.repository.PizzeRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/pizze")
public class PizzeRestController {
	
	@Autowired
	private PizzeRepository pizzeRepo;
	
	//metodo READ con filtro di ricerca
	@GetMapping
	public ResponseEntity <List<Pizza>> index(@RequestParam(name = "keyword", required = false) String keyword){
		
			if(keyword != null && !keyword.isBlank()) {
			return new ResponseEntity<List<Pizza>>(pizzeRepo.findByNomePizzaOrDescrizioneContaining(keyword, keyword), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<List<Pizza>>(pizzeRepo.findAll(), HttpStatus.OK);
		}
		
	}
	
	
	//metodo READ info Pizza con gestione errore
	@GetMapping("{id}")
	public ResponseEntity <Pizza> getPizza(@PathVariable ("id") Integer id) {
		Optional <Pizza> pizza = pizzeRepo.findById(id);
		if(pizza.isPresent()) {
			return new ResponseEntity<Pizza>(pizza.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<Pizza>(HttpStatus.NOT_FOUND);
		}
	}
	
	

}

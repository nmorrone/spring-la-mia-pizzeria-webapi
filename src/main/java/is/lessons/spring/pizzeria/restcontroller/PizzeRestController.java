package is.lessons.spring.pizzeria.restcontroller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import is.lessons.spring.pizzeria.model.Pizza;
import is.lessons.spring.pizzeria.repository.PizzeRepository;
import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/pizze")
public class PizzeRestController {
	
	@Autowired
	private PizzeRepository pizzeRepo;
	
	//API Metodo READ Pizza + Keyword (Opzionale)
	@GetMapping
	public ResponseEntity <List<Pizza>> index(@RequestParam(name = "keyword", required = false) String keyword){
		
			if(keyword != null && !keyword.isBlank()) {
			return new ResponseEntity<List<Pizza>>(pizzeRepo.findByNomePizzaOrDescrizioneContaining(keyword, keyword), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<List<Pizza>>(pizzeRepo.findAll(), HttpStatus.OK);
		}
		
	}
	
	
	//API Metodo READ Pizza Singola
	@GetMapping("{id}")
	public ResponseEntity <Pizza> getPizza(@PathVariable ("id") Integer id) {
		Optional <Pizza> pizza = pizzeRepo.findById(id);
		if(pizza.isPresent()) {
			return new ResponseEntity<Pizza>(pizza.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<Pizza>(HttpStatus.NOT_FOUND);
		}
	}
	
	//API Metodo UPDATE Pizza
	@PutMapping("{id}")
	public ResponseEntity <Pizza> updatePizza(@PathVariable Integer id, @RequestBody Pizza pizza){
		
		try {
			Optional <Pizza> pizzaById = pizzeRepo.findById(id);
			Pizza modifichePizza = pizzaById.get();
			modifichePizza.setPrezzo(pizza.getPrezzo());
			pizzeRepo.save(modifichePizza);
			return ResponseEntity.ok(modifichePizza);
			
		}
		catch(Exception e){}
		return ResponseEntity.notFound().build();
	}
	
	//API Metodo DELETE Pizza
	@DeleteMapping("{id}")
	public void deletePizza(@PathVariable Integer id){
		
		try {
			pizzeRepo.deleteById(id);
		}
		catch (Exception e) {
			System.err.println("Impossibile effettuare la cancellazione: " + e);
			
		}
		
	}
	
	//API Metodo POST Pizza
	@PostMapping
	public void postPizza(@Valid @RequestBody Pizza pizza){
		pizzeRepo.save(pizza);
	}

}

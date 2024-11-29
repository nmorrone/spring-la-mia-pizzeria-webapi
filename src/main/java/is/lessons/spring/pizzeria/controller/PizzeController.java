package is.lessons.spring.pizzeria.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import is.lessons.spring.pizzeria.model.Bevande;
import is.lessons.spring.pizzeria.model.Coupon;
import is.lessons.spring.pizzeria.model.Fritti;
import is.lessons.spring.pizzeria.model.Pizza;
import is.lessons.spring.pizzeria.repository.BevandeRepository;
import is.lessons.spring.pizzeria.repository.CouponRepository;
import is.lessons.spring.pizzeria.repository.FrittiRepository;
import is.lessons.spring.pizzeria.repository.IngredientiRepository;
import is.lessons.spring.pizzeria.repository.PizzeRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/")
public class PizzeController {

	@Autowired
	private PizzeRepository pizzeRepo;
	@Autowired
	private FrittiRepository frittiRepo;
	@Autowired
	private BevandeRepository bevandeRepo;
	@Autowired
	private CouponRepository couponRepo;
	@Autowired
	private IngredientiRepository ingredientiRepo;

	//metodo display pizze e menu
	@GetMapping("/pizze")
	public String indexPizze(Authentication authentication,@RequestParam(name = "keyword", required = false) String keyword, Model model) {

		List<Pizza> elencoPizze;
		List<Bevande> elencoBevande;
		List<Fritti> elencoFritti;

		if (keyword != null && !keyword.isBlank()) {

			elencoPizze = pizzeRepo.findByNomePizzaOrDescrizioneContaining(keyword, keyword);
			elencoBevande = bevandeRepo.findByNomeBevandaContaining(keyword);
			elencoFritti = frittiRepo.findByNomeFrittoContaining(keyword);
			model.addAttribute("keyword", keyword);

		} else {
			elencoPizze = pizzeRepo.findAll();
			elencoBevande = bevandeRepo.findAll();
			elencoFritti = frittiRepo.findAll();
		}

		model.addAttribute("pizze", elencoPizze);
		model.addAttribute("fritti", elencoFritti);
		model.addAttribute("bevande", elencoBevande);
		return "pizze/lista-pizze";
	}

	// metodo display singola pizza
	@GetMapping("/pizze/{id}")
	public String infoPizza(@PathVariable Integer id, Model model) {
		Optional<Pizza> infoPizza = pizzeRepo.findById(id);
		if (infoPizza.isPresent()) {
			model.addAttribute("infoPizza", infoPizza.get());
			model.addAttribute("verifica", 1);
		} else {

			// controllare TH:IF Condizione per Display
			model.addAttribute("verifica", 0);
		}
		return "pizze/info-pizza";
	}

	//display form creazione pizza e store in DB
	@GetMapping("/pizze/crea-pizza")
	public String creaPizza(Model model) {
		model.addAttribute("pizza", new Pizza());
		model.addAttribute("listaIngredienti", ingredientiRepo.findAll());
		return "pizze/crea-pizza";
	}

	@PostMapping("/pizze/crea-pizza")
	public String storePizza(@Valid @ModelAttribute("pizza") Pizza pizzaForm, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			return "pizze/crea-pizza";
		}
		pizzeRepo.save(pizzaForm);
		return "redirect:/pizze";
	}

	//metodi modifica pizza esistente e aggiornamento dati DB
	@GetMapping("/pizze/modifica-pizza/{id}")
	public String modificaPizza(@PathVariable("id") Integer id, Model model) {
		Optional<Pizza> infoPizza = pizzeRepo.findById(id);
		if (infoPizza.isPresent()) {
			model.addAttribute("pizza", infoPizza.get());
			model.addAttribute("listaIngredienti", ingredientiRepo.findAll());
		} else {
			// controllare TH:IF Condizione per Display
			model.addAttribute("verifica", 0);
		}
		return "/pizze/modifica-pizza";
	}

	@PostMapping("/pizze/modifica-pizza/{id}")
	public String aggiornaPizza(@PathVariable Integer id, @Valid @ModelAttribute("pizza") Pizza pizzaForm,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "pizze/modifica-pizza";
		}

		pizzeRepo.save(pizzaForm);

		return "redirect:/pizze";
	}
	//metodo rimozione pizza su DB
	@PostMapping("/pizze/rimuovi/{id}")
	public String rimuoviPizza(@PathVariable Integer id) {

		pizzeRepo.deleteById(id);

		return "redirect:/pizze";
	}

	//metodi display form coupon e store in DB
	@GetMapping("/pizze/crea-offerta/{id}")
	public String creaCoupon(@PathVariable("id") Integer id, Model model) {

		Pizza pizza = pizzeRepo.findById(id).get();
		Coupon offerta = new Coupon();
		offerta.setPizza(pizza);

		model.addAttribute("offerta", offerta);

		return "/pizze/crea-offerta";
	}

	@PostMapping("/pizze/crea-offerta/{id}")
	public String storeCoupon(@PathVariable("id") Integer id, @Valid @ModelAttribute("coupon") Coupon couponForm,
			BindingResult bindingResults, Model model) {
		if (bindingResults.hasErrors()) {
			return "/pizze/crea-offerta";
		}
		couponRepo.save(couponForm);
		return "redirect:/pizze/" + couponForm.getPizza().getId();
	}

	//metodi modifica coupon e aggiornamento DB
	@GetMapping("/pizze/modifica-offerta/{id}")
	public String modificaCoupon(@PathVariable("id") Integer id, Model model) {

		Optional<Coupon> offerta = couponRepo.findById(id);
		if (offerta.isPresent()) {
			model.addAttribute("offerta", offerta.get());
		} else {
			model.addAttribute("verifica", 0);
		}
		return "/pizze/modifica-offerta";
	}

	@PostMapping("/pizze/modifica-offerta/{id}")
	public String modificaCoupon(@PathVariable("id") Integer id, @Valid @ModelAttribute("coupon") Coupon couponForm,
			BindingResult bindingResults, Model model) {

		if (bindingResults.hasErrors()) {
			return "/pizze/modifica-offerta";
		}

		couponRepo.save(couponForm);
		return "redirect:/pizze/" + couponForm.getPizza().getId();
	}

}

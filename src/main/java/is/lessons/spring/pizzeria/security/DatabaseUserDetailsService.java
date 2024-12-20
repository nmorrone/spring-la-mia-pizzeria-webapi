package is.lessons.spring.pizzeria.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import is.lessons.spring.pizzeria.model.User;
import is.lessons.spring.pizzeria.repository.UserRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		Optional<User> userByUsername = userRepo.findByUsername(username);
		
		if(userByUsername.isPresent()) {
			
			return new DatabaseUserDetails(userByUsername.get());
		}
		else{
			throw new UsernameNotFoundException("Non è registrato nessun utente con questo username");
			
		}
	}

}

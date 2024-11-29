package is.lessons.spring.pizzeria.security;

import java.util.Collection;
import java.util.HashSet;

import org.apache.catalina.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import is.lessons.spring.pizzeria.model.Roles;
import is.lessons.spring.pizzeria.model.User;

public class DatabaseUserDetails implements UserDetails {
	
	
	private final Integer id;
	private final String username;
	private final String password;
	private final HashSet<GrantedAuthority> authorities;
	
	
	public DatabaseUserDetails(User user) {
		
		this.id = user.getId();
		this.username = user.getUsername();
		this.password = user.getPassword();
		authorities = new HashSet<GrantedAuthority>();
		
		for(Roles role : user.getRoles()) {
			
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.authorities;
	}


	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}


	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}


	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}


	

}

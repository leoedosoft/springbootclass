/**
import com.teoresi.blogdataapi.model.Role;
import com.teoresi.blogdataapi.model.User;
import com.teoresi.blogdataapi.repository.IUserRepository;
import com.teoresi.blogdataapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
 
	 
	@Autowired
	private UserService userService;
	
	@Autowired
	private IUserRepository userRepository;
	
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		
		if(mail == null) {
			throw new UsernameNotFoundException("Devono essere inserite la mail");
		}
		
		User userModel =  userRepository.selectUserByMailAndIdUser(mail);
		
		if(userModel == null) {
			throw  new UsernameNotFoundException("Utente non trovato!!");
		}
		
		UserBuilder userBuilder = null;
		userBuilder = org.springframework.security.core.userdetails.User.withUsername(userModel.getMail());
		userBuilder.disabled(false);
		userBuilder.password(userModel.getPassword());
		userBuilder.authorities(getAuthorities(userModel));
		
		return userBuilder.build();
	}
	
	private Collection<GrantedAuthority> getAuthorities(User user){
		List<Role> roles = user.getRoles();
		Collection<GrantedAuthority> authorities = new ArrayList<>(roles.size());
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority("ROLE_"+	role.getTipo()));
		}
		return authorities;
	}

}

 */
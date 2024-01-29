package com.teoresi.blogwebapp.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.teoresi.blogwebapp.model.Role;
import com.teoresi.blogwebapp.model.User;
import com.teoresi.blogwebapp.repository.IUserRepository;
import com.teoresi.blogwebapp.service.RoleService;
import com.teoresi.blogwebapp.service.UserService;

@Controller
public class UserController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public String iniciarSesion() {
		return "login";
	}
	
	private static final List<String> selectRoles = Collections.unmodifiableList(Arrays.asList("USER", "MODERATOR", "ADMIN"));
	
	@RequestMapping("/register")
	public String newUser(Model model) {
		try {
			User user = new User();
			model.addAttribute("user", user);
		} catch (Exception e) {
			throw new RuntimeException("newUser: Verificare creazione User! "+ e);
		}
		return "new_user";
	}

	@RequestMapping(value = "/saveRegister", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveRegister(@Valid @ModelAttribute("user") User user, BindingResult result) {
		try {
			if(result.hasErrors()) {
				return "new_user";
			}
			
			Role role = new Role();
			//CRITTIAMO LA PASSWORD
			String crypPassword = bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(crypPassword);
			user = userRepository.save(user);
			role.setTipo("USER");
			role.setUser(user);
			roleService.save(role);
		} catch (Exception e) {
			throw new RuntimeException("saveRegister: Verificare registrazione User! "+ e);
		}
		return "redirect:/login";
	}
	
	@RequestMapping("/newAdmin")
	public String newAdmin(Model model) {
		try {
			Role role = new Role();
			User user = new User();
			//CRITTIAMO LA PASSWORD
			user.setPassword(bCryptPasswordEncoder.encode("admin"));
			user.setName("admin");
			user.setSurname("admin");
			user.setMail("admin@test");
			user = userRepository.save(user);
			role.setTipo("ADMIN");
			role.setUser(user);
			roleService.save(role);
		} catch (Exception e) {
			throw new RuntimeException("createAdmin: Verificare creazione admin! "+ e);
		}
		return "redirect:/login";
	}
	
	@RequestMapping("/newModerator")
	public String newModerator(Model model) {
		try {
			Role role = new Role();
			User user = new User();
			//CRITTIAMO LA PASSWORD
			user.setPassword(bCryptPasswordEncoder.encode("moderator"));
			user.setName("moderator");
			user.setSurname("moderator");
			user.setMail("moderator@test.com");
			user = userRepository.save(user);
			role.setTipo("MODERATOR");
			role.setUser(user);
			roleService.save(role);
		} catch (Exception e) {
			throw new RuntimeException("create MODERATOR: Verificare creazione MODERATOR! "+ e);
		}
		return "redirect:/login";
	}
	
	@RequestMapping("/users")
	public String getUserList(Model model) {
		model.addAttribute("pageTitle", "Users");
		return "list_user";
	}
	
	@RequestMapping("/deleteUser/{iduser}")
	public String deletetUser(@PathVariable(name="iduser") String iduser) {
		try {
			User user = new User();
			Role role = new Role();
			user = userRepository.getById(iduser);
			if(user!= null) {
				role = user.getRoles().get(0);
				if(role != null) {
					roleService.delete(role.getIdRole());
					userRepository.delete(user);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("deletetUser: Verificare delete user!"+e);
		}
		return "redirect:/users";
	}
	
	@RequestMapping(value="/editUser2222/{iduser}", method = RequestMethod.GET)
	public String editUser(@PathVariable(name="iduser") String iduser, Model model) {
		try {
			User user = userService.get(iduser);
			user.setTypeRole(user.getRoles().get(0).getTipo());
			model.addAttribute("user", user);
			model.addAttribute("selectRoles", selectRoles);
		} catch (Exception e) {
			throw new RuntimeException("editUser: Verificare Modifica - User!"+e);
		}
		return "edit_user";
	}
	
	@RequestMapping(value="/editUser/{iduser}", method = RequestMethod.GET)
	public ModelAndView editUser(@PathVariable(name="iduser") String iduser) {
		ModelAndView modelAndView = new ModelAndView("edit_user");
		try {
			User user = userService.get(iduser);
			user.setTypeRole(user.getRoles().get(0).getTipo());
			//user.setRoles(user.getRoles());
			modelAndView.addObject("user", user);
			modelAndView.addObject("selectRoles", selectRoles);
		} catch (Exception e) {
			throw new RuntimeException("editUser: Verificare Modifica - User!"+e);
		}
		return modelAndView;
	}
	
	@RequestMapping(value = "/saveUser", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
		try {
			model.addAttribute("selectRoles", selectRoles);
			if(result.hasErrors()) {
				return "edit_user";
			}
			String valueTypeRole = user.getTypeRole();
			if(!user.getTypeRole().equals(user.getRoles().get(0).getTipo())) {
				Role role = user.getRoles().get(0);
				role.setTipo(valueTypeRole);
				roleService.save(role);
			}
			user = userRepository.save(user);
		} catch (Exception e) {
			throw new RuntimeException("saveUser: Verificare Modifica User! "+ e);
		}
		return "redirect:/users";
	}
	
}

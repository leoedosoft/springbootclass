package com.teoresi.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.teoresi.webapp.exception.BindingException;
import com.teoresi.webapp.exception.ErrorResponse;
import com.teoresi.webapp.exception.NotFoundException;
import com.teoresi.webapp.model.User;
import com.teoresi.webapp.repository.IUtenteRepository;
import com.teoresi.webapp.service.RoleService;
import com.teoresi.webapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UtentiController
{
	private static final Logger LOGGER = LoggerFactory.getLogger(UtentiController.class);

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	private IUtenteRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private ResourceBundleMessageSource errMessage;
	
	@GetMapping(value = "/cerca/tutti")
	public List<User> getAllUser()
	{
		LOGGER.info("Otteniamo tutti gli User");

		return userService.listAll();
	}
	
	@GetMapping(value = "/cerca/userid/{userId}")
	public User getUtente(@PathVariable("userId") String UserId) throws NotFoundException {
		LOGGER.info("Otteniamo l'utente " + UserId);
		
		User utente = userService.get(UserId);
		
		if (utente == null)
		{
			String ErrMsg = String.format("L'utente %s non e' stato trovato!", UserId);

			LOGGER.warn(ErrMsg);
			
			throw new NotFoundException(ErrMsg);
		}
		
		return utente;
	}

	@GetMapping(value = "/cerca/mail/{mail}")
	public User getUtenteByMail(@PathVariable("mail") String mail) throws NotFoundException {
		LOGGER.info("Otteniamo l'utente con mail" + mail);

		User user = userRepository.selectUserByMailAndIdUser(mail);

		if (user == null)
		{
			String ErrMsg = String.format("L'utente con MAIL %s non e' stato trovato!", mail );

			LOGGER.warn(ErrMsg);

			throw new NotFoundException(ErrMsg);
		}

		return user;
	}
	
	// ------------------- INSERIMENTO / MODIFICA UTENTE ------------------------------------
	@PostMapping(value = "/inserisci")
	public ResponseEntity<ErrorResponse.InfoMsg> addNewUser(@Valid @RequestBody User user,
															BindingResult bindingResult) throws BindingException {
		//userRepository.save(user);
		if (user.getUserid() != null){
			User checkUtente = userService.get(user.getUserid());
			if (checkUtente != null)
			{
				user.setUserid(checkUtente.getUserid());
				LOGGER.info("Modifica Utente");
			}
			else
			{
				LOGGER.info("Inserimento Nuovo Utente");
			}
			/*
			Role role = new Role();
			User user2 = new User();
			//CRITTIAMO LA PASSWORD
			user2.setPassword("password");
			user2.setName("admin2");
			user2.setSurname("admin2");
			user2.setMail("admin2@test.com");
			user2 = userRepository.save(user2);
			role.setTipo("ADMIN");
			role.setUser(user2);
			roleService.save(role);
			 */
		}

		if (bindingResult.hasErrors())
		{
			String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());

			LOGGER.warn(MsgErr);

			throw new BindingException(MsgErr);
		}
		
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		userRepository.save(user);

		return new ResponseEntity<ErrorResponse.InfoMsg>(new ErrorResponse.InfoMsg(LocalDate.now(),
				String.format("Inserimento Utente %s Eseguita Con Successo", user.getUserid())), HttpStatus.CREATED);
	}

	// ------------------- ELIMINAZIONE UTENTE ------------------------------------
	@DeleteMapping(value = "/elimina/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") String UserId) throws NotFoundException {
		LOGGER.info("Eliminiamo l'utente con id " + UserId);

		User user = userService.get(UserId);

		if (user == null)
		{
			String MsgErr = String.format("Utente %s non presente in anagrafica! ",UserId);

			LOGGER.warn(MsgErr);
			
			throw new NotFoundException(MsgErr);
		}

		userService.delete(user.getUserid());
		
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		headers.setContentType(MediaType.APPLICATION_JSON);

		ObjectNode responseNode = mapper.createObjectNode();

		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", "Eliminazione Utente " + UserId + " Eseguita Con Successo");

		return new ResponseEntity<>(responseNode, headers, HttpStatus.OK);
	}
}

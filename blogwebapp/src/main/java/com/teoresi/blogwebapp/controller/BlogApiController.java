package com.teoresi.blogwebapp.controller;

import com.teoresi.blogwebapp.helloworld.HelloWorldBean;
import com.teoresi.blogwebapp.model.Blog;
import com.teoresi.blogwebapp.model.Role;
import com.teoresi.blogwebapp.model.User;
import com.teoresi.blogwebapp.service.BlogService;
import com.teoresi.blogwebapp.service.RoleService;
import com.teoresi.blogwebapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
//@RestController
public class BlogApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BlogApiController.class);

	@Autowired
	private BlogService blogService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private MessageSource message;

	@RequestMapping("/blogs2")
	public String viewBlog(Model model) {
		model.addAttribute("pageTitle", "Blogs");
		return "list_blog";
	}

	@RequestMapping("/new2")
	public String newBlog(Model model) {
		try {
			Blog blog = new Blog();
			model.addAttribute("blog", blog);
		} catch (Exception e) {
			throw new RuntimeException(
					message.getMessage("blogapp.error.controller.newBlog", null, LocaleContextHolder.getLocale()) + e);
		}
		return "new_blog";
	}

	@RequestMapping(value = "/save2", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveBlog(@Valid @ModelAttribute("blog") Blog blog, BindingResult result) {
		try {
			if (result.hasErrors()) {
				if (blog.getUser().getName().isEmpty())
					result.addError(new FieldError("blog", "user.name", message
							.getMessage("blogapp.msg.validation.user.name", null, LocaleContextHolder.getLocale())));
				if (blog.getUser().getSurname().isEmpty())
					result.addError(new FieldError("blog", "user.surname", message
							.getMessage("blogapp.msg.validation.user.surname", null, LocaleContextHolder.getLocale())));
				if (blog.getUser().getMail().isEmpty())
					result.addError(new FieldError("blog", "user.mail", message
							.getMessage("blogapp.msg.validation.user.mail", null, LocaleContextHolder.getLocale())));

				return blog.getIdblog() == null ? "new_blog" : "edit_blog";
			}
			Role role = new Role();
			User user = new User();
			Date date = new Date();
			if (blog.getIdblog() == null) {
				blog.setPublicationData(date);
			}
			user = blog.getUser();
			if (user != null) {
				String crypPassword = bCryptPasswordEncoder.encode("test");
				user.setPassword(crypPassword);
				role.setUser(user);
				roleService.save(role);
			}
			userService.save(user);
			blogService.save(blog);

		} catch (Exception e) {
			throw new RuntimeException("saveBlog: Verificare salvataggio blog!" + e);
		}
		return "redirect:/blogs2";
	}

	@ResponseBody
	@RequestMapping("/edit2/{idblog}")
	public ModelAndView editBlog(@PathVariable(name = "idblog") String idblog) {
		ModelAndView modelAndView = new ModelAndView("edit_blog");
		try {
			Blog blog = blogService.get(idblog);
			modelAndView.addObject("blog", blog);
		} catch (Exception e) {
			throw new RuntimeException("editBlog: Verificare Modifica - edit del blog!" + e);
		}
		return modelAndView;
	}

	@RequestMapping("/delete2/{idblog}")
	public String deletetBlog(@PathVariable(name = "idblog") String idblog) {
		try {
			blogService.delete(idblog);
		} catch (Exception e) {
			throw new RuntimeException("deletetBlog: Verificare delete blog!" + e);
		}
		return "redirect:/blogs2";
	}

	@RequestMapping("/detail2/{idblog}")
	public String detailBlog(@PathVariable(name = "idblog") String idblog, Model model) {
		model.addAttribute("pageTitle", "View Blog");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Blog blog = blogService.get(idblog);
			model.addAttribute("b_title", blog.getTitle());
			model.addAttribute("b_publicationdate", dateFormat.format(blog.getPublicationData()));
			model.addAttribute("b_nameAuthor", blog.getUser().getName());
			model.addAttribute("b_surnameAuthor", blog.getUser().getSurname());
			model.addAttribute("b_mailAuthor", blog.getUser().getMail());
			model.addAttribute("b_content", blog.getContent());
		} catch (Exception e) {
			throw new RuntimeException("detailBlog: Verificare VIew - blog!" + e);
		}
		return "view_blog";
	}

	@GetMapping(path = "/hello-world/path-variable/{name}")
	public HelloWorldBean helloWorldPathVariable(@PathVariable String name) {
		return new HelloWorldBean(String.format("Hello World, %s", name));
	}

	@PostMapping("/daddo")
	public ResponseEntity<User> createUser(@RequestBody User user) {

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getIduser())
				.toUri();

		return ResponseEntity.created(location).build();
	}

}

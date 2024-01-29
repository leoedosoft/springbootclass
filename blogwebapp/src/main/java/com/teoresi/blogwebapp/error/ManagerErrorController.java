package com.teoresi.blogwebapp.error;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ManagerErrorController implements ErrorController{

	private static final Logger LOGGER = LoggerFactory.getLogger(ManagerErrorController.class);
	
	public String getErrorPath() {
		return "/error";
	}
	
	/**
	 * Errori più comune da mostrare all’utente 403, 404, 500, 503 e 504 
	 */
	@RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        String errorPage = "error";
        String titlePage = "Error";
        String codeError = "";
       
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		
		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());
			switch (statusCode) {
			  case 403:
			  	// handle HTTP 403 Forbidden error
				titlePage = "Forbidden error";
				errorPage = "error/403";
				LOGGER.error("Error 403");
			    break;
			  case 404:
				// handle HTTP 404 Not Found error
				titlePage = "Page Not Found";
				errorPage = "error/404";
				LOGGER.error("Error 404");
			    break;
			  case 500:
				// handle HTTP 500 Internal Server error
				errorPage = "error/500";
				titlePage = "Internal Server Error";
				LOGGER.error("Error 500");
			    break;
			  case 503:
				// handle HTTP 500 Service Unavailable
				errorPage = "error/503";
				titlePage = "Service Unavailable";
				LOGGER.error("Error 503");
			    break;
			  case 504:
				// handle HTTP 504 Gateway Timeout
				errorPage = "error/504";
				titlePage = "Gateway Timeoute";
				LOGGER.error("Error 504");
			    break;
			}
			codeError = statusCode.toString();
		}
		model.addAttribute("titlePage", titlePage);
		model.addAttribute("codeError", codeError);
		return errorPage;
    }
}


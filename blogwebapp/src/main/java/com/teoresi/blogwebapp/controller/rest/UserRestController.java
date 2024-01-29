package com.teoresi.blogwebapp.controller.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teoresi.blogwebapp.dto.DataTableDTO;
import com.teoresi.blogwebapp.service.UserService;

@RestController
public class UserRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);
	final static String msgFixedRest = "ERROR UserRestController";
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/userslist", method = RequestMethod.GET)
	public DataTableDTO getBlogsList(@RequestParam("start") int start,
			@RequestParam("length") int length,
			@RequestParam("draw") int draw,
			@RequestParam("order[0][column]") int sortColIndex,
			@RequestParam("order[0][dir]") String order,
			@RequestParam("columns[0][data]") String username,
			@RequestParam("search[value]") String valueSearch
			){
	
		DataTableDTO tableDTO = new DataTableDTO();
		try {
			int totalUsers = 0;
			totalUsers = userService.totalUsers();
			tableDTO.setData((List)userService.findAllUserWithRoles(start, length));
			tableDTO.setRecordsFiltered(totalUsers);
			tableDTO.setRecordsTotal(totalUsers); // Needed to show Pagination in Datatable
			tableDTO.setDraw(draw);
		} catch (Exception e) {
			throw new RuntimeException(msgFixedRest+" Verificare lettura api List Users"+e);
		}
		
		return tableDTO;
	}

}

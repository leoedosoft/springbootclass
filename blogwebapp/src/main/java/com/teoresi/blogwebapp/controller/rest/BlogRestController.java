package com.teoresi.blogwebapp.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teoresi.blogwebapp.dto.DataTableDTO;
import com.teoresi.blogwebapp.model.Blog;
import com.teoresi.blogwebapp.service.BlogService;

@RestController
public class BlogRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BlogRestController.class);
	final static String msgFixedRest = "ERROR BlogRestController";
	
	@Autowired
	private BlogService blogService;
	

	/*
	    Handle DataTable request parameters in spring boot request
		columns[0][data]:firstName
		columns[0][name]:
		columns[0][searchable]:true
		columns[0][orderable]:true
		columns[0][search][value]:
		columns[0][search][regex]:false
		columns[1][data]:lastName
		columns[1][name]:
		columns[1][searchable]:true
		columns[1][orderable]:true
		columns[1][search][value]:
		columns[1][search][regex]:false
		columns[2][data]:age
		columns[2][name]:
		columns[2][searchable]:true
		columns[2][orderable]:true
		columns[2][search][value]:
		columns[2][search][regex]:false
		order[0][column]:0
		order[0][dir]:asc
		start:0
		length:10
		search[value]:
		search[regex]:false
	 */
	@RequestMapping(value = "/bloglist", method = RequestMethod.GET)
	public DataTableDTO getBlogsList(@RequestParam("start") int start,
			@RequestParam("length") int length,
			@RequestParam("draw") int draw,
			@RequestParam("order[0][column]") int sortColIndex,
			@RequestParam("order[0][dir]") String order,
			@RequestParam("columns[0][data]") String title,
			@RequestParam("columns[0][search][value]") String titleSearchValue,
			@RequestParam("columns[1][search][value]") String conentSearchValue,
			@RequestParam("search[value]") String valueSearch
			){
		DataTableDTO tableDTO = new DataTableDTO();
		try {
			int totalBlogs = 0;
			int numPage = start >= length ?  start/length : 0;
			Page<Blog> blogPage = blogService.findByTitleLikeOrContentLike("%"+valueSearch+"%", "%"+valueSearch+"%", PageRequest.of(numPage, length));
			totalBlogs = (int)blogPage.getTotalElements();
			List<Blog> blogList = new ArrayList<Blog>();
			if(blogPage != null && blogPage.hasContent()) {
				blogList = blogPage.getContent();
			}
			tableDTO.setData((List)blogList);
			tableDTO.setRecordsFiltered(totalBlogs);
			tableDTO.setRecordsTotal(totalBlogs);
			tableDTO.setDraw(draw);
		} catch (Exception e) {
			throw new RuntimeException(msgFixedRest+" Verificare lettura api List Blog"+e);
		}
		return tableDTO;
	}
	
	

}

package com.teoresi.blogdataservice.controller;

import com.teoresi.blogdataservice.exceptions.NotFoundException;
import com.teoresi.blogdataservice.model.Blog;
import com.teoresi.blogdataservice.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("api")
public class BlogController {

    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    private BlogService blogService;

    @GetMapping(value = "/blog/{idblog}", produces = "application/json")
    public ResponseEntity<Blog> blogById(@PathVariable("idblog") String idblog) throws NotFoundException {
        logger.info("****** Otteniamo blog con id " + idblog + " *******");

        Blog blog;
        blog = blogService.get(idblog);

        if (blog == null)
        {
            String ErrMsg = String.format("Il idblog %s non è stato trovato!", idblog);

            logger.warn(ErrMsg);

            throw new NotFoundException(ErrMsg);
            //return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
        }
        else
        {
            return new ResponseEntity<Blog>(blog, HttpStatus.OK);
        }
    }
}

package com.teoresi.blogdataservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.teoresi.blogdataservice.exceptions.ErrorResponse;
import com.teoresi.blogdataservice.exceptions.NotFoundException;
import com.teoresi.blogdataservice.model.Blog;
import com.teoresi.blogdataservice.service.BlogService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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

    @DeleteMapping(value = "/blogs/{idblog}", produces = "application/json")
    public ResponseEntity<?> deleteById(@PathVariable("idblog") String idblog) throws NotFoundException {
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
        blogService.delete(blog.getBlogid());

        HttpHeaders headers = new HttpHeaders();
        ObjectMapper mapper = new ObjectMapper();

        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectNode responseNode = mapper.createObjectNode();

        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Eliminazione Blog " + idblog + " Eseguita Con Successo");

        return new ResponseEntity<>(responseNode, headers, HttpStatus.OK);
    }


    @GetMapping(value = "/blogs", produces = "application/json")
    public ResponseEntity<List<Blog>> listBlogs() throws NotFoundException {
        logger.info("****** Otteniamo list blogs  *******");

        List<Blog> listBlogs = new ArrayList<>();
        listBlogs = blogService.listAll();

        if (listBlogs == null)
        {
            String ErrMsg = String.format("list blogs vuoto!");

            logger.warn(ErrMsg);

            throw new NotFoundException(ErrMsg);
        }
        else
        {
            return new ResponseEntity<List<Blog>>(listBlogs, HttpStatus.OK);
        }
    }


    @PostMapping(value = "/blog")
    public ResponseEntity<ErrorResponse.InfoMsg> addNewBlog(@Valid @RequestBody Blog blog){

        if(blog.getBlogid() != null && blog.getBlogid().equals("-1")){
            blog.setBlogid(null);
            logger.info("Creazione Blog");
        }
        blogService.save(blog);

        return new ResponseEntity<ErrorResponse.InfoMsg>(new ErrorResponse.InfoMsg(LocalDate.now(),
                String.format("Inserimento Blog %s Eseguita Con Successo", blog.getBlogid())), HttpStatus.CREATED);
    }

    @PutMapping(value = "/blog")
    public ResponseEntity<ErrorResponse.InfoMsg> updateBlog(@Valid @RequestBody Blog blog){
        if (blog.getBlogid() != null){
            Blog checkBlog = blogService.get(blog.getBlogid());
            if (checkBlog != null)
            {
                blog.setBlogid(blog.getBlogid());
                logger.info("Modifica Blog");
            }
        }

        blogService.save(blog);

        return new ResponseEntity<ErrorResponse.InfoMsg>(new ErrorResponse.InfoMsg(LocalDate.now(),
                String.format("Modifica Blog %s Eseguita Con Successo", blog.getBlogid())), HttpStatus.CREATED);
    }
}

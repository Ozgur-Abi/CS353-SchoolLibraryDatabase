package com.app.controller;

import com.app.entity.*;
import com.app.service.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor(onConstructor = @__({@Autowired,@NonNull}))
public class BookController {
    private final UserService userService;
    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getBooks(Model model) {
        Query q = entityManager.createNativeQuery("SELECT * FROM book", Book.class);
        List<Book> blist=(List<Book>)q.getResultList( );
        model.addAttribute("books", blist);
        //List<Book> slist=(List<Book>)model.getAttribute("searchedBooks");
        return "books/book-search";
    }

    @RequestMapping(value = "/book-search", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String searchBooks(@RequestParam String bookName, RedirectAttributes redirectAttributes) {
        Query q = entityManager.createNativeQuery("SELECT * FROM book", Book.class);
        List<Book> blist=(List<Book>)q.getResultList( );
        redirectAttributes.addAttribute("searchedBooks", bookName);
        System.out.println(bookName);
        return "redirect:/books/book-search";
    }

    @RequestMapping(value = "/book-search",method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getResults(Model model, @RequestParam(value = "searchedBooks", required = false) String name) {
        Book a = new Book();
        Query q = entityManager.createNativeQuery("SELECT * FROM book", Book.class);
        List<Book> blist=(List<Book>)q.getResultList( );
        model.addAttribute("books", blist);

        System.out.println(name);
        return "books/book-search";
    }
}

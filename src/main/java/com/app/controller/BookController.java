package com.app.controller;

import com.app.entity.*;
import com.app.service.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

    @RequestMapping(value = "/book_request", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getRequestBookForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        return "books/request_book";
    }

    @Transactional
    @RequestMapping(value = "/book_request", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String sendRequestForm(@RequestParam String title, @RequestParam String author, @RequestParam String genre,
                                  @RequestParam int pubYear, @RequestParam String pub, @RequestParam String lang,
                                  @RequestParam String isbn, @RequestParam String issn) {
        System.out.println(title);
        System.out.println(author);
        entityManager.createNativeQuery("INSERT INTO book (author, genre, isbn, issn, language, published_year, publisher, title) VALUES (?,?,?,?,?,?,?,?)")
                .setParameter(1, author)
                .setParameter(2, genre)
                .setParameter(3, isbn)
                .setParameter(4, issn)
                .setParameter(5, lang)
                .setParameter(6, pubYear)
                .setParameter(7, pub)
                .setParameter(8, title)
                .executeUpdate();
        return "redirect:/books/book-search";
    }

    @RequestMapping(value = "/book-search", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String searchBooks(@RequestParam String title, RedirectAttributes redirectAttributes) {
        Query q = entityManager.createNativeQuery("SELECT * FROM book", Book.class);
        List<Book> blist=(List<Book>)q.getResultList( );
        redirectAttributes.addAttribute("title", title);
        System.out.println(title);
        return "redirect:/books/book-search";
    }




    @RequestMapping(value = "/book-search",method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getResults(Model model, @RequestParam(value = "title", required = false) String title,
    @RequestParam(value = "author", required = false) String author, @RequestParam(value = "keyword", required = false) String keyword){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        String sqlQuery = "SELECT * FROM book ";
        if (keyword != null){
            sqlQuery += "WHERE title LIKE '%" + keyword + "%'";
            sqlQuery += "OR WHERE author LIKE '%" + keyword + "%'";
            sqlQuery += "OR WHERE genre LIKE '%" + keyword + "%'";
            sqlQuery += "OR WHERE isbn LIKE '%" + keyword + "%'";
            sqlQuery += "OR WHERE issn LIKE '%" + keyword + "%'";
        }
        else if (author != null){
            sqlQuery += "WHERE author '%" + author + "%'";
        }
        else if (title != null){
            sqlQuery += "WHERE title LIKE '%" + title + "%'";
        }

        Query q = entityManager.createNativeQuery(sqlQuery, Book.class);
        List<Book> blist=(List<Book>)q.getResultList( );
        model.addAttribute("books", blist);

        return "books/book-search";
    }
}

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public String sendRequestForm(Model model, @RequestParam String title, @RequestParam String author, @RequestParam String genre,
                                  @RequestParam int pubYear, @RequestParam String pub, @RequestParam String lang,
                                  @RequestParam String isbn, @RequestParam String issn) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        long rid =  ((MyUserDetails)authentication.getPrincipal()).getId();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        entityManager.createNativeQuery("INSERT INTO book_request (requester_id, author, genre, isbn, issn, language, published_year, publisher, request_date, title) VALUES (?,?,?,?,?,?,?,?,?,?)")
                .setParameter(1, rid)
                .setParameter(2, author)
                .setParameter(3, genre)
                .setParameter(4, isbn)
                .setParameter(5, issn)
                .setParameter(6, lang)
                .setParameter(7, pubYear)
                .setParameter(8, pub)
                .setParameter(9,  Long.parseLong(dtf.format(now)))
                .setParameter(10, title)
                .executeUpdate();

        /*entityManager.createNativeQuery("INSERT INTO book (author, genre, isbn, issn, language, published_year, publisher, title) VALUES (?,?,?,?,?,?,?,?)")
                .setParameter(1, author)
                .setParameter(2, genre)
                .setParameter(3, isbn)
                .setParameter(4, issn)
                .setParameter(5, lang)
                .setParameter(6, pubYear)
                .setParameter(7, pub)
                .setParameter(8, title)
                .executeUpdate();*/
        return "redirect:/books/book-search";
    }

    @RequestMapping(value = "/book-search", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String searchBooks(@RequestParam String title, @RequestParam int userSelection, RedirectAttributes redirectAttributes) {
        Query q = entityManager.createNativeQuery("SELECT * FROM book", Book.class);
        List<Book> blist=(List<Book>)q.getResultList( );
        redirectAttributes.addAttribute("title", title);
        redirectAttributes.addAttribute("userSelection", userSelection);
        System.out.println(title);
        return "redirect:/books/book-search";
    }




    @RequestMapping(value = "/book-search",method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getResults(Model model, @RequestParam(value = "title", required = false) String title,
                             @RequestParam(value = "userSelection", required = false) String keyword)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        String sqlQuery = "SELECT * FROM book ";
        if (title != null && Objects.equals(keyword, "0")){
            sqlQuery += "WHERE title LIKE '%" + title + "%'";

            //sqlQuery += " OR author LIKE '%" + title + "%'";
            //sqlQuery += " OR genre LIKE '%" + title + "%'";
            //sqlQuery += "OR WHERE isbn LIKE '%" + keyword + "%'";
            //sqlQuery += "OR WHERE issn LIKE '%" + keyword + "%'";
        }
        else if (title != null && Objects.equals(keyword, "1")){
            sqlQuery += "WHERE author LIKE'%" + title + "%'";
            sqlQuery += " OR author LIKE '%" + title + "%'";
            sqlQuery += " OR genre LIKE '%" + title + "%'";
            sqlQuery += "OR isbn LIKE '%" + title + "%'";
            sqlQuery += "OR issn LIKE '%" + title + "%'";
        }
        else if (title != null && Objects.equals(keyword, "2")){
            sqlQuery += "WHERE author LIKE '%" + title + "%'";
        }

        Query q = entityManager.createNativeQuery(sqlQuery, Book.class);
        List<Book> blist=(List<Book>)q.getResultList( );
        model.addAttribute("books", blist);

        return "books/book-search";
    }

    @RequestMapping(value = "/book_requests", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getBookRequestView(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        Query q = entityManager.createNativeQuery("SELECT * FROM book_request", BookRequest.class);
        List<BookRequest> blist=(List<BookRequest>)q.getResultList( );
        model.addAttribute("requests", blist);
        return "books/book_requests";
    }

    @RequestMapping(value = "/reservation_requests", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getReservationRequestView(Model model) {
        Query q = entityManager.createNativeQuery("SELECT * FROM reservation_record", ReservationRecord.class);
        List<ReservationRecord> blist=(List<ReservationRecord>)q.getResultList( );
        model.addAttribute("requests", blist);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        return "books/reservation_requests";
    }
    @RequestMapping(value = "/borrow_requests", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getBorrowRequestView(Model model) {
        Query q = entityManager.createNativeQuery("SELECT * FROM borrow_record", BorrowRecord.class);
        List<BorrowRecord> blist=(List<BorrowRecord>)q.getResultList( );
        model.addAttribute("requests", blist);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        return "books/borrow_requests";
    }

    @RequestMapping(value = "/book_add", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getBookAddView(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        return "books/add_book";
    }

    @Transactional
    @RequestMapping(value = "/book_add", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String sendBookAddForm(Model model, @RequestParam String title, @RequestParam String author, @RequestParam String genre,
                                  @RequestParam int pubYear, @RequestParam String pub, @RequestParam String lang,
                                  @RequestParam String isbn, @RequestParam String issn) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        long rid =  ((MyUserDetails)authentication.getPrincipal()).getId();

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

}

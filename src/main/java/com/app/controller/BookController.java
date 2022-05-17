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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Query q = entityManager.createNativeQuery("SELECT * FROM book", Book.class);
        List<Book> blist=(List<Book>)q.getResultList( );
        model.addAttribute("books", blist);
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
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
        else if (title != null && Objects.equals(keyword, "3")){
            String[] dateStrings = title.split("-");
            int startDate = Integer.valueOf(dateStrings[0]);
            int endDate = Integer.valueOf(dateStrings[0]);
            sqlQuery += "WHERE published_year >= " + startDate + " AND published_year<= " + endDate;
        }

        Query q = entityManager.createNativeQuery(sqlQuery, Book.class);
        List<Book> blist=(List<Book>)q.getResultList( );
        model.addAttribute("books", blist);

        return "books/book-search";
    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String showBookPage(@PathVariable("id") long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        long rid =  ((MyUserDetails)authentication.getPrincipal()).getId();
        Query q = entityManager.createNativeQuery("SELECT * FROM book WHERE book_id = " + id, Book.class);
        List<Book> blist=(List<Book>)q.getResultList( );
        Book b = blist.get(0);
        model.addAttribute("book", b);
        String aQuery = "SELECT * FROM borrow_record WHERE book_id = " + id + " AND return_date IS NULL AND approver_id IS NOT NULL";
        Query q2 = entityManager.createNativeQuery(aQuery, BorrowRecord.class);
        List<BorrowRecord> rlist=(List<BorrowRecord>)q2.getResultList( );

        String rQuery = "SELECT * FROM book_rating WHERE book_id = " + id;
        Query q3 = entityManager.createNativeQuery(rQuery, BookRating.class);
        List<BookRating> rateList = (List<BookRating>)q3.getResultList( );

        String resQuery = "SELECT * FROM reservation_record WHERE book_id = " + id + " AND approver_id IS NOT NULL";
        Query q4 = entityManager.createNativeQuery(resQuery, ReservationRecord.class);
        List<ReservationRecord> reslist=(List<ReservationRecord>)q4.getResultList( );

        String rCheckQuery = "SELECT * FROM book_rating WHERE book_id = " + id + " AND rater_id = " + rid;
        Query q5 = entityManager.createNativeQuery(rCheckQuery, BookRating.class);
        List<BookRating> rateCheckList = (List<BookRating>)q5.getResultList( );

        String ratingMax = "SELECT MAX(score) FROM book_rating";
        Query q6 = entityManager.createNativeQuery(ratingMax);

        String ratingMin = "SELECT MIN(score) FROM book_rating";
        Query q7 = entityManager.createNativeQuery(ratingMin);

        model.addAttribute("maxScore", q6.getResultList().get(0));
        model.addAttribute("minScore", q7.getResultList().get(0));

        if(rateCheckList.isEmpty())
            model.addAttribute("rateable", "Rateable");
        else
            model.addAttribute("rateable", "Already Rated");

        model.addAttribute("ratings", rateList);

        if (rlist.isEmpty())
            model.addAttribute("availability", "Borrowable");
        else{
            if (rlist.get(0).getBorrower().getId() == rid){ //this user has borrowed the book
                model.addAttribute("availability", "Borrowed");
            }
            else if (reslist.isEmpty())
                model.addAttribute("availability", "Reserveable");
            else if (reslist.get(0).getRequester().getId() == rid){ //this user has borrowed the book
                model.addAttribute("availability", "Reserved");
            }
            else{
                model.addAttribute("availability", "Unavailable");
            }
        }
        return "books/book";
    }

    @Transactional
    @RequestMapping(value = "book/{id}/book_rate", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String sendBookRating(Model model, @PathVariable("id") long id, @RequestParam int score, @RequestParam String comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        long rid =  ((MyUserDetails)authentication.getPrincipal()).getId();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();

        entityManager.createNativeQuery("INSERT INTO book_rating (comment, rater_id, rating_date, score, book_id) VALUES (?,?,?,?,?)")
                .setParameter(1, comment)
                .setParameter(2, rid)
                .setParameter(3, Long.parseLong(dtf.format(now)))
                .setParameter(4, score)
                .setParameter(5, id)
                .executeUpdate();

        return "redirect:/books/book-search";
    }

    @Transactional
    @RequestMapping(value = "book/{id}/book_borrow", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String sendBorrowRequest(Model model, @PathVariable("id") long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        long rid =  ((MyUserDetails)authentication.getPrincipal()).getId();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        entityManager.createNativeQuery("INSERT INTO borrow_record (borrow_date, book_id, requester_id) VALUES (?,?,?)")
                .setParameter(1, Long.parseLong(dtf.format(now)))
                .setParameter(2, id)
                .setParameter(3, rid)
                .executeUpdate();
        Book b = (Book) model.getAttribute("book");
        System.out.println(id);

        return "redirect:/books/book-search";
    }

    @Transactional
    @RequestMapping(value = "book/{id}/book_reserve", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String sendReservationRequest(Model model, @PathVariable("id") long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        long rid =  ((MyUserDetails)authentication.getPrincipal()).getId();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        entityManager.createNativeQuery("INSERT INTO reservation_record (request_date, book_id, requester_id) VALUES (?,?,?)")
                .setParameter(1, Long.parseLong(dtf.format(now)))
                .setParameter(2, id)
                .setParameter(3, rid)
                .executeUpdate();

        return "redirect:/books/book-search";
    }

    @Transactional
    @RequestMapping(value = "book/{id}/book_return", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String returnBook(Model model, @PathVariable("id") long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        long rid =  ((MyUserDetails)authentication.getPrincipal()).getId();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        long dateLong = Long.parseLong(dtf.format(now));
        String query = "UPDATE borrow_record SET return_date = " + dateLong + " WHERE book_id = " + id + " AND requester_id = " + rid +
                " AND return_date IS NULL ";
        entityManager.createNativeQuery(query)
                .executeUpdate();

        return "redirect:/books/book-search";
    }

    @Transactional
    @RequestMapping(value = "book/{id}/book_unreserve", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String unreserveBook(Model model, @PathVariable("id") long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        long rid =  ((MyUserDetails)authentication.getPrincipal()).getId();
        String query = "DELETE FROM reservation_record WHERE book_id = " + id + " AND requester_id = " + rid;
        entityManager.createNativeQuery(query)
                .executeUpdate();

        return "redirect:/books/book-search";
    }

    @Transactional
    @RequestMapping(value = "borrow_requests/{id}/approve", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String approveBorrowRequest(Model model, @PathVariable("id") long id, @RequestParam String date) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        long rid =  ((MyUserDetails)authentication.getPrincipal()).getId();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        long dateLong = Long.parseLong(date.replace("-", ""));
        String query = "UPDATE borrow_record SET due_date = " + dateLong + ", approver_id = " + rid + " WHERE borrow_id = " + id;
        entityManager.createNativeQuery(query)
                .executeUpdate();

        return "redirect:/books/borrow_requests";
    }

    @Transactional
    @RequestMapping(value = "borrow_requests/{id}/reject", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String denyBorrowRequest(Model model, @PathVariable("id") long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        long rid =  ((MyUserDetails)authentication.getPrincipal()).getId();
        String query = "DELETE from borrow_record WHERE borrow_id = " + id;
        entityManager.createNativeQuery(query)
                .executeUpdate();

        return "redirect:/books/borrow_requests";
    }

    @Transactional
    @RequestMapping(value = "reservation_requests/{id}/approve", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String approveReservationRequest(Model model, @PathVariable("id") long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        long rid =  ((MyUserDetails)authentication.getPrincipal()).getId();
        String query = "UPDATE reservation_record SET approver_id = " + rid + " WHERE reservation_id = " + id;
        entityManager.createNativeQuery(query)
                .executeUpdate();

        return "redirect:/books/reservation_requests";
    }

    @Transactional
    @RequestMapping(value = "reservation_requests/{id}/reject", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String cancelReservationRequest(Model model, @PathVariable("id") long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        long rid =  ((MyUserDetails)authentication.getPrincipal()).getId();
        String query = "DELETE from reservation_record WHERE reservation_id = " + id;
        entityManager.createNativeQuery(query)
                .executeUpdate();

        return "redirect:/books/reservation_requests";
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

    @RequestMapping(value = "/view_notifications", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getNotifications(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        long rid =  ((MyUserDetails)authentication.getPrincipal()).getId();
        Query q = entityManager.createNativeQuery("SELECT * FROM notification WHERE receiver_id = " + rid, Notification.class);
        List<Notification> nlist=(List<Notification>)q.getResultList( );
        model.addAttribute("notifications", nlist);
        return "books/view_notifications";
    }

    @RequestMapping(value = "/send_notification", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getSendNotification(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        long rid =  ((MyUserDetails)authentication.getPrincipal()).getId();
        return "books/send_notification";
    }

    @Transactional
    @RequestMapping(value = "/send_notification", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public String sendNotification(Model model, @RequestParam String receiverId, @RequestParam String message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        long rid =  ((MyUserDetails)authentication.getPrincipal()).getId();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();


        long recId = userService.findUserByBilkentId(receiverId).getId();

        entityManager.createNativeQuery("INSERT INTO notification (notification_date, text, receiver_id, sender_id) VALUES (?,?,?,?)")
                .setParameter(1, Long.parseLong(dtf.format(now)))
                .setParameter(2, message)
                .setParameter(3, recId)
                .setParameter(4, rid)
                .executeUpdate();
        return "redirect:/books/book-search";
    }

    @RequestMapping(value = "/reservation_requests", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getReservationRequestView(Model model) {
        Query q = entityManager.createNativeQuery("SELECT * FROM reservation_record WHERE approver_id IS NULL", ReservationRecord.class);
        List<ReservationRecord> blist=(List<ReservationRecord>)q.getResultList( );
        model.addAttribute("requests", blist);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        return "books/reservation_requests";
    }
    @RequestMapping(value = "/borrow_requests", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getBorrowRequestView(Model model) {
        Query q = entityManager.createNativeQuery("SELECT * FROM borrow_record WHERE approver_id IS NULL", BorrowRecord.class);
        List<BorrowRecord> blist=(List<BorrowRecord>)q.getResultList( );
        model.addAttribute("requests", blist);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        return "books/borrow_requests";
    }

    @RequestMapping(value = "/add_book", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getBookAddView(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("role", ((MyUserDetails)authentication.getPrincipal()).getRole().ordinal());
        return "books/add_book";
    }

    @Transactional
    @RequestMapping(value = "/add_book", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
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

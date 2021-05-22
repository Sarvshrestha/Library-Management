package handler;

import exceptions.*;
import models.Book;
import models.Booking;
import models.User;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class BookIssueHandler {
    private static final int MAX_BOOK_ISSUE_LIMIT = 4;
    private static final int MAX_DAYS = 14;
    private static final int PRICE_PER_DAY = 1;
    private UsersHandler usersHandler;
    private BooksHandler booksHandler;
    private HashMap<LocalDate, List<Booking>> listHashMap;

    public BookIssueHandler(UsersHandler usersHandler, BooksHandler booksHandler) {
        this.usersHandler = usersHandler;
        this.booksHandler = booksHandler;
        this.listHashMap =  new HashMap<>();
    }

    public void issueBook(List<Book> bookList, User user){
        if (bookList.size() > MAX_BOOK_ISSUE_LIMIT){
            // Put Exception Name Properly
            throw new BookingLimitReached();
        }
        checkBooksAvailability(bookList);
        checkValidUserForBooking(user);
        checkForDuplicateIssuance(user, bookList);

        processBookIssue(bookList, user);
    }

    private void checkBooksAvailability(List<Book> bookList){
        boolean status;
        for (Book book: bookList){
            status = booksHandler.checkBookAvailable(book.getPublication(), book.getId());
            if (!status){
                throw new BookNotAvailable();
            }
        }
    }

    private void checkValidUserForBooking(User user){
        boolean valid = true;
        valid = usersHandler.eligibleForBookIssue(user.getId(), MAX_BOOK_ISSUE_LIMIT);
        if (!valid){
            // TODO Check for Pay Now Option
            throw new InvalidUser();
        }
    }

    private void checkForDuplicateIssuance(User user, List<Book> bookList){
        for (Book book: bookList){
            // IMPROVEMENT CODE STYLE
            boolean duplicate = usersHandler.isUserIssuedThisBook(user.getId(), book.getId());
            if (duplicate){
                throw new DuplicateBookIssue();
            }
        }
    }

    private void processBookIssue(List<Book> bookList, User user){
        try {
            List<Booking> todayBookings = listHashMap.get(LocalDate.now());
            if (todayBookings == null) {
                todayBookings = new ArrayList<>();
            }
            Booking issuedBook = new Booking(bookList, user);
            todayBookings.add(issuedBook);
            user.setBooks(bookList);
            // Think Once More for Modelling...
            // Keep Type Enum
            // Check for Books Limit and during Availability
            booksHandler.updateBook(bookList, "ISSUE");

        } catch (Exception e) {
            System.out.println("Booking Not Possible for Now");
        }
    }

    public void returnBook(LocalDate localDate, UUID booking_id){
        Booking booking = getBookingHistory(localDate, booking_id);
        int fine = calculateFine(booking);
        User user = booking.getUser();
        if (fine > 0){
            // PAY Fine Needs to Improve from Main.
            boolean finePaid = payFine(fine);
            if (finePaid){
                // TODO ANY ACTION
            } else{
                user.setFine(fine);
            }
        }
        booksHandler.updateBook(booking.getBook(), "RETURN");
        user.setBooks(null);
    }

    private Booking getBookingHistory(LocalDate localDate, UUID booking_id){
        if (localDate == null){
            for (LocalDate date: listHashMap.keySet()){
                List<Booking> bookings = listHashMap.get(date);
                for (Booking booking: bookings){
                    if (booking_id == booking.getId()){
                        return booking;
                    }
                }
            }
        } else{
            List<Booking> bookings = listHashMap.get(localDate);
            for (Booking booking: bookings){
                if (booking_id == booking.getId()){
                    return booking;
                }
            }
        }
        throw new BookingNotFound();
    }

    private int calculateFine(Booking booking){
        LocalDate issueDate = booking.getIssueDate();
        LocalDate todayDate = LocalDate.now();
        Period period = Period.between(issueDate, todayDate);
        int noOfDays = period.getDays();
        if (noOfDays > MAX_DAYS){
            return (noOfDays-MAX_DAYS)*PRICE_PER_DAY;
        }
        return 0;
    }

    private boolean payFine(int fine) {
        return true;
    }
}

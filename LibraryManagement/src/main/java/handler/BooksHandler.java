package handler;

import models.Book;

import java.util.*;

public class BooksHandler {
    // Keep Shelf and Booking Id Map Seperate
    private HashMap<String, List<Book>> bookHashMap = new HashMap<>();

    public void addBook(Book book){
        List<Book> bookList = bookHashMap.get(book.getPublication());
        if (bookList == null){
            bookList = new ArrayList<>();
        }
        bookList.add(book);
    }

    public Book getBookByBookId(UUID book_id){
        for (String s: bookHashMap.keySet()){
            List<Book> bookList = bookHashMap.get(s);
            for (Book book1: bookList){
                if (book_id == book1.getId()){
                    return book1;
                }
            }
        }
        return null;
    }

    public void removeBook(UUID book_id){
        outer: for (String s: bookHashMap.keySet()){
            List<Book> bookList = bookHashMap.get(s);
            for (Book book1: bookList){
                if (book_id == book1.getId()){
                    bookList.remove(book1);
                    break outer;
                }
            }
        }
    }

    public void updateBook(List<Book> bookList, String type){
        if (type.equals("ISSUE")) {
            for (Book book : bookList) {
                Book b1 = getBookByBookId(book.getId());
                b1.bookIssued();
            }
        } else{
            for (Book book : bookList) {
                Book b1 = getBookByBookId(book.getId());
                b1.bookReturned();
            }
        }
    }

    public List<Book> searchBookByPublication(String publication){
        return bookHashMap.get(publication);
    }

    public boolean checkBookAvailable(String publication, UUID book_id){
        if (publication != null) {
            List<Book> bookList = bookHashMap.get(publication);
            for (Book book : bookList) {
                if (book_id == book.getId()) {
                    if (book.getCount() > 0) {
                        return true;
                    }
                }
            }
        } else{
            Book book = getBookByBookId(book_id);
            return book.getCount() > 0;
        }
        return false;
    }
}

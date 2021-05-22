package models;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class Booking {
    private UUID id;
    private List<Book> book;
    private User user;
    private LocalDate issueDate;
    private LocalDate returnedDate;
    private boolean returned;

    public Booking(List<Book> book, User user) {
        this.id = UUID.randomUUID();
        this.book = book;
        this.user = user;
        this.issueDate = LocalDate.now();
    }
}

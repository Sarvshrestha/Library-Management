package models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class User {
    private UUID id;
    private String name;
    private String email;
    private String mobile;
    private int fine;
    private List<Book> books;

    public User(String name, String email, String mobile) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.books = new ArrayList<>();
    }
}

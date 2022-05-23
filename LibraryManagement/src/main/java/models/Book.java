package models;

import lombok.Data;

import java.util.UUID;
import java.util.UUID;

@Data
public class Book {
    private UUID id;
    private String name;
    private String publication;
    private int count;

    public Book(String name, String publication) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.publication = publication;
    }

    public void bookIssued(){
        // Check Book Availability Here...
        this.count--;
    }

    public void bookReturned(){
        this.count++;
    }
}

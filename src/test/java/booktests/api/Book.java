package booktests.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {

    public String name;
    public String author;
    public Integer publishYear;
    public String comment;

    public Book(String name, String author, Integer publishYear, String comment) {
        this.name = name;
        this.author = author;
        this.publishYear = publishYear;
        this.comment = comment;
    }

    public Book(String name, String author, Integer publishYear) {
        this(name, author, publishYear, null);
    }

    public Book(String name, String author) {
        this(name, author, null, null);
    }
}
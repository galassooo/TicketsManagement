package ch.supsi.business;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

final public class TicketBusiness {

    /* fields */
    @JsonInclude
    private  int id;
    @JsonInclude
    private String title;
    @JsonInclude
    private String description;
    @JsonInclude
    private String author;

    @JsonIgnore
    private static AtomicInteger idGenerator = new AtomicInteger(0);

    /* constructors */
    private TicketBusiness() {

    }
    @JsonCreator
    public TicketBusiness(
            @JsonProperty(value = "title") String title,
            @JsonProperty(value = "description") String description,
            @JsonProperty(value = "author") String author) {

        id  = idGenerator.incrementAndGet();
        this.title = title == null || title.isBlank() ? "Not available" : title;
        this.description = description == null || description.isBlank() ? "Not available" : description;
        this.author = author == null || author.isBlank() ? "Not available" : author;
    }

    /* getters & setters */
    public void setTitle(String title) {
        this.title = title == null || title.isBlank() ? this.title : title;
    }

    public void setDescription(String description) {
        this.description = description == null || description.isBlank() ? this.description : description;
    }

    public void setAuthor(String author) {
        this.author = author == null || author.isBlank() ? this.author : author;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketBusiness that)) return false;
        return Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getAuthor(), that.getAuthor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDescription(), getAuthor());
    }

    @JsonIgnore
    @Override
    public String toString() {
        return "\ntitle: " + title + "\ndescription: " + description + "\nauthor: " + author;
    }
}
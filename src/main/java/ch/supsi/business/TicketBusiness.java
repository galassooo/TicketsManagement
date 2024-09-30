package ch.supsi.business;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

final public class TicketBusiness {

    /* fields */
    @JsonInclude
    private String title;
    @JsonInclude
    private String description;
    @JsonInclude
    private String author;

    /* constructors */
    public TicketBusiness() {

    }
    @JsonCreator
    public TicketBusiness(
            @JsonProperty(value = "title") String title,
            @JsonProperty(value = "description") String description,
            @JsonProperty(value = "author") String author) {

        this.title = title == null || title.isBlank() ? "Not available" : title;
        this.description = description == null || description.isBlank() ? "Not available" : description;
        this.author = author == null || author.isBlank() ? "Not available" : author;
    }

    public String getTitle() {
        return title;
    }

    /* getters & setters */
    public void setTitle(String title) {
        this.title = title == null || title.isBlank() ? "Not available" : title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null || description.isBlank() ? "Not available" : description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null || author.isBlank() ? "Not available" : author;
    }

    @JsonIgnore
    @Override
    public String toString() {
        return "\ntitle: " + title + "\ndescription: " + description + "\nauthor: " + author;
    }
}

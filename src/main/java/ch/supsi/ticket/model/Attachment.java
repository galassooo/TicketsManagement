package ch.supsi.ticket.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private byte[] data;

    @Column
    private long size;

    @Column
    private String sizeUnit;

    public Attachment(String name, byte[] data, long size) {
        this.name = name;
        this.data = data;
        this.size = size;
    }
}

package Xeva.productiveApp.tags;

import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.task.Task;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Tag {

    @Id
    @SequenceGenerator(
            name = "tag_sequence",
            sequenceName = "tag_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tag_sequence"
    )
    private Long id;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Column(
            name = "taskId"
    )
    private Long taskId;

    @Column(
            name = "owner_email",
            nullable = false
    )
    private String ownerEmail;

    private LocalDateTime lastUpdated;

    private String uuid;

    @PrePersist
    public void onInsert() {
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    public Tag(String ownerEmail, String name, String uuid){
        this.ownerEmail = ownerEmail;
        this.name = name;
    }

    public Tag(String ownerEmail, String name, Long taskId, String uuid){
        this.ownerEmail = ownerEmail;
        this.name = name;
        this.taskId = taskId;
    }

    public Tag(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public Tag(Long id, String name, Long taskId){
        this.id = id;
        this.name = name;
        this.taskId = taskId;
    }

    public Tag(String name, Long taskId){
        this.name = name;
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return getName().equals(tag.getName()) && getOwnerEmail().equals(tag.getOwnerEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getOwnerEmail());
    }

}

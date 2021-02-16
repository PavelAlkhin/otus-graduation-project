package ru.otus.project.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "posts")
public class PostRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String title;

    @Column(name="text", length = 20000)
    private String text;

    private Boolean visibleAll;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments;

    public PostRecord(User user, Date date, String title, String text, Boolean visibleAll) {
        this.user = user;
        this.date = date;
        this.title = title;
        this.text = text;
        this.visibleAll = visibleAll;
    }

    public PostRecord() {
    }

}

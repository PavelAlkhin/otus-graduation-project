package ru.otus.project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String text;

    @ManyToOne
    private User user;

    @OneToOne
    private PostRecord post;

    public Comment(String text, User user, PostRecord post) {
        this.text = text;
        this.user = user;
        this.post = post;
    }
}

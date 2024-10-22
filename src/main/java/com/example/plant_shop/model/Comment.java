package com.example.plant_shop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    @Column(name = "date")
    private Date date;
    @Column(name = "text")
    private String text;

    @Column(name = "parent_id")
    private Long parentId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "news_id")
    private News news;

    public Comment(Date date, String text, Long parentId, User author, News news) {
        this.date = date;
        this.text = text;
        this.parentId = parentId;
        this.author = author;
        this.news = news;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) &&
                Objects.equals(date, comment.date) &&
                Objects.equals(text, comment.text) &&
                Objects.equals(parentId, comment.parentId) &&
                Objects.equals(author, comment.author) &&
                Objects.equals(news, comment.news);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, text, parentId, author, news);
    }
}

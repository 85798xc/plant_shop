package com.example.plant_shop.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "news")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "news_id")
    private Long id;
    @Column(name = "news_name")
    private String newsName;
    @Column(name = "publication_date")
    private LocalDateTime publicationDate = LocalDateTime.now();
    @Column(name = "description")
    private String description;
    @Column(name = "news_text")
    private String text;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    public News(String newsName, String description, String text) {
        this.newsName = newsName;
        this.description = description;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(id, news.id) &&
                Objects.equals(newsName, news.newsName) &&
                Objects.equals(publicationDate, news.publicationDate) &&
                Objects.equals(description, news.description) &&
                Objects.equals(text, news.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, newsName, publicationDate, description, text);
    }
}
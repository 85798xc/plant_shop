package com.example.plant_shop.service;

import com.example.plant_shop.model.News;
import com.example.plant_shop.repository.NewsRepo;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NewsService {

    private final NewsRepo newsRepo;

    public Page<News> getAllNews(Pageable pageable) {
        return newsRepo.findAll(pageable);
    }

    public Page<News> searchByKey(String text, LocalDateTime fromDate, LocalDateTime toDate,
                                  Integer fromNumberComments, Integer toNumberComments, Pageable pageable) {
        return newsRepo.searchByKey(text, fromDate, toDate, fromNumberComments,
                toNumberComments, pageable);
    }

    public News findNewsById(Long id) {
        return newsRepo.findNewsById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Новость с id =" + id + " не найдена!"));
    }

    public News createNews(News news) {
        if (newsRepo.existsByNewsNameAndPublicationDate(news.getNewsName(), news.getPublicationDate()))
            throw new EntityExistsException("Новость " + news.getNewsName() + " уже существует!");

        return saveNews(new News(
                news.getNewsName(),
                news.getDescription(),
                news.getText()));
    }

    public News updateNews(Long id, News editedNews) {
        News news = findNewsById(id);
        news.setNewsName(editedNews.getNewsName());
        news.setDescription(editedNews.getDescription());
        news.setText(editedNews.getText());

        return saveNews(news);
    }

    public News saveNews(News news) {
        try {
            return newsRepo.save(news);

        } catch (RuntimeException e) {
            log.error("Новость {} не сохранена! Error: [{}].", news.getNewsName(), e.getLocalizedMessage());
            throw new PersistenceException(String.format("Новость %s не сохранена! Error: [%s].",
                    news.getNewsName(), e));
        }
    }

    public void deleteNews(Long newsId) {
        if (!newsRepo.existsById(newsId))
            throw new EntityNotFoundException("Новость с id = " + newsId + " не найдена!");
        newsRepo.deleteById(newsId);
    }
}
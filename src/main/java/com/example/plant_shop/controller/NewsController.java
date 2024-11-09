package com.example.plant_shop.controller;

import com.example.plant_shop.model.Comment;
import com.example.plant_shop.model.News;
import com.example.plant_shop.service.CommentService;
import com.example.plant_shop.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
//"Контроллер для работы с новостями"
public class NewsController {

    private final NewsService newsService;
    private final CommentService commentService;

    //"Получение списка новостей"
    @GetMapping("/all")
    public ResponseEntity<List<News>> getAllNews(
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false, defaultValue = "DESC") String order,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(order), sort));
        return ResponseEntity.ok().body(newsService.getAllNews(pageable).getContent());
    }

    //"Поиск новостей по ключевому слову"
    @GetMapping("/search")
    public ResponseEntity<List<News>> searchByKey(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "fromDate", required = false, defaultValue = "1970-01-01 00:00:00") String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "fromNumberComments", required = false, defaultValue = "0") Integer fromNumberComments,
            @RequestParam(value = "toNumberComments", required = false) Integer toNumberComments,
            @RequestParam(value = "sort", required = false, defaultValue = "publicationDate") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "DESC") String order,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(order), sort));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (toDate == null)
            toDate = LocalDateTime.now().format(formatter);
        return ResponseEntity.ok().body(
                newsService.searchByKey(text, LocalDateTime.parse(fromDate, formatter),
                        LocalDateTime.parse(toDate, formatter), fromNumberComments,
                        toNumberComments, pageable).getContent());
    }

    //"Получение новости по id"
    @GetMapping("/{id}")
    public ResponseEntity<News> getNews(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(newsService.findNewsById(id));
    }

    //"Создание новости"
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createNews(@RequestBody News news) {
        return ResponseEntity.ok().body(newsService.createNews(news));
    }

    //"Редактирование новости"
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> editNews(@PathVariable("id") Long id,
                                      @RequestBody News editedNews) {
        return ResponseEntity.ok().body(newsService.updateNews(id, editedNews));
    }

    //"Удаление новости по её id"
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable("id") Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok().body("Новость с id=" + id + " удалена");
    }

//    страница нумеруется с 0

    //"Получение списка комментариев к новости по её id"
    @GetMapping("/comments/{newsId}")
    public ResponseEntity<List<Comment>> getComments(
            @PathVariable Long newsId,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Comment> comments = commentService.getComments(newsId, pageable);
        return ResponseEntity.ok().body(comments.getContent());
    }
}

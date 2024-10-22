package com.example.plant_shop.service;

import com.example.plant_shop.model.Comment;
import com.example.plant_shop.model.User;
import com.example.plant_shop.repository.CommentRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepo commentRepo;
    private final UserService userService;
    private final NewsService newsService;

    public Page<Comment> getComments(Long newsId, Pageable pageable) {
        return commentRepo.findAllByNews_Id(newsId, pageable);
    }

    public Comment findCommentById(Long id) {
        return commentRepo.findCommentById(id)
                .orElseThrow(() -> new EntityNotFoundException("Коменнтарий с id = " + id + " не найден!"));
    }

    public Comment createComment(String authorName, Long newsId, CommentDTO commentDTO) {
        User author = (User) userService.loadUserByUsername(authorName);
        Comment newComment = new Comment(
                new Date(),
                commentDTO.getText(),
                commentDTO.getParentId(),
                author,
                newsService.findNewsById(newsId));
        return save(newComment);
    }

    public Comment updateComment(String username, Long commentId, CommentDTO commentDTO) {
        if (matchersUser(username, commentId))
            throw new CommentException(String.format("Пользователь %s не может отредактировать комментарий с id = %d!",
                    username, commentId));

        Comment comment = findCommentById(commentId);
        comment.setText(commentDTO.getText());
        return save(comment);
    }

    public void deleteById(String username, Long commentId) {
        if (!matchersUser(username, commentId))
            throw new CommentException(String.format("Пользователь %s не может удалить комментарий с id = %d!",
                    username, commentId));

        commentRepo.deleteById(commentId);
    }

    /**
     * Проверка, что комментарий соответствует пользователю с указанным username и что комментарий существует
     *
     * @param username  - имя пользователя, создавшего комментарий
     * @param commentId - id комментария
     */
    public boolean matchersUser(String username, Long commentId) {
        return username.equals(findCommentById(commentId).getAuthor().getUsername()) && commentRepo.existsById(commentId);
    }

    public Comment save(Comment comment) {
        try {
            return commentRepo.save(comment);

        } catch (RuntimeException e) {
            log.error("Комментарий {} не сохранён. Error: [{}]", comment.getAuthor(), e);
            throw new PersistenceException(String.format("Комментарий %s не сохранён. Error: [%s].", comment.getId(), e));
        }
    }
}
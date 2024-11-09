package com.example.plant_shop.controller;

import com.example.plant_shop.dto.CommentDTO;
import com.example.plant_shop.model.Comment;
import com.example.plant_shop.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //"Получение комментарияпо его id"
    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getComment(@PathVariable Long commentId) {
        return ResponseEntity.ok().body(commentService.findCommentById(commentId));
    }

    //"Создание комментария текущим пользователем"
    @PostMapping("/{newsId}")
    public ResponseEntity<?> createComment(@PathVariable Long newsId,
                                           @RequestBody CommentDTO commentDTO,
                                           Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok().body(commentService.createComment(username, newsId, commentDTO));
    }

    //"Редактирование комментария текущего пользователя по его id"
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId,
                                           @RequestBody CommentDTO commentDTO,
                                           Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok().body(commentService.updateComment(username, commentId, commentDTO));
    }

    //"Удаление комментария текущего пользователя по id"
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, Principal principal) {
        String username = principal.getName();
        commentService.deleteById(username, commentId);
        return ResponseEntity.ok().body("Комментарий пользователя " + username + " удалён.");
    }
}

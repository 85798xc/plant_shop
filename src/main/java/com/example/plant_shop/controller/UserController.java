package com.example.plant_shop.controller;

import com.example.plant_shop.dto.UserUpdateRequest;
import com.example.plant_shop.model.User;
import com.example.plant_shop.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
//"Контроллер для работы с пользователями")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    //"Получение списка всех пользователей"
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(value = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "DESC") String order,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(order), sort));
        return ResponseEntity.ok().body(userServiceImpl.getUsers(pageable).getContent());
    }

    //"Получение пользователя по его id"
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(userServiceImpl.findUserById(id));
    }

    //"Поиск пользователя по email"
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUserByText(
            @RequestParam("text") String email,
            @RequestParam(value = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "DESC") String order,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(order), sort));
        return ResponseEntity.ok().body(userServiceImpl.searchUserByText(email, pageable).getContent());
    }

    //"Обновление текущего пользователя"
    @PutMapping("/update")
    @Transactional
    public ResponseEntity<?> updateCurrentUser(@RequestBody UserUpdateRequest request, Principal principal) {
        String username = principal.getName();
        User user = userServiceImpl.updateUser(username, request);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body(user);
    }

    //"Удаление текущего пользователя"
    @DeleteMapping("/delete")
    @Transactional
    public ResponseEntity<?> deleteUser(Principal principal) {
        String username = principal.getName();
        userServiceImpl.deleteUser(username);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body("Пользльзователь " + principal.getName() + " удалён.");
    }

    //"Обновление пользователя по username"
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@PathVariable("username") String username,
                                        @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok().body(userServiceImpl.updateUser(username, request));
    }

    //"Удаление пользователя по username"
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {
        userServiceImpl.deleteUser(username);
        return ResponseEntity.ok().body("Пользователь " + username + " удалён.");
    }

}

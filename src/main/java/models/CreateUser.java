package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Все геттеры, сеттеры и return
@AllArgsConstructor // Конструктор со всеми полями
@NoArgsConstructor  // Пустой конструктор для Jackson
public class CreateUser {
    private String email;
    private String password;
    private String name;
}

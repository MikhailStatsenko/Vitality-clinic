package com.vitality.clinic.model;

import com.vitality.clinic.model.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    @Email(message = "Логин должен соответствовать формату электронной почты")
    @Size(min = 5, max = 100, message = "Логин содержать от 5 до 100 символов")
    @Column(name = "login")
    private String login;

    @NotBlank(message = "Поле не может быть пустым")
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;
}

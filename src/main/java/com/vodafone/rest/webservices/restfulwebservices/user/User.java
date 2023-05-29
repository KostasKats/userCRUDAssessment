package com.vodafone.rest.webservices.restfulwebservices.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Entity(name="user_infos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank(message = "Username cannot be null")
    private String username;

    @NotBlank(message = "Email cannot be null")
    @Email
    private String email;
    private String name;
    private String lastname;


}

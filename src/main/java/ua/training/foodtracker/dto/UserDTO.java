package ua.training.foodtracker.dto;

import lombok.*;

import javax.persistence.Column;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDTO {

    private String username;
    private String password;
    private String firstName;
}
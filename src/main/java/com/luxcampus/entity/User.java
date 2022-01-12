package com.luxcampus.entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class User {
    private int id;
    private String name;
    private String lastName;
    private String email;
    //hashed
    private String password;

}

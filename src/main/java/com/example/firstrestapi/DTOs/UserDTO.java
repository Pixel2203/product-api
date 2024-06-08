package com.example.firstrestapi.DTOs;

import java.util.Date;

public record UserDTO (int id, String prename, String name, String email, String password, Date birth) {
}

package com.AluguelDeCarros.dto.authorization;

import com.AluguelDeCarros.entity.user.UserRole;

public record RegisterDto(String login, String password, UserRole role)  {
}

package com.AluguelDeCarros.service.authorization;


import com.AluguelDeCarros.dto.authorization.AuthenticationDto;
import com.AluguelDeCarros.dto.authorization.RegisterDto;
import com.AluguelDeCarros.entity.user.User;
import com.AluguelDeCarros.exceptions.PasswordValidationException;
import com.AluguelDeCarros.repository.authorization.UserRepository;
import com.AluguelDeCarros.security.TokenService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String login(AuthenticationDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        return tokenService.generateToken((User) auth.getPrincipal());
    }

    public void register(RegisterDto data) {
        if (this.repository.findByLogin(data.login()) != null) {
            throw new IllegalArgumentException("User already exists");
        }

        // Validar a senha antes de registrar o usuário
        List<String> validationFailures = validate(data.password());
        if (!validationFailures.isEmpty()) {
            throw new PasswordValidationException("Senha Invalida!!!\n" + String.join("\n", validationFailures));
        }

        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, data.role());
        this.repository.save(newUser);
    }

    public List<String> validate(String pass) {
        List<String> failures = new ArrayList<>();
        validateLength(pass, failures);
        validateUppercase(pass, failures);
        validateLowercase(pass, failures);
        validateNumber(pass, failures);
        validateSpecialChars(pass, failures);
        validateNoSequentialChars(pass, failures);

        return failures;
    }

    private static void validateLength(String pass, List<String> failures) {
        if (StringUtils.isBlank(pass) || pass.length() < 8) {
            failures.add("A senha deve possuir pelo menos 08 caracteres.");
        }
    }

    private static void validateUppercase(String pass, List<String> failures) {
        if (!Pattern.matches(".*[A-Z].*", pass)) {
            failures.add("A senha deve possuir pelo menos uma letra maiúscula.");
        }
    }

    private static void validateLowercase(String pass, List<String> failures) {
        if (!Pattern.matches(".*[a-z].*", pass)) {
            failures.add("A senha deve possuir pelo menos uma letra minúscula.");
        }
    }

    private static void validateNumber(String pass, List<String> failures) {
        if (!Pattern.matches(".*[0-9].*", pass)) {
            failures.add("A senha deve possuir pelo menos um numero.");
        }
    }
    private static void validateSpecialChars(String pass, List<String> failures) {
        if (!Pattern.matches(".*[\\W].*", pass)) {
            failures.add("A senha deve possuir pelo menos um caractere especial.");
        }
    }

    //VALIDAÇÃO DE SEQUENCIA DE NUMEROS E LETRAS
    private static void validateNoSequentialChars(String pass, List<String> failures) {
        if (hasSequentialNumbers(pass) || hasSequentialLetters(pass)) {
            failures.add("A senha não pode conter sequências numéricas ou de letras.");
        }
    }

    private static boolean hasSequentialNumbers(String pass) {
        for (int i = 0; i < pass.length() - 2; i++) {
            char first = pass.charAt(i);
            char second = pass.charAt(i + 1);
            char third = pass.charAt(i + 2);

            if (Character.isDigit(first) && Character.isDigit(second) && Character.isDigit(third)) {
                int firstNum = Character.getNumericValue(first);
                int secondNum = Character.getNumericValue(second);
                int thirdNum = Character.getNumericValue(third);

                // Verificar se é sequência crescente ou decrescente
                if ((secondNum == firstNum + 1 && thirdNum == secondNum + 1) ||
                        (secondNum == firstNum - 1 && thirdNum == secondNum - 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasSequentialLetters(String pass) {
        for (int i = 0; i < pass.length() - 2; i++) {
            char first = pass.charAt(i);
            char second = pass.charAt(i + 1);
            char third = pass.charAt(i + 2);

            if (Character.isLetter(first) && Character.isLetter(second) && Character.isLetter(third)) {
                if ((second == first + 1 && third == second + 1) ||
                        (second == first - 1 && third == second - 1)) {
                    return true;
                }
            }
        }
        return false;
    }
}

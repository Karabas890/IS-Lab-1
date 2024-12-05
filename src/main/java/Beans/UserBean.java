package Beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class UserBean implements Serializable {
    private String username;
    private String password;

    private String newUsername;
    private String newPassword;
    private String confirmPassword;

    // Геттеры и сеттеры
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    // Метод для обработки логина
    public String login() {
        // Пример логики: Проверяем пользователя
        if ("test".equals(username) && "password".equals(password)) {
            return "home.xhtml?faces-redirect=true"; // Перенаправление на страницу home.xhtml
        } else {
            // Выводим ошибку (можно интегрировать с JSF message)
            return null;
        }
    }

    // Метод для обработки регистрации
    public String register() {
        if (newPassword.equals(confirmPassword)) {
            // Пример логики регистрации
            System.out.println("User registered: " + newUsername);
            return "home.xhtml?faces-redirect=true"; // Перенаправление на страницу home.xhtml
        } else {
            // Выводим сообщение об ошибке (можно интегрировать с JSF message)
            return null;
        }
    }
}

package Beans;

import dao.UserDAO;
import entities.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
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

    private boolean loggedIn;

    @Inject
    private UserDAO userDAO;

    // Геттеры и сеттеры для username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Геттеры и сеттеры для password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Геттеры и сеттеры для newUsername
    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    // Геттеры и сеттеры для newPassword
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    // Геттеры и сеттеры для confirmPassword
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    // Метод для обработки логина
    public String login() {
        User user = userDAO.findByUsernameAndPassword(username, password);
        if (user != null) {
            loggedIn = true;
            return "home.xhtml?faces-redirect=true"; // Успешный вход
        } else {
            loggedIn = false;
            // Можно добавить сообщение об ошибке
            return null; // Ошибка входа
        }
    }

    // Метод для обработки регистрации
    public String register() {
        if (newPassword.equals(confirmPassword)) {
            User newUser = new User(newUsername, newPassword);
            userDAO.save(newUser);
            return "index.xhtml?faces-redirect=true"; // Перенаправление на страницу логина
        } else {
            // Выводим сообщение об ошибке
            return null; // Ошибка регистрации
        }
    }

    // Метод для выхода
    public String logout() {
        loggedIn = false;
        username = null;
        password = null;
        return "index.xhtml?faces-redirect=true";
    }
}

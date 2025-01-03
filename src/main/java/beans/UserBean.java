package beans;

import dao.UserDAO;
import entities.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@Named
@SessionScoped
public class UserBean implements Serializable {
    private String username;
    private String password;

    private String newUsername;
    private String newPassword;
    private String confirmPassword;

    private boolean loggedIn;

    private String errorMessage; // Для сообщений об ошибках
    private String registrationErrorMessage; // Для ошибок регистрации
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
    // Геттеры и сеттеры для errorMessage
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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
    // Геттеры и сеттеры для registrationErrorMessage
    public String getRegistrationErrorMessage() {
        return registrationErrorMessage;
    }

    public void setRegistrationErrorMessage(String registrationErrorMessage) {
        this.registrationErrorMessage = registrationErrorMessage;
    }

    // Метод для обработки логина
    public String login() {
        User user = userDAO.findByUsernameAndPassword(username, password);
        if (user == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Неверное имя пользователя или пароль", "Неверное имя пользователя или пароль"));
            return null;
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Вы успешно вошли в систему", "Вы успешно вошли в систему"));
        return "home?faces-redirect=true";
    }


    public String register() {
        FacesContext context = FacesContext.getCurrentInstance();

        // Проверяем заполненность всех полей
        if (newUsername == null || newUsername.isEmpty() || newPassword == null || newPassword.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Все поля должны быть заполнены.", "Все поля должны быть заполнены."));
            return null;
        }

        // Проверяем совпадение паролей
        if (!newPassword.equals(confirmPassword)) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Пароли не совпадают.", "Пароли не совпадают."));
            return null;
        }

        // Проверяем существование пользователя
        User existingUser = userDAO.findByUsername(newUsername);
        if (existingUser != null) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Пользователь с таким именем уже существует.", "Пользователь с таким именем уже существует."));
            return null;
        }

        // Создаем нового пользователя
        User newUser = new User();
        newUser.setUsername(newUsername);
        newUser.setPassword(newPassword); // Рекомендуется зашифровать пароль перед сохранением
        userDAO.save(newUser);

        context.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Регистрация успешна", "Регистрация успешна"));
        return null;
    }


    // Метод для выхода
    public String logout() {
        loggedIn = false;
        username = null;
        password = null;
        return "index.xhtml?faces-redirect=true";
    }
}

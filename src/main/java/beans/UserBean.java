package beans;

import services.UserService;
import entities.User;
import enums.Role;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
public class UserBean implements Serializable {
    private String username;
    private String password;

    private String newUsername;
    private String newPassword;
    private String confirmPassword;
    private Role role;

    private boolean loggedIn;

    private String errorMessage; // Для сообщений об ошибках
    private String registrationErrorMessage; // Для ошибок регистрации
    @Inject
    private UserService userService;
    // Геттеры и сеттеры для username
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
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

    public String login() {
        System.out.println("Login starts");
        User user = userService.findByUsernameAndPassword(username, password);
        if (user == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Неверное имя пользователя или пароль", "Неверное имя пользователя или пароль"));
            return null;
        }

        // Сохраняем имя пользователя в сессии
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", user.getUsername());
        loggedIn = true;

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
        User existingUser = userService.findByUsername(newUsername);
        if (existingUser != null) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Пользователь с таким именем уже существует.", "Пользователь с таким именем уже существует."));
            return null;
        }

        // Создаем нового пользователя
        User newUser = new User();
        newUser.setUsername(newUsername);
        newUser.setPassword(newPassword); // Рекомендуется зашифровать пароль перед сохранением
        newUser.setRole(Role.USER);
        userService.save(newUser);

        context.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Регистрация успешна", "Регистрация успешна"));
        return null;
    }


    public String logout() {
        loggedIn = false;
        username = null;
        password = null;

        // Удаляем данные из сессии
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("username");

        return "index.xhtml?faces-redirect=true";
    }

    public User getCurrentUser() {
        // Логика получения текущего пользователя из контекста
        System.out.println("getCurrentUser moment: ");
        String currentUserName = userService.getCurrentUserName(); // Метод или поле для получения имени пользователя
        return userService.findByUsername(currentUserName);
    }
}

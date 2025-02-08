package beans;

import services.UserService;
import entities.User;
import enums.Role;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    private boolean requestAdminRights; // Новое поле для запроса прав администратора
    private User user;
    // Новое поле для статуса запроса на получение прав администратора
    private String requestStatusMessage; // Статус запроса на права админа

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
    public String getRequestStatusMessage() {
        return requestStatusMessage;
    }

    public void setRequestStatusMessage(String requestStatusMessage) {
        this.requestStatusMessage = requestStatusMessage;
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
        // Хэшируем введенный пароль
        String hashedPassword = hashPassword(password);
        User checkUser = userService.findByUsernameAndPassword(username, hashedPassword);
        if (checkUser == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Неверное имя пользователя или пароль", "Неверное имя пользователя или пароль"));
            return null;
        }

        // Сохраняем имя пользователя в сессии
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", checkUser.getUsername());
        loggedIn = true;

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Вы успешно вошли в систему", "Вы успешно вошли в систему"));
        user=checkUser;
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
        // Проверка длины пароля (не менее 3 символов)
        if (newPassword.length() < 3) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Пароль должен содержать не менее 3 символов.", "Пароль должен содержать не менее 3 символов."));
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
        // Хэшируем пароль с использованием SHA-256
        String hashedPassword = hashPassword(newPassword);

        // Создаем нового пользователя
        User newUser = new User();
        newUser.setUsername(newUsername);
        newUser.setPassword(hashedPassword); // Рекомендуется зашифровать пароль перед сохранением
        newUser.setRole(Role.USER);
        userService.save(newUser);

        context.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Регистрация успешна", "Регистрация успешна"));
        return null;
    }
    // Метод для хэширования пароля с использованием SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка при хэшировании пароля", e);
        }
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
    // Метод для запроса прав администратора
    public void requestAdminRights() {
        if (user == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Вы не авторизованы."));
            return;
        }

        // Изменяем поле requestAdminRights на true
        user.setRequestAdminRights(true);

        // Сохраняем изменения в базе данных
        userService.update(user);

        // Устанавливаем сообщение о статусе запроса
        requestStatusMessage = "Ваш запрос на получение прав администратора был отправлен.";

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Запрос отправлен", requestStatusMessage));
    }
    // Геттеры и сеттеры для requestAdminRights
    public boolean isRequestAdminRights() {
        return requestAdminRights;
    }

    public void setRequestAdminRights(boolean requestAdminRights) {
        this.requestAdminRights = requestAdminRights;
        loadPendingRequests();
    }
    private List<User> pendingRequests;

    public List<User> getPendingRequests() {
        loadPendingRequests();
        return pendingRequests;
    }

    public void setPendingRequests(List<User> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }
    public void approveAdminRequest(User user) {
        System.out.println("approveAdminRequest moment");
        user.setRequestAdminRights(false);
        user.setRole(Role.ADMIN); // Назначаем роль ADMIN
        userService.save(user); // Сохраняем изменения в базе данных
        loadPendingRequests(); // Перезагружаем список заявок
    }

    public void rejectAdminRequest(User user) {
        user.setRequestAdminRights(false); // Отклоняем заявку
        userService.save(user); // Сохраняем изменения в базе данных
        loadPendingRequests(); // Перезагружаем список заявок
    }

    private void loadPendingRequests() {
        pendingRequests = userService.findUsersWithPendingAdminRights(); // Загружаем список пользователей с активными заявками
    }
}

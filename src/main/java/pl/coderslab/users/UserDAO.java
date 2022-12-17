package pl.coderslab.users;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.utils.DbUtil;

import java.sql.*;
import java.util.Arrays;


public class UserDAO {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String SELECT_USER_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, username = ?, password = ? WHERE id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public User create(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            //Pobieramy wstawiony do bazy identyfikator, a następnie ustawiamy id obiektu user.
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(SELECT_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, String.valueOf(userId));
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            User user = new User();
            if (resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String checkAndSetPassword(User user) {
           User userReadFromDatabase = this.read(user.getId());
                String cryptedPassword = userReadFromDatabase.getPassword();
                if (BCrypt.checkpw(user.getPassword(), cryptedPassword)) {
                    return cryptedPassword;
                }
                return this.hashPassword(user.getPassword());
    }

    public void update(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUserName());
            statement.setString(3, this.checkAndSetPassword(user));
            statement.setInt(4, user.getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int userId) {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_USER_QUERY);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User[] addToArray(User user, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1); // Tworzymy kopię tablicy powiększoną o 1.
        tmpUsers[users.length] = user; // Dodajemy obiekt na ostatniej pozycji.
        return tmpUsers;
    } // Zwracamy nową tablicę.

    public User[] findAll() {
        User[] allUsers = new User[0];
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_USERS);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                User userToAddToArray = this.read(id);
                allUsers = addToArray(userToAddToArray, allUsers);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;

    }


}

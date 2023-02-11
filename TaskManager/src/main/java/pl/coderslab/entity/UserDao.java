package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;
import pl.coderslab.User;

import java.sql.*;
import java.util.Arrays;

public class UserDao {
    public static final String CREATE_USER_QUERY = "INSERT INTO users(email, username, password) VALUES (?, ?, ?)";
    public static final String READ_USER_QUERY = "SELECT * FROM users WHERE id = ? ";
    public static final String DELETE_USER_QUERY = "DELETE  FROM users WHERE id = ?;";
    public static final String READ_ALL_USER_QUERY = "SELECT * FROM users";
    public static final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, username = ?, password = ? WHERE id = ?";

    public static String hashPassword(String password) {

        return BCrypt.hashpw(password, BCrypt.gensalt());



    }

    public User create(User user) {

        try (Connection conn = DbUtil.connect()) {
            PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUsername());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
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

    public  User read(int valId)  {
        try (Connection conn = DbUtil.connect()) {
            PreparedStatement statement = conn.prepareStatement(READ_USER_QUERY);
            statement.setInt(1, valId);
            ResultSet resultSet = statement.executeQuery();
            User user = new User();

                while (resultSet.next()) {
                    user.setId(resultSet.getInt("id"));
                    user.setEmail(resultSet.getString("email"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));



                }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();

        }
//        return null;

    }
    public void delete(int userId){
        try (Connection conn = DbUtil.connect()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_USER_QUERY);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public User[] findAll()
    {
        try (Connection conn = DbUtil.connect()) {
            PreparedStatement statement = conn.prepareStatement(READ_ALL_USER_QUERY);
            ResultSet resultSet = statement.executeQuery();
            User[] users = new User[0];
            User user = new User();
                while (resultSet.next()) {

                    user.setId(resultSet.getInt("id"));
                    user.setEmail(resultSet.getString("email"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    users = Arrays.copyOf(users, users.length +1 );
                    users[users.length -1] = user;



                }
                return users;

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;

    }
    public void update(User user) {

        try (Connection conn = DbUtil.connect()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_USER_QUERY);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUsername());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();

        }

    }
}

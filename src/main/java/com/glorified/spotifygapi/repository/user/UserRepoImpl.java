package com.glorified.spotifygapi.repository.user;

import com.glorified.spotifygapi.models.requests.LoginRequest;
import com.glorified.spotifygapi.models.user.User;
import com.glorified.spotifygapi.repository.SqlAbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepoImpl extends SqlAbstractRepository implements UserRepository {
    @Override
    public User login(LoginRequest loginRequest) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = this.newConnection();
            System.out.println("PASSWORD " + loginRequest.getPassword());
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE email=? AND password=?"
            );
            preparedStatement.setString(1,loginRequest.getEmail());
            preparedStatement.setString(2,loginRequest.getPassword());
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;

    }

    @Override
    public User createUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"};

            preparedStatement = connection.prepareStatement(
                    "INSERT INTO users ( username, password,email) VALUES (?,?,?)",
                    generatedColumns
            );

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2,user.getPassword());
            preparedStatement.setString(3,user.getEmail());


            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next()){
                user.setId(resultSet.getInt(1));
            }
            if(resultSet == null)
            {
                System.out.println("NULLLA");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return user;
    }

    @Override
    public User findUser(String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE email = ?");

            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return user;
    }

}

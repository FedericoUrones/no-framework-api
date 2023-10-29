package org.api.noframework.infrastructure;

import org.api.noframework.domain.NewTask;
import org.api.noframework.domain.Task;
import org.api.noframework.domain.TaskRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskDBRepository implements TaskRepository {

    private static TaskDBRepository instance;

    public static TaskDBRepository getInstance() {
        if(instance == null) {
            instance = new TaskDBRepository();
        }
        return instance;
    }

    Connection connection = null;

    private TaskDBRepository() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite::memory:");
            System.out.println("Connection to SQLite has been established.");
            createDBObjects();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void createDBObjects() throws SQLException {
        Statement statement = connection.createStatement();

        String sql = "CREATE TABLE Tasks " +
                "(id TEXT PRIMARY KEY," +
                " description TEXT NOT NULL)";
        statement.executeUpdate(sql);
        System.out.println("Tables created.");
    }

    @Override
    public String create(NewTask task) {

        String sql = "INSERT INTO Tasks (id, description) VALUES (?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String id = UUID.randomUUID().toString();
            statement.setString(1, id);
            statement.setString(2, task.getDescription());
            statement.executeUpdate();
            System.out.println("Insert executed.");
            return id;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Task> getAll() {
        String sql = "SELECT * FROM Tasks";
        List<Task> tasks = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            System.out.println("Query getAll executed.");
            while (rs.next()) {
                Task resultTask = new Task(rs.getString("id"), rs.getString("description"));
                tasks.add(resultTask);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tasks;
    }

    @Override
    public Task getById(String id) {
        String sql = "SELECT * FROM Tasks where id = (?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            System.out.println("Query getById executed.");
            if (rs.next()) {
                return new Task(rs.getString("id"), rs.getString("description"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Task update(String taskId, Task task) {
        String sql = "UPDATE Tasks SET description = (?) where id = (?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, task.getDescription());
            statement.setString(2, taskId);
            statement.executeUpdate();
            System.out.println("Update executed.");
            return task;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM Tasks where id = (?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
            System.out.println("Delete executed.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

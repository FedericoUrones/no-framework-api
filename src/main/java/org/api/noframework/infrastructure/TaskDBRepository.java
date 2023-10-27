package org.api.noframework.infrastructure;

import org.api.noframework.domain.NewTask;
import org.api.noframework.domain.Task;
import org.api.noframework.domain.TaskRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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
            System.out.println("Tables created.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void createDBObjects() throws SQLException {
        Statement stmt = connection.createStatement();

        String sql = "CREATE TABLE Tasks " +
                "(id TEXT PRIMARY KEY," +
                " description TEXT NOT NULL)";
        stmt.executeUpdate(sql);
    }

    @Override
    public String create(NewTask task) {
        return null;
    }

    @Override
    public List<Task> getAll() {
        return null;
    }

    @Override
    public Task getById(String id) {
        return null;
    }

    @Override
    public Task update(String taskId, Task task) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}

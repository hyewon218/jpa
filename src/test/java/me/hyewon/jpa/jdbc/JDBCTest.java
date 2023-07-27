package me.hyewon.jpa.jdbc;

import me.hyewon.jpa.jdbc.dao.AccountDAO;
import me.hyewon.jpa.jdbc.vo.AccountVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCTest {

    @Test
    @DisplayName("JDBC DB 연결 실습")
    void jdbcTest() throws SQLException {
        // given
        // docker run -p 5432:5432 -e POSTGRES_PASSWORD=pass -e POSTGRES_USER=hyewon -e POSTGRES_DB=messenger --name postgres_boot -d postgres
        // docker exec -i -t postgres_boot bash
        // su - postgres
        // psql --username hyewon --dbname messenger
        // \list (데이터 베이스 조회)
        // \dt (테이블 조회)

        // IntelliJ Database 에서도 조회

        String url = "jdbc:postgresql://localhost:5432/messenger";
        String username = "hyewon";
        String password = "pass";

        // when
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try {
                String creatSql = "CREATE TABLE ACCOUNT (id SERIAL PRIMARY KEY, username varchar(255), password varchar(255))";
                try (PreparedStatement statement = connection.prepareStatement(creatSql)) {
                    statement.execute();
                }
            } catch (SQLException e) {
                if (e.getMessage().equals("ERROR: relation \"account\" already exists")) {
                    System.out.println("ACCOUNT 테이블이 이미 존재합니다.");
                } else {
                    throw new RuntimeException();
                }
            }
        }
        // then
        // DB 확인
    }

    @Test
    @DisplayName("JDBC 삽입/조회 실습")
    void jdbcInsertSelectTest() throws SQLException {
        // given
        String url = "jdbc:postgresql://localhost:5432/messenger";
        String username = "hyewon";
        String password = "pass";

        // when
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connection created: " + connection);

            String insertSql = "INSERT INTO ACCOUNT (id, username, password) VALUES ((SELECT coalesce(MAX(ID), 0) + 1 FROM ACCOUNT A), 'user1', 'pass1')";
            try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
                statement.execute();
            }

            // then
            String selectSql = "SELECT * FROM ACCOUNT";
            try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
                var rs = statement.executeQuery();
                while (rs.next()) {
                    System.out.printf("%d, %s, %s", rs.getInt("id"), rs.getString("username"),
                            rs.getString("password"));
                }
            }
        }
    }

    @Test
    @DisplayName("JDBC DAO 삽입/조회 실습")
    void jdbcDAOInsertSelectTest() throws SQLException {
        // given
        AccountDAO accountDAO = new AccountDAO();

        // when
        var id = accountDAO.insertAccount(new AccountVO("new user", "new password"));

        // then
        var account = accountDAO.selectAccount(id);
        assert account.getUsername().equals("new user");
    }
}

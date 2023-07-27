package me.hyewon.jpa.jdbc;// 이외 의존성 클래스는 아래 깃헙링크 참조
// https://github.com/thesun4sky/jpastudy/tree/work-tea-1day/src/test/java/me/whitebear/jpastudy/jdbc

import me.hyewon.jpa.jdbc.template.AccountTemplateDAO;
import me.hyewon.jpa.jdbc.vo.AccountVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;

@JdbcTest // Jdbc Slice Test
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 테스트용 DB 쓰지 않도록
@Rollback(value = false) // Transactional 에 있는 테스트 변경은 기본적으론 롤백 하도록 되어있다.
public class JDBCTemplateTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("SQL Mapper - JDBC Template 실습")
    void sqlMapper_JDBCTemplateTest() {
        // given
        var accountTemplateDAO = new AccountTemplateDAO(jdbcTemplate);

        // when
        var id = accountTemplateDAO.insertAccount(new AccountVO("new user2", "new password2"));

        // then
        var account = accountTemplateDAO.selectAccount(id);
        assert account.getUsername().equals("new user2");
    }
}

package me.hyewon.jpa.mybatis.mapper;

import me.hyewon.jpa.mybatis.vo.AccountMyBatisVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper // 알아서 mapper 인터페이스를 bean 으로 만들어준다. (원래는 DBConfiguration 에 mapping location 설정을 해줘야 했음)
public interface AccountMapperV2 {

    @Select("SELECT * FROM account WHERE id = #{id}")
    AccountMyBatisVO selectAccount(Integer id);

    @Insert("INSERT INTO ACCOUNT (username, password) VALUES (#{username}, #{password})")
    void insertAccount(AccountMyBatisVO vo);
}
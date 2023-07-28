package me.hyewon.jpa.mybatis.mapper;

import me.hyewon.jpa.mybatis.vo.AccountMyBatisVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountMapper {

    AccountMyBatisVO selectAccount(@Param("id") int id);

    void insertAccount(AccountMyBatisVO vo); // vo 로 return 하기 위해 AccountMyBatisVO 호출
}
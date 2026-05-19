package com.example.backform.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserMapper {
    Map<String, Object> findByAccount(@Param("account") String account);
    int countByUsername(@Param("username") String username);
    int countByEmail(@Param("email") String email);
    int insertUser(@Param("username") String username, @Param("email") String email, @Param("passwordHash") String passwordHash, @Param("nickname") String nickname);
    Map<String, Object> findProfileById(@Param("id") Long id);
    int updateLastLogin(@Param("id") Long id);
    int updatePasswordHashByUsername(@Param("username") String username, @Param("passwordHash") String passwordHash);
}

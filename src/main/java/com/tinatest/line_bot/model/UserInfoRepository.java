package com.tinatest.line_bot.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfoEntity, String> {

    List<UserInfoEntity> findByNotifyTrueAndApproveTrueAndAccessTokenIsNotNull();

//    UserInfoEntity findByUserIdContains(String code);

    @Query(value="SELECT * FROM user_info WHERE user_id like %?1%", nativeQuery = true)
    UserInfoEntity findByUserIdLike(String code);

    UserInfoEntity findByAdminAndUserId(boolean admin, String userId);

    UserInfoEntity findByUserId(String userId);

    List<UserInfoEntity> findByUserLineIdIsNotNull();
}

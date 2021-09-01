package com.tinatest.line_bot.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfoEntity, String> {

    List<UserInfoEntity> findByNotifyAndApprove(boolean notify, boolean approve);

    UserInfoEntity findByUserIdContains(String code);

    UserInfoEntity findByAdminAndUserId(boolean admin, String userId);
}

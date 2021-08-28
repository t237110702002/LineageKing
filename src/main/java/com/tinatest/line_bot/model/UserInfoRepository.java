package com.tinatest.line_bot.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfoEntity, String> {

    List<UserInfoEntity> findByNotify(boolean notify);

}

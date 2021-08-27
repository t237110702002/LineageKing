package com.tinatest.line_bot.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineageKingInfoRepository extends CrudRepository<LineageKingInfoEntity, String> {

    LineageKingInfoEntity findByKingName(String name);
}

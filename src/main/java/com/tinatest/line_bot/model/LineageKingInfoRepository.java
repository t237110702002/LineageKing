package com.tinatest.line_bot.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineageKingInfoRepository extends CrudRepository<LineageKingInfoEntity, String> {

    LineageKingInfoEntity findByKingName(String name);

    List<LineageKingInfoEntity> findAllByOrderByNextAppear();

    List<LineageKingInfoEntity> findTop10ByOrderByNextAppear();
}

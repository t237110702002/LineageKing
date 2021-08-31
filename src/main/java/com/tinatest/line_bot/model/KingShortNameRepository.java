package com.tinatest.line_bot.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KingShortNameRepository extends CrudRepository<KingShortNameEntity, String> {

    KingShortNameEntity findByShortName(String shortName);

    List<KingShortNameEntity> findByKingNameOrShortName(String name, String shortName);
}

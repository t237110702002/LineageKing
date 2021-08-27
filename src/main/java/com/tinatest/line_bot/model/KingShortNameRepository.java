package com.tinatest.line_bot.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KingShortNameRepository extends CrudRepository<KingShortNameEntity, String> {

    KingShortNameEntity findByShortName(String shortName);
}

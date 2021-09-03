package com.tinatest.line_bot.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LineageKingInfoRepository extends CrudRepository<LineageKingInfoEntity, String> {

    LineageKingInfoEntity findByKingName(String name);

    List<LineageKingInfoEntity> findAllByOrderByNextAppear();

    @Query(value="SELECT * FROM lineage_king_info WHERE next_appear >= ?1 ORDER BY next_appear", nativeQuery = true)
    List<LineageKingInfoEntity> getAppearFromNow(Date date);

}

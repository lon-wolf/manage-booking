package com.booking.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.booking.db.models.Inventroy;

@Repository
public interface InventoryRepository extends JpaRepository<Inventroy, Long> {

	@Query("SELECT i FROM inventory i WHERE (i.start < :start AND i.end >= :start) OR (i.start >= :start AND i.start <= :end) AND i.type = :type ORDER BY i.start")
	List<Inventroy> findByStartAndEndAndType(@Param("start") long start, @Param("end") long end,
			@Param("type") Integer type);

}

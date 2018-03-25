package com.booking.db.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.booking.db.models.Inventroy;

@Repository
public interface InventoryRepository extends JpaRepository<Inventroy, Long> {

	@Query("SELECT i FROM inventory i WHERE (i.start <= :end OR i.end >= :start) AND i.type = :type ORDER BY i.start")
	List<Inventroy> findByStartAndEndAndType(@Param("start") Date start, @Param("end") Date end,
			@Param("type") Integer type);

	@Query("SELECT i FROM inventory i WHERE i.start <= :end OR i.end >= :start ORDER BY i.start")
	List<Inventroy> findByStartAndEnd(@Param("start") Date start, @Param("end") Date end);

}

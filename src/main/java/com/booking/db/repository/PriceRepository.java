package com.booking.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.booking.db.models.Price;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

	@Query("SELECT p FROM price p WHERE (p.start < :start AND p.end >= :start) OR (p.start >= :start AND p.start <= :end) AND p.type = :type ORDER BY p.start")
	List<Price> findByStartAndEndAndType(@Param("start") long start, @Param("end") long end,
			@Param("type") Integer type);

}

package com.booking.db.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.booking.db.models.Price;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

	@Query("SELECT p FROM price p WHERE p.start <= :end OR p.end >= :start OR p.day IS NOT NULL ORDER BY p.start")
	List<Price> findByStartAndEnd(@Param("start") Date start, @Param("end") Date end);
}

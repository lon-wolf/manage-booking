CREATE DATABASE manage_booking;
CREATE TABLE `price` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `price` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `start` BIGINT DEFAULT NULL,
  `end` BIGINT DEFAULT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
);
CREATE INDEX price_start
ON price (start);
CREATE INDEX price_end
ON price (end);
CREATE TABLE `inventory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `available` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `start` BIGINT DEFAULT NULL,
  `end` BIGINT DEFAULT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`)
);
CREATE INDEX inventory_start
ON inventory (start);
CREATE INDEX inventory_end
ON inventory (end);
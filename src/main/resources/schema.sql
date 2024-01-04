
CREATE TABLE `favorite_location` (
  `favorite_location_id` bigint NOT NULL AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `location_id` varchar(20) NOT NULL,
  PRIMARY KEY (`favorite_location_id`),
  KEY `location_id` (`location_id`),
  KEY `member_id` (`member_id`),
  CONSTRAINT `favorite_location_ibfk_1` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`),
  CONSTRAINT `favorite_location_ibfk_2` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3;


CREATE TABLE `member` (
  `member_id` bigint NOT NULL AUTO_INCREMENT,
  `reg_time` datetime(6) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `UK_mbmcqelty0fbrvxp1q58dn57t` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE location(
   location_id     VARCHAR(20) NOT NULL PRIMARY KEY
  ,name            VARCHAR(36) NOT NULL
  ,zone            VARCHAR(12)
  ,tol_describe    VARCHAR(1972) NOT NULL
  ,description     VARCHAR(400)
  ,tel             VARCHAR(31)
  ,address         VARCHAR(58)
  ,zipcode         VARCHAR(5)
  ,region          VARCHAR(3)
  ,town            VARCHAR(4)
  ,travelling_info VARCHAR(200)
  ,open_time       VARCHAR(203)
  ,picture1        VARCHAR(119)
  ,pic_describe1   VARCHAR(133)
  ,picture2        VARCHAR(118)
  ,pic_describe2   VARCHAR(200)
  ,picture3        VARCHAR(100)
  ,pic_describe3   VARCHAR(198)
  ,map             VARCHAR(66)
  ,gov             VARCHAR(10)
  ,longitude       VARCHAR(10) NOT NULL
  ,latitude        VARCHAR(9) NOT NULL
  ,orgclass        VARCHAR(9)
  ,class1          VARCHAR(2) NOT NULL
  ,class2          VARCHAR(2)
  ,class3          VARCHAR(2)
  ,level           VARCHAR(1)
  ,website         VARCHAR(127)
  ,parking_info    VARCHAR(93)
  ,parking_info_px VARCHAR(10)
  ,parking_info_py VARCHAR(9)
  ,ticket_info     VARCHAR(100)
  ,remarks         VARCHAR(98)
  ,keyword         VARCHAR(50)
  ,change_time     VARCHAR(19)
);


CREATE TABLE city(
   postal_address_city varchar(10) NOT NULL PRIMARY KEY
   ,city_detail varchar(200)
  ,position_lat                  NUMERIC(9,6) NOT NULL
  ,position_lon                  NUMERIC(10,6) NOT NULL
);
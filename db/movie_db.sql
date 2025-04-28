/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80039 (8.0.39)
 Source Host           : localhost:3306
 Source Schema         : movie_db

 Target Server Type    : MySQL
 Target Server Version : 80039 (8.0.39)
 File Encoding         : 65001

 Date: 11/12/2024 14:42:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for movie
-- ----------------------------
DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cast` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `director` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `duration` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `genre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `mpaa_rating` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `price` float NOT NULL,
  `producer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `synopsis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `trailer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of movie
-- ----------------------------
INSERT INTO `movie` VALUES (1, NULL, 'currently-running', '2024-11-08 11:50:25', 'MV of Runaway by Kanye West,Kanye1 follows a visionary music producer whose rise to fame brings creativity, controversy, and personal challenges, exploring the high stakes of ambition and artistry.', 'Kanye West', '1 hr 45', 'Comedy', '4.8', 20, 'Kanye West', NULL, 'Kanye1', 'https://www.youtube.com/embed/afLEae9_KSA');
INSERT INTO `movie` VALUES (2, NULL, 'coming-soon', '2024-11-25 11:53:27', 'As fame grows, personal struggles and controversial decisions threaten to unravel his legacy. A powerful exploration of artistry, identity, and the cost of greatness.', 'Shawn Levy', '1 hr 30 ', 'Action', '4', 50, 'Hugh', NULL, 'Kanye2', 'https://www.youtube.com/embed/6-1CwFumx_M');
INSERT INTO `movie` VALUES (3, NULL, 'coming-soon', '2024-12-11 13:41:59', 'For over a year, a series of bold daylight bank robberies and armored car heists leaves law enforcement baffled and the public panicked throughout the Pacific Northwest. As the attacks become increasingly violent.', 'Jude Law', '1hr 56 ', 'Mystery', '3', 30, 'Justin Kurzel', NULL, 'The Order', 'https://youtube.com/embed/pPZaOB4ea-0');
INSERT INTO `movie` VALUES (4, NULL, 'currently-running', '2024-12-11 13:37:28', 'Deadpool\'s peaceful existence comes crashing down when the Time Variance Authority recruits him to help safeguard the multiverse. He soon unites with his would-be pal, Wolverine, to complete the mission and save his world from an existential threat.', 'Shawn Levy', '1h 12', 'Action', '3.8', 56, 'Rob Delaney', NULL, 'Deadpool ', 'https://youtube.com/embed/BCiNjLq0S60?si=wRGbXNn8wyWPzWm6');

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `age_group` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` float NOT NULL,
  `order_id` bigint NOT NULL,
  `schedule_id` bigint NOT NULL,
  `seat_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKt4dc2r9nbvbujrljv3e23iibt`(`order_id` ASC) USING BTREE,
  INDEX `FK44infkblpvl1vn0oi5f7jkx2w`(`schedule_id` ASC) USING BTREE,
  INDEX `FKoxqjd59fosi5kqvv9jd0vtlk3`(`seat_id` ASC) USING BTREE,
  CONSTRAINT `FK44infkblpvl1vn0oi5f7jkx2w` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKoxqjd59fosi5kqvv9jd0vtlk3` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKt4dc2r9nbvbujrljv3e23iibt` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (4, 'adult', 56, 2, 6, 126);
INSERT INTO `order_item` VALUES (5, 'child', 39.2, 2, 6, 127);
INSERT INTO `order_item` VALUES (6, 'senior', 47.6, 2, 6, 128);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `sales_tax` float NULL DEFAULT NULL,
  `shipping_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `total_cost` float NOT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `payment_id` bigint NOT NULL,
  `promotion_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK8aol9f99s97mtyhij0tvfj41f`(`payment_id` ASC) USING BTREE,
  INDEX `FKkl19lst67x545047o4n1d0jpv`(`promotion_id` ASC) USING BTREE,
  CONSTRAINT `FK8aol9f99s97mtyhij0tvfj41f` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKkl19lst67x545047o4n1d0jpv` FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (2, '2024-12-11 19:39:31.333698', 5, '123 Main apt 103 ', 133.52, NULL, 72, 6);

-- ----------------------------
-- Table structure for payment_cards
-- ----------------------------
DROP TABLE IF EXISTS `payment_cards`;
CREATE TABLE `payment_cards`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `card_holder_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `card_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `card_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `expiry_date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UKq1ht2ihu4bf52ert31s9t556q`(`card_number` ASC) USING BTREE,
  INDEX `FKbs8w6tjl44mr8778hwv1orct5`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FKbs8w6tjl44mr8778hwv1orct5` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payment_cards
-- ----------------------------

-- ----------------------------
-- Table structure for payments
-- ----------------------------
DROP TABLE IF EXISTS `payments`;
CREATE TABLE `payments`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `bank_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `card_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `cvv` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `expiry` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `payment_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `routing_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_id` bigint NOT NULL,
  `is_delete` bit(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKj94hgy9v5fw1munb90tar2eje`(`user_id` ASC) USING BTREE,
  CONSTRAINT `FKj94hgy9v5fw1munb90tar2eje` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of payments
-- ----------------------------
INSERT INTO `payments` VALUES (72, '', '', '123456789', '441', '05/16', 'Credit Card', '', 2, b'0');
INSERT INTO `payments` VALUES (73, '123456', 'boa', '', '', '', 'Bank', '123456', 2, b'1');
INSERT INTO `payments` VALUES (74, '', '', '', '', '', '', '', 2, b'0');

-- ----------------------------
-- Table structure for promotion
-- ----------------------------
DROP TABLE IF EXISTS `promotion`;
CREATE TABLE `promotion`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `discount` int NOT NULL,
  `expiration_date` date NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UKlomfalb6gsh66ox4gy0t2g7qw`(`code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of promotion
-- ----------------------------
INSERT INTO `promotion` VALUES (6, 'summer', 10, '2024-12-12');
INSERT INTO `promotion` VALUES (7, 'tade', 20, '2024-12-10');

-- ----------------------------
-- Table structure for schedule
-- ----------------------------
DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `showtime` datetime(6) NOT NULL,
  `movie_id` bigint NOT NULL,
  `showroom_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKa6hosaihwhtb3scvamdyh9mlv`(`movie_id` ASC) USING BTREE,
  INDEX `FKpseo8bfy8q8571rtacnyx7al9`(`showroom_id` ASC) USING BTREE,
  CONSTRAINT `FKa6hosaihwhtb3scvamdyh9mlv` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKpseo8bfy8q8571rtacnyx7al9` FOREIGN KEY (`showroom_id`) REFERENCES `showroom` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule
-- ----------------------------
INSERT INTO `schedule` VALUES (5, NULL, '2024-12-12 21:37:00.000000', 1, 1);
INSERT INTO `schedule` VALUES (6, NULL, '2024-12-14 02:37:00.000000', 4, 2);
INSERT INTO `schedule` VALUES (7, NULL, '2024-12-14 22:42:00.000000', 1, 3);

-- ----------------------------
-- Table structure for schedule_seat
-- ----------------------------
DROP TABLE IF EXISTS `schedule_seat`;
CREATE TABLE `schedule_seat`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_available` bit(1) NOT NULL,
  `schedule_id` bigint NOT NULL,
  `seat_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKtq21x5uhvs88cim8fq34u2sn9`(`schedule_id` ASC) USING BTREE,
  INDEX `FKhea3c96p6234u76dj5xat31n6`(`seat_id` ASC) USING BTREE,
  CONSTRAINT `FKhea3c96p6234u76dj5xat31n6` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKtq21x5uhvs88cim8fq34u2sn9` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule_seat
-- ----------------------------

-- ----------------------------
-- Table structure for seat
-- ----------------------------
DROP TABLE IF EXISTS `seat`;
CREATE TABLE `seat`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `seat_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `showroom_id` bigint NOT NULL,
  `is_available` bit(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKhorsv21pcm7t1y74l94t6lya3`(`showroom_id` ASC) USING BTREE,
  CONSTRAINT `FKhorsv21pcm7t1y74l94t6lya3` FOREIGN KEY (`showroom_id`) REFERENCES `showroom` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 175 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of seat
-- ----------------------------
INSERT INTO `seat` VALUES (110, 'S1', 1, b'1');
INSERT INTO `seat` VALUES (111, 'S2', 1, b'1');
INSERT INTO `seat` VALUES (112, 'S3', 1, b'1');
INSERT INTO `seat` VALUES (113, 'S4', 1, b'1');
INSERT INTO `seat` VALUES (114, 'S5', 1, b'1');
INSERT INTO `seat` VALUES (115, 'S6', 1, b'1');
INSERT INTO `seat` VALUES (116, 'S7', 1, b'1');
INSERT INTO `seat` VALUES (117, 'S8', 1, b'1');
INSERT INTO `seat` VALUES (118, 'S9', 1, b'1');
INSERT INTO `seat` VALUES (119, 'S10', 1, b'1');
INSERT INTO `seat` VALUES (120, 'S11', 1, b'1');
INSERT INTO `seat` VALUES (121, 'S12', 1, b'1');
INSERT INTO `seat` VALUES (122, 'S13', 1, b'1');
INSERT INTO `seat` VALUES (123, 'S14', 1, b'1');
INSERT INTO `seat` VALUES (124, 'S15', 1, b'1');
INSERT INTO `seat` VALUES (125, 'S1', 2, b'1');
INSERT INTO `seat` VALUES (126, 'S2', 2, b'0');
INSERT INTO `seat` VALUES (127, 'S3', 2, b'0');
INSERT INTO `seat` VALUES (128, 'S4', 2, b'0');
INSERT INTO `seat` VALUES (129, 'S5', 2, b'1');
INSERT INTO `seat` VALUES (130, 'S6', 2, b'1');
INSERT INTO `seat` VALUES (131, 'S7', 2, b'1');
INSERT INTO `seat` VALUES (132, 'S8', 2, b'1');
INSERT INTO `seat` VALUES (133, 'S9', 2, b'1');
INSERT INTO `seat` VALUES (134, 'S10', 2, b'1');
INSERT INTO `seat` VALUES (135, 'S11', 2, b'1');
INSERT INTO `seat` VALUES (136, 'S12', 2, b'1');
INSERT INTO `seat` VALUES (137, 'S13', 2, b'1');
INSERT INTO `seat` VALUES (138, 'S14', 2, b'1');
INSERT INTO `seat` VALUES (139, 'S15', 2, b'1');
INSERT INTO `seat` VALUES (140, 'S16', 2, b'1');
INSERT INTO `seat` VALUES (141, 'S17', 2, b'1');
INSERT INTO `seat` VALUES (142, 'S18', 2, b'1');
INSERT INTO `seat` VALUES (143, 'S19', 2, b'1');
INSERT INTO `seat` VALUES (144, 'S20', 2, b'1');
INSERT INTO `seat` VALUES (145, 'S1', 3, b'1');
INSERT INTO `seat` VALUES (146, 'S2', 3, b'1');
INSERT INTO `seat` VALUES (147, 'S3', 3, b'1');
INSERT INTO `seat` VALUES (148, 'S4', 3, b'1');
INSERT INTO `seat` VALUES (149, 'S5', 3, b'1');
INSERT INTO `seat` VALUES (150, 'S6', 3, b'1');
INSERT INTO `seat` VALUES (151, 'S7', 3, b'1');
INSERT INTO `seat` VALUES (152, 'S8', 3, b'1');
INSERT INTO `seat` VALUES (153, 'S9', 3, b'1');
INSERT INTO `seat` VALUES (154, 'S10', 3, b'1');
INSERT INTO `seat` VALUES (155, 'S11', 3, b'1');
INSERT INTO `seat` VALUES (156, 'S12', 3, b'1');
INSERT INTO `seat` VALUES (157, 'S13', 3, b'1');
INSERT INTO `seat` VALUES (158, 'S14', 3, b'1');
INSERT INTO `seat` VALUES (159, 'S15', 3, b'1');
INSERT INTO `seat` VALUES (160, 'S16', 3, b'1');
INSERT INTO `seat` VALUES (161, 'S17', 3, b'1');
INSERT INTO `seat` VALUES (162, 'S18', 3, b'1');
INSERT INTO `seat` VALUES (163, 'S19', 3, b'1');
INSERT INTO `seat` VALUES (164, 'S20', 3, b'1');
INSERT INTO `seat` VALUES (165, 'S21', 3, b'1');
INSERT INTO `seat` VALUES (166, 'S22', 3, b'1');
INSERT INTO `seat` VALUES (167, 'S23', 3, b'1');
INSERT INTO `seat` VALUES (168, 'S24', 3, b'1');
INSERT INTO `seat` VALUES (169, 'S25', 3, b'1');
INSERT INTO `seat` VALUES (170, 'S26', 3, b'1');
INSERT INTO `seat` VALUES (171, 'S27', 3, b'1');
INSERT INTO `seat` VALUES (172, 'S28', 3, b'1');
INSERT INTO `seat` VALUES (173, 'S29', 3, b'1');
INSERT INTO `seat` VALUES (174, 'S30', 3, b'1');

-- ----------------------------
-- Table structure for showroom
-- ----------------------------
DROP TABLE IF EXISTS `showroom`;
CREATE TABLE `showroom`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `capacity` int NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of showroom
-- ----------------------------
INSERT INTO `showroom` VALUES (1, 15, 'R1');
INSERT INTO `showroom` VALUES (2, 20, 'R2');
INSERT INTO `showroom` VALUES (3, 30, 'R3');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `receive_promotion` bit(1) NULL DEFAULT NULL,
  `role` enum('ADMIN','USER') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','SUSPENDED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK6dotkott2kjsp8vw4d0m25fb7`(`email` ASC) USING BTREE,
  UNIQUE INDEX `UKr43af9ap4edm43mmtq01oddj6`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (2, 'University of Georgia', 'qitaotan02@gmail.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '54210782012', b'1', 'ADMIN', 'ACTIVE', '123');
INSERT INTO `users` VALUES (6, 'univeristy of georgia', 'test@gmail.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '78954612', b'1', 'USER', 'INACTIVE', '123456');

SET FOREIGN_KEY_CHECKS = 1;

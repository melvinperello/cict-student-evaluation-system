-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.2.10-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win32
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for cictems
CREATE DATABASE IF NOT EXISTS `cictems` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `cictems`;

-- Dumping structure for table cictems.backup_logs
CREATE TABLE IF NOT EXISTS `backup_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time_executed` datetime DEFAULT current_timestamp(),
  `backup_result` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `executed_with` int(11) DEFAULT NULL,
  `executed_on` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `backup_mode` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `active` tinyint(4) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `backup_log_faculty_id` (`executed_with`),
  CONSTRAINT `backup_log_faculty_id` FOREIGN KEY (`executed_with`) REFERENCES `faculty` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.backup_schedule
CREATE TABLE IF NOT EXISTS `backup_schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `created_date` datetime DEFAULT current_timestamp(),
  `active` tinyint(4) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `backup_schedule_faculty_id` (`created_by`),
  CONSTRAINT `backup_schedule_faculty_id` FOREIGN KEY (`created_by`) REFERENCES `faculty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

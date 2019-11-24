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


-- Dumping database structure for sms_server
CREATE DATABASE IF NOT EXISTS `sms_server` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `sms_server`;

-- Dumping structure for table sms_server.exception_log
CREATE TABLE IF NOT EXISTS `exception_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT current_timestamp(),
  `count` int(11) DEFAULT 0,
  `message` varchar(500) DEFAULT '',
  `active` int(11) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table sms_server.exception_log: ~0 rows (approximately)
/*!40000 ALTER TABLE `exception_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `exception_log` ENABLE KEYS */;

-- Dumping structure for table sms_server.sms_log
CREATE TABLE IF NOT EXISTS `sms_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gateway_id` varchar(150) DEFAULT '',
  `message_uuid` varchar(200) DEFAULT '',
  `date` datetime DEFAULT NULL,
  `recipient` varchar(50) DEFAULT '',
  `dispatch_date` datetime DEFAULT NULL,
  `message_status` varchar(50) DEFAULT '',
  `failure_cause` varchar(100) DEFAULT '',
  `message_body` varchar(160) DEFAULT '',
  `pdu_data` varchar(200) DEFAULT '',
  `entry_date` datetime DEFAULT current_timestamp(),
  `active` tinyint(4) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- Dumping data for table sms_server.sms_log: ~0 rows (approximately)
/*!40000 ALTER TABLE `sms_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_log` ENABLE KEYS */;

-- Dumping structure for table sms_server.sms_query
CREATE TABLE IF NOT EXISTS `sms_query` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reciepients_number` varchar(50) DEFAULT NULL,
  `message_body` varchar(160) DEFAULT '',
  `sender_name` varchar(50) DEFAULT '',
  `date` datetime DEFAULT current_timestamp(),
  `status` int(11) DEFAULT 0,
  `active` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

-- Dumping data for table sms_server.sms_query: ~0 rows (approximately)
/*!40000 ALTER TABLE `sms_query` DISABLE KEYS */;
/*!40000 ALTER TABLE `sms_query` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

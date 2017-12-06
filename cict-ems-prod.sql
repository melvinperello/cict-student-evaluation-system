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

-- Dumping structure for table cictems.academic_program
CREATE TABLE IF NOT EXISTS `academic_program` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `floor_assignment` int(11) DEFAULT NULL,
  `created_date` datetime DEFAULT current_timestamp(),
  `created_by` int(11) DEFAULT NULL,
  `removed_date` datetime DEFAULT NULL,
  `removed_by` int(11) DEFAULT NULL,
  `implemented` tinyint(4) DEFAULT 0,
  `implementation_date` datetime DEFAULT NULL,
  `implemented_by` int(11) DEFAULT NULL,
  `active` int(11) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `created_by` (`created_by`),
  KEY `implemented_by` (`implemented_by`),
  KEY `removed` (`removed_by`),
  CONSTRAINT `created_by` FOREIGN KEY (`created_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `implemented_by` FOREIGN KEY (`implemented_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `removed` FOREIGN KEY (`removed_by`) REFERENCES `faculty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.academic_term
CREATE TABLE IF NOT EXISTS `academic_term` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `school_year` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `semester` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `semester_regular` int(11) DEFAULT NULL,
  `current` int(11) DEFAULT 0,
  `evaluation_service` int(11) DEFAULT 0,
  `es_updater` int(11) DEFAULT NULL,
  `es_update_date` datetime DEFAULT NULL,
  `adding_service` int(11) DEFAULT 0,
  `as_updater` int(11) DEFAULT NULL,
  `as_update_date` datetime DEFAULT NULL,
  `encoding_service` int(11) DEFAULT 0,
  `ens_updater` int(11) DEFAULT NULL,
  `ens_update_date` datetime DEFAULT NULL,
  `type` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'REGULAR',
  `approval_state` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'PENDING',
  `active` int(11) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `academic_term_as_updater` (`as_updater`),
  KEY `academic_term_es_updater` (`es_updater`),
  KEY `acadenuc_term_encs_updater` (`ens_updater`),
  CONSTRAINT `academic_term_as_updater` FOREIGN KEY (`as_updater`) REFERENCES `faculty` (`id`),
  CONSTRAINT `academic_term_es_updater` FOREIGN KEY (`es_updater`) REFERENCES `faculty` (`id`),
  CONSTRAINT `acadenuc_term_encs_updater` FOREIGN KEY (`ens_updater`) REFERENCES `faculty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.account_faculty
CREATE TABLE IF NOT EXISTS `account_faculty` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `FACULTY_id` int(11) DEFAULT NULL,
  `assigned_cluster` int(11) DEFAULT NULL,
  `username` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `transaction_pin` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `recovery_question` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `recovery_answer` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `access_level` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `blocked_count` int(11) DEFAULT 0,
  `disabled_since` datetime DEFAULT NULL,
  `wrong_count` int(11) DEFAULT 0,
  `blocked_until` datetime DEFAULT NULL,
  `active` int(11) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `account_faculty_fk_from_faculty_id` (`FACULTY_id`),
  CONSTRAINT `account_faculty_fk_from_faculty_id` FOREIGN KEY (`FACULTY_id`) REFERENCES `faculty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.account_faculty_attempt
CREATE TABLE IF NOT EXISTS `account_faculty_attempt` (
  `try_id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) DEFAULT NULL,
  `time` datetime DEFAULT current_timestamp(),
  `ip_address` varchar(200) COLLATE utf8_unicode_ci DEFAULT 'UNKNOWN',
  `pc_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'UNKNOWN',
  `pc_username` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'UNKNOWN',
  `os_version` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'UNKNOWN',
  `platform` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'UNKNOWN',
  `result` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'UNKNOWN',
  `active` int(11) DEFAULT 1,
  PRIMARY KEY (`try_id`),
  KEY `attempt_fk_from_account_faculty_id` (`account_id`),
  CONSTRAINT `attempt_fk_from_account_faculty_id` FOREIGN KEY (`account_id`) REFERENCES `account_faculty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2844 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.account_faculty_session
CREATE TABLE IF NOT EXISTS `account_faculty_session` (
  `session_id` int(11) NOT NULL AUTO_INCREMENT,
  `FACULTY_account_id` int(11) NOT NULL DEFAULT 0,
  `session_start` datetime DEFAULT NULL,
  `keep_alive` datetime DEFAULT NULL,
  `ip_address` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pc_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pc_username` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `os` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `session_end` datetime DEFAULT NULL,
  `platform` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'Unknown',
  `active` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`session_id`),
  KEY `session_fk_from_account_faculty_id` (`FACULTY_account_id`),
  CONSTRAINT `session_fk_from_account_faculty_id` FOREIGN KEY (`FACULTY_account_id`) REFERENCES `account_faculty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2661 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.account_student
CREATE TABLE IF NOT EXISTS `account_student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `STUDENT_id` int(11) DEFAULT NULL,
  `username` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `recovery_question` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `recovery_answer` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `access_level` varchar(100) COLLATE utf8_unicode_ci DEFAULT 'STUDENT',
  `affiliates` varchar(100) COLLATE utf8_unicode_ci DEFAULT 'NONE',
  `state` int(11) DEFAULT 0,
  `tries` int(11) DEFAULT 0 COMMENT 'same as faculty wrong count',
  `blocked_until` datetime DEFAULT NULL,
  `active` int(11) DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `STUDENT_id` (`STUDENT_id`),
  CONSTRAINT `acc_student_fk_from_student_id` FOREIGN KEY (`STUDENT_id`) REFERENCES `student` (`cict_id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.announcements
CREATE TABLE IF NOT EXISTS `announcements` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) COLLATE utf8_unicode_ci DEFAULT '',
  `message` varchar(300) COLLATE utf8_unicode_ci DEFAULT '',
  `date` datetime DEFAULT current_timestamp(),
  `announced_by` int(11) DEFAULT NULL,
  `active` int(11) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `announce_by_fk` (`announced_by`),
  CONSTRAINT `announce_by_fk` FOREIGN KEY (`announced_by`) REFERENCES `faculty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.curriculum
CREATE TABLE IF NOT EXISTS `curriculum` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `ACADPROG_id` int(30) DEFAULT NULL,
  `major` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'NO DESCRIPTION',
  `ladderization` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ladderization_type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `study_years` int(11) DEFAULT NULL,
  `created_date` datetime DEFAULT current_timestamp(),
  `created_by` int(11) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` int(11) DEFAULT NULL,
  `removed_date` datetime DEFAULT NULL,
  `removed_by` int(11) DEFAULT NULL,
  `implemented` tinyint(4) DEFAULT 0,
  `implementation_date` datetime DEFAULT NULL,
  `implemented_by` int(11) DEFAULT NULL,
  `obsolete_term` int(11) DEFAULT 0,
  `active` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `curriculum_fk_from_academic_program_id` (`ACADPROG_id`),
  KEY `curriculum_fk_created_by` (`created_by`),
  KEY `curriculum_fk_removed_by` (`removed_by`),
  KEY `curriculum_fk_implemented_by` (`implemented_by`),
  KEY `curriculum_fk_updated_by` (`updated_by`),
  CONSTRAINT `curriculum_fk_created_by` FOREIGN KEY (`created_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `curriculum_fk_from_academic_program_id` FOREIGN KEY (`ACADPROG_id`) REFERENCES `academic_program` (`id`),
  CONSTRAINT `curriculum_fk_implemented_by` FOREIGN KEY (`implemented_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `curriculum_fk_removed_by` FOREIGN KEY (`removed_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `curriculum_fk_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `faculty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.curriculum_history
CREATE TABLE IF NOT EXISTS `curriculum_history` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT,
  `CURRICULUM_id` int(11) DEFAULT NULL,
  `major` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ladderization` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ladderization_type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `study_years` int(11) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` int(11) DEFAULT NULL,
  `removed_date` datetime DEFAULT NULL,
  `removed_by` int(11) DEFAULT NULL,
  `implemented` int(11) DEFAULT NULL,
  `implementation_date` datetime DEFAULT NULL,
  `implemented_by` int(11) DEFAULT NULL,
  `active` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.curriculum_history_summary
CREATE TABLE IF NOT EXISTS `curriculum_history_summary` (
  `history_id` int(11) NOT NULL AUTO_INCREMENT,
  `curriculum_id` int(11) DEFAULT NULL,
  `description` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `created_date` datetime DEFAULT current_timestamp(),
  `active` tinyint(4) DEFAULT 1,
  PRIMARY KEY (`history_id`),
  KEY `cur_history_fk_curriculum_id` (`curriculum_id`),
  KEY `cur_history_fk_faculty` (`created_by`),
  CONSTRAINT `cur_history_fk_curriculum_id` FOREIGN KEY (`curriculum_id`) REFERENCES `curriculum` (`id`),
  CONSTRAINT `cur_history_fk_faculty` FOREIGN KEY (`created_by`) REFERENCES `faculty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.curriculum_pre
CREATE TABLE IF NOT EXISTS `curriculum_pre` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `curriculum_id_get` int(11) DEFAULT NULL,
  `curriculum_id_req` int(11) DEFAULT NULL,
  `cur_type` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'PRIMARY' COMMENT 'PRIMARY, EQUIVALENT',
  `created_date` datetime NOT NULL DEFAULT current_timestamp(),
  `created_by` int(11) DEFAULT NULL,
  `removed_date` datetime DEFAULT NULL,
  `removed_by` int(11) DEFAULT NULL,
  `active` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `get_fk` (`curriculum_id_get`),
  KEY `req_fk` (`curriculum_id_req`),
  KEY `cur_pre_created` (`created_by`),
  KEY `cur_pre_removed` (`removed_by`),
  CONSTRAINT `cur_pre_created` FOREIGN KEY (`created_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `cur_pre_removed` FOREIGN KEY (`removed_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `get_fk` FOREIGN KEY (`curriculum_id_get`) REFERENCES `curriculum` (`id`),
  CONSTRAINT `req_fk` FOREIGN KEY (`curriculum_id_req`) REFERENCES `curriculum` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.curriculum_requisite_ext
CREATE TABLE IF NOT EXISTS `curriculum_requisite_ext` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `SUBJECT_id_get` int(11) DEFAULT NULL,
  `SUBJECT_id_req` int(11) DEFAULT NULL,
  `CURRICULUM_id` int(11) DEFAULT NULL,
  `type` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'COREQUISITE',
  `added_by` int(11) DEFAULT NULL,
  `added_date` datetime DEFAULT current_timestamp(),
  `removed_by` int(11) DEFAULT NULL,
  `removed_date` datetime DEFAULT current_timestamp(),
  `active` tinyint(4) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `req_ext_get` (`SUBJECT_id_get`),
  KEY `req_ext_req` (`SUBJECT_id_req`),
  KEY `req_ext_cur` (`CURRICULUM_id`),
  KEY `req_ext_added_by` (`added_by`),
  KEY `req_ext_updated_by` (`removed_by`),
  CONSTRAINT `req_ext_added_by` FOREIGN KEY (`added_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `req_ext_cur` FOREIGN KEY (`CURRICULUM_id`) REFERENCES `curriculum` (`id`),
  CONSTRAINT `req_ext_get` FOREIGN KEY (`SUBJECT_id_get`) REFERENCES `subject` (`id`),
  CONSTRAINT `req_ext_req` FOREIGN KEY (`SUBJECT_id_req`) REFERENCES `subject` (`id`),
  CONSTRAINT `req_ext_updated_by` FOREIGN KEY (`removed_by`) REFERENCES `faculty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.curriculum_requisite_line
CREATE TABLE IF NOT EXISTS `curriculum_requisite_line` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `SUBJECT_id_get` int(11) DEFAULT NULL,
  `SUBJECT_id_req` int(11) DEFAULT NULL,
  `CURRICULUM_id` int(11) DEFAULT NULL,
  `created_date` datetime DEFAULT current_timestamp(),
  `created_by` int(11) DEFAULT NULL,
  `removed_date` datetime DEFAULT NULL,
  `removed_by` int(11) DEFAULT NULL,
  `active` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `curriculum_requisite_fk_from_subject_id` (`SUBJECT_id_get`),
  KEY `curriculum_requisite_fk_req_from_subject_id` (`SUBJECT_id_req`),
  KEY `cur_fk` (`CURRICULUM_id`),
  KEY `cur_req_created` (`created_by`),
  KEY `cur_req_removed` (`removed_by`),
  CONSTRAINT `cur_fk` FOREIGN KEY (`CURRICULUM_id`) REFERENCES `curriculum` (`id`),
  CONSTRAINT `cur_req_created` FOREIGN KEY (`created_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `cur_req_removed` FOREIGN KEY (`removed_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `curriculum_requisite_fk_from_subject_id` FOREIGN KEY (`SUBJECT_id_get`) REFERENCES `subject` (`id`),
  CONSTRAINT `curriculum_requisite_fk_req_from_subject_id` FOREIGN KEY (`SUBJECT_id_req`) REFERENCES `subject` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.curriculum_subject
CREATE TABLE IF NOT EXISTS `curriculum_subject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `CURRICULUM_id` int(11) DEFAULT NULL,
  `SUBJECT_id` int(11) DEFAULT NULL,
  `year` int(11) NOT NULL DEFAULT 0,
  `semester` int(11) NOT NULL DEFAULT 0 COMMENT '3 for mid year',
  `sequence` int(11) NOT NULL DEFAULT 0,
  `added_by` int(11) DEFAULT NULL,
  `added_date` datetime DEFAULT current_timestamp(),
  `removed_by` int(11) DEFAULT NULL,
  `removed_date` datetime DEFAULT current_timestamp(),
  `active` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `curriculum_subjects_fk_from_curriculum_id` (`CURRICULUM_id`),
  KEY `curriculum_subjects_fk_from_subject_id` (`SUBJECT_id`),
  KEY `cursub_fk_added_by` (`added_by`),
  KEY `cursub_fk_updated_by` (`removed_by`),
  CONSTRAINT `curriculum_subjects_fk_from_curriculum_id` FOREIGN KEY (`CURRICULUM_id`) REFERENCES `curriculum` (`id`),
  CONSTRAINT `curriculum_subjects_fk_from_subject_id` FOREIGN KEY (`SUBJECT_id`) REFERENCES `subject` (`id`),
  CONSTRAINT `cursub_fk_added_by` FOREIGN KEY (`added_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `cursub_fk_updated_by` FOREIGN KEY (`removed_by`) REFERENCES `faculty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=331 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.evaluation
CREATE TABLE IF NOT EXISTS `evaluation` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `STUDENT_id` int(11) DEFAULT NULL,
  `ACADTERM_id` int(11) DEFAULT NULL,
  `FACULTY_id` int(11) DEFAULT NULL,
  `adding_reference_id` int(11) DEFAULT NULL COMMENT 'the previous evaluation reference',
  `evaluation_date` datetime DEFAULT NULL,
  `year_level` int(11) DEFAULT NULL,
  `type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'REGULAR, , ADDING/CHANGING, SUMMER, MIDYEAR',
  `remarks` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'ACCEPTED' COMMENT 'OVERWRITTEN',
  `print_type` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'NOT_SET',
  `cancelled_by` int(11) DEFAULT NULL,
  `cancelled_date` datetime DEFAULT NULL,
  `check_mode` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'CHECKED' COMMENT 'CHECKED AND IGNORED',
  `active` int(1) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `evaluation_fk_from_student_cict_id` (`STUDENT_id`),
  KEY `evaluation_fk_from_academic_term_id` (`ACADTERM_id`),
  KEY `evaluation_fk_from_faculty_id` (`FACULTY_id`),
  KEY `evaluation_fk_self` (`adding_reference_id`),
  CONSTRAINT `evaluation_fk_from_academic_term_id` FOREIGN KEY (`ACADTERM_id`) REFERENCES `academic_term` (`id`),
  CONSTRAINT `evaluation_fk_from_faculty_id` FOREIGN KEY (`FACULTY_id`) REFERENCES `faculty` (`id`),
  CONSTRAINT `evaluation_fk_from_student_cict_id` FOREIGN KEY (`STUDENT_id`) REFERENCES `student` (`cict_id`),
  CONSTRAINT `evaluation_fk_self` FOREIGN KEY (`adding_reference_id`) REFERENCES `evaluation` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='has a duplication of student_id in the load_subjects';

-- Data exporting was unselected.
-- Dumping structure for table cictems.faculty
CREATE TABLE IF NOT EXISTS `faculty` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `bulsu_id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `first_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `middle_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name_extension` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'the third jr sr',
  `mobile_number` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `gender` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rank` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `designation` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `active` int(1) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.faculty_profile
CREATE TABLE IF NOT EXISTS `faculty_profile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `birthdate` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `place_of_birth` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sex` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `civil_status` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `height` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `weight` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `blood_type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `GSIS_id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PAGIBIG_id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PHILHEALTH_id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SSS_id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TIN_no` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `citizenship` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dual_citizenship` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dual_type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `dual_country` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `res_house` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `res_street` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `res_village` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `res_brgy` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `res_city` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `res_province` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `res_zip` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `addr_house` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `addr_street` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `addr_village` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `addr_brgy` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `addr_city` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `addr_province` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `addr_zip` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `telephone` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mobile` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `spouse_surname` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `spouse_firstname` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `spouse_middlename` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `spouse_name_ext` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `spouse_occupation` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `spouse_work_agency` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `spouse_work_address` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `spouse_telephone` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `father_surname` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `father_firstname` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `father_middlename` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `father_name_ext` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mother_maidenname` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mother_surname` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mother_firstname` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mother_middlename` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.grade
CREATE TABLE IF NOT EXISTS `grade` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `STUDENT_id` int(30) DEFAULT NULL,
  `SUBJECT_id` int(30) DEFAULT NULL,
  `ACADTERM_id` int(30) DEFAULT NULL,
  `rating` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'UNPOSTED',
  `remarks` varchar(100) COLLATE utf8_unicode_ci DEFAULT 'UNPOSTED',
  `credit` double DEFAULT 0,
  `credit_method` varchar(100) COLLATE utf8_unicode_ci DEFAULT 'REGULAR' COMMENT 'REGULAR, CREDIT, CROSS_ENROLL',
  `created_by` int(11) DEFAULT NULL COMMENT 'FACULTY ID',
  `created_date` datetime DEFAULT current_timestamp(),
  `posted` int(1) DEFAULT 0,
  `posted_by` int(11) DEFAULT NULL COMMENT 'EMS-01-JOEMAR DELA CRUZ',
  `posting_date` datetime DEFAULT NULL,
  `inc_expire` datetime DEFAULT NULL COMMENT 'if remarks is INC +1 year in the posting date else make it default',
  `updated_by` int(11) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `reason_for_update` varchar(50) COLLATE utf8_unicode_ci DEFAULT '' COMMENT 'IF the grade will be changed',
  `referrence_curriculum` int(11) DEFAULT NULL,
  `course_reference` int(11) DEFAULT NULL,
  `grade_state` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'ACCEPTED',
  `active` int(1) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `grade_fk_from_student_id` (`STUDENT_id`),
  KEY `grade_fk_from_subject_id` (`SUBJECT_id`),
  KEY `grade_fk_from_academic_term_id` (`ACADTERM_id`),
  KEY `grade_fk_from_faculty_id_posted` (`posted_by`),
  KEY `grade_fk_from_faculty_id_update` (`updated_by`),
  KEY `grade_fk_from_faculty_id_created` (`created_by`),
  KEY `grade_fk_ref_cur` (`referrence_curriculum`),
  KEY `grade_fk_course_ref` (`course_reference`),
  CONSTRAINT `grade_fk_course_ref` FOREIGN KEY (`course_reference`) REFERENCES `student_course_history` (`id`),
  CONSTRAINT `grade_fk_from_academic_term_id` FOREIGN KEY (`ACADTERM_id`) REFERENCES `academic_term` (`id`),
  CONSTRAINT `grade_fk_from_faculty_id_created` FOREIGN KEY (`created_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `grade_fk_from_faculty_id_posted` FOREIGN KEY (`posted_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `grade_fk_from_faculty_id_update` FOREIGN KEY (`updated_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `grade_fk_from_student_id` FOREIGN KEY (`STUDENT_id`) REFERENCES `student` (`cict_id`),
  CONSTRAINT `grade_fk_from_subject_id` FOREIGN KEY (`SUBJECT_id`) REFERENCES `subject` (`id`),
  CONSTRAINT `grade_fk_ref_cur` FOREIGN KEY (`referrence_curriculum`) REFERENCES `curriculum` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=685 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='evaluation id will be null until evaluated';

-- Data exporting was unselected.
-- Dumping structure for table cictems.linked_entrance
CREATE TABLE IF NOT EXISTS `linked_entrance` (
  `reference_id` int(11) NOT NULL AUTO_INCREMENT,
  `student_number` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'NONE',
  `faculty_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'NONE',
  `floor_assignment` int(11) DEFAULT NULL,
  `active` tinyint(4) DEFAULT 1,
  PRIMARY KEY (`reference_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.linked_marshall_session
CREATE TABLE IF NOT EXISTS `linked_marshall_session` (
  `ses_id` int(11) NOT NULL AUTO_INCREMENT,
  `cict_id` int(11) DEFAULT NULL,
  `account_id` int(11) DEFAULT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `org` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `imei` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `session_start` datetime DEFAULT current_timestamp(),
  `session_end` datetime DEFAULT NULL,
  `status` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'ONLINE',
  `active` int(11) DEFAULT 1,
  PRIMARY KEY (`ses_id`),
  KEY `marshall_session_fk_from_student_cict_id` (`cict_id`),
  KEY `marshall_session_fk_from_account_student_id` (`account_id`),
  CONSTRAINT `marshall_session_fk_from_account_student_id` FOREIGN KEY (`account_id`) REFERENCES `account_student` (`id`),
  CONSTRAINT `marshall_session_fk_from_student_cict_id` FOREIGN KEY (`cict_id`) REFERENCES `student` (`cict_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.linked_pila
CREATE TABLE IF NOT EXISTS `linked_pila` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ACADTERM_id` int(11) DEFAULT NULL,
  `STUDENT_id` int(11) DEFAULT NULL,
  `ACCOUNT_id` int(11) DEFAULT NULL,
  `SETTINGS_id` int(11) DEFAULT NULL,
  `NOTIFIED` int(11) DEFAULT 0,
  `conforme` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `course` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `imei` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `request_accepted` datetime DEFAULT current_timestamp(),
  `request_called` datetime DEFAULT NULL,
  `request_validity` datetime DEFAULT NULL,
  `floor_assignment` int(11) DEFAULT 0,
  `floor_number` int(11) DEFAULT 0,
  `cluster_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT '',
  `status` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'INLINE' COMMENT 'INLINE, VOID, CALLED',
  `remarks` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'NONE',
  `active` int(11) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `pila_fk_from_academic_term_id` (`ACADTERM_id`),
  KEY `pila_fk_from_student_cict_id` (`STUDENT_id`),
  KEY `pila_fk_from_account_student_id` (`ACCOUNT_id`),
  CONSTRAINT `pila_fk_from_academic_term_id` FOREIGN KEY (`ACADTERM_id`) REFERENCES `academic_term` (`id`),
  CONSTRAINT `pila_fk_from_account_student_id` FOREIGN KEY (`ACCOUNT_id`) REFERENCES `account_student` (`id`),
  CONSTRAINT `pila_fk_from_student_cict_id` FOREIGN KEY (`STUDENT_id`) REFERENCES `student` (`cict_id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.linked_pila_3f
CREATE TABLE IF NOT EXISTS `linked_pila_3f` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pila_id` int(11) DEFAULT NULL,
  `active` int(11) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `pila_fk_id` (`pila_id`),
  CONSTRAINT `pila_fk_id` FOREIGN KEY (`pila_id`) REFERENCES `linked_pila` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.linked_pila_4f
CREATE TABLE IF NOT EXISTS `linked_pila_4f` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pila_id` int(11) DEFAULT NULL,
  `active` int(11) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `pila_fk` (`pila_id`),
  CONSTRAINT `pila_fk` FOREIGN KEY (`pila_id`) REFERENCES `linked_pila` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.linked_settings
CREATE TABLE IF NOT EXISTS `linked_settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `floor_3_max` int(11) DEFAULT 0,
  `floor_4_max` int(11) DEFAULT 0,
  `floor_3_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `floor_4_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `floor_3_last` int(11) DEFAULT 0,
  `floor_4_last` int(11) DEFAULT 0,
  `floor_3_cut` int(11) DEFAULT 0,
  `floor_4_cut` int(11) DEFAULT 0,
  `created_by` int(11) DEFAULT NULL,
  `created_date` datetime DEFAULT current_timestamp(),
  `mark` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'ALIVE',
  `active` int(11) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.linked_telemetry
CREATE TABLE IF NOT EXISTS `linked_telemetry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `class_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Unknown',
  `method_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Unknown',
  `line` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Unknown',
  `classification` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Unknown',
  `exception_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Unknown',
  `description` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Unknown',
  `student_number` varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Unknown',
  `student_name` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Unknown',
  `version` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Unknown',
  `build` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Unknown',
  `model` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Unknown',
  `board` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Unknown',
  `brand` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Unknown',
  `hardware` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Unknown',
  `manufacturer` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Unknown',
  `serial` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Unknown',
  `date_submitted` datetime NOT NULL DEFAULT current_timestamp(),
  `active` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.load_group
CREATE TABLE IF NOT EXISTS `load_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `SUBJECT_id` int(11) DEFAULT NULL,
  `LOADSEC_id` int(11) DEFAULT NULL,
  `faculty` int(11) DEFAULT NULL,
  `group_type` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'REGULAR' COMMENT 'REGULAR,TUTORIAL',
  `added_date` datetime DEFAULT current_timestamp(),
  `added_by` int(11) DEFAULT NULL,
  `removed_date` datetime DEFAULT NULL,
  `removed_by` int(11) DEFAULT NULL,
  `active` int(11) DEFAULT 1,
  `archived` int(11) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `load_group_fk_from_subject_id` (`SUBJECT_id`),
  KEY `load_group_fk_from_load_section_id` (`LOADSEC_id`),
  CONSTRAINT `load_group_fk_from_load_section_id` FOREIGN KEY (`LOADSEC_id`) REFERENCES `load_section` (`id`),
  CONSTRAINT `load_group_fk_from_subject_id` FOREIGN KEY (`SUBJECT_id`) REFERENCES `subject` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35975 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='all the subjects in that section';

-- Data exporting was unselected.
-- Dumping structure for table cictems.load_group_schedule
CREATE TABLE IF NOT EXISTS `load_group_schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `load_group_id` int(11) DEFAULT NULL,
  `class_day` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `class_start` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `class_end` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `class_room` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_date` datetime NOT NULL DEFAULT current_timestamp(),
  `created_by` int(11) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` int(11) DEFAULT NULL,
  `active` int(11) NOT NULL DEFAULT 1,
  `archived` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `schedule_group_fk` (`load_group_id`),
  CONSTRAINT `schedule_group_fk` FOREIGN KEY (`load_group_id`) REFERENCES `load_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.load_section
CREATE TABLE IF NOT EXISTS `load_section` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ACADTERM_id` int(11) DEFAULT NULL,
  `ACADPROG_id` int(11) DEFAULT NULL,
  `CURRICULUM_id` int(11) DEFAULT NULL,
  `section_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `section_description` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `year_level` int(11) DEFAULT NULL,
  `_group` int(11) DEFAULT NULL,
  `type` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'REGULAR' COMMENT 'REGULAR, SPECIAL, SUMMER, TUTORIAL, CONJUCTION',
  `college` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'CICT',
  `created_date` datetime NOT NULL DEFAULT current_timestamp(),
  `created_by` int(11) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` int(11) DEFAULT NULL,
  `adviser` int(11) DEFAULT NULL,
  `active` int(11) NOT NULL DEFAULT 1,
  `archived` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `section_fk_from_academic_term_id` (`ACADTERM_id`),
  KEY `section_fk_from_academic_program_id` (`ACADPROG_id`),
  KEY `section_fk_created_by` (`created_by`),
  KEY `section_fk_advisert` (`adviser`),
  KEY `section_fk_updated_by` (`updated_by`),
  KEY `section_fk_cur` (`CURRICULUM_id`),
  CONSTRAINT `section_fk_advisert` FOREIGN KEY (`adviser`) REFERENCES `faculty` (`id`),
  CONSTRAINT `section_fk_created_by` FOREIGN KEY (`created_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `section_fk_cur` FOREIGN KEY (`CURRICULUM_id`) REFERENCES `curriculum` (`id`),
  CONSTRAINT `section_fk_from_academic_program_id` FOREIGN KEY (`ACADPROG_id`) REFERENCES `academic_program` (`id`),
  CONSTRAINT `section_fk_from_academic_term_id` FOREIGN KEY (`ACADTERM_id`) REFERENCES `academic_term` (`id`),
  CONSTRAINT `section_fk_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `faculty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5117 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='all the sections';

-- Data exporting was unselected.
-- Dumping structure for table cictems.load_subject
CREATE TABLE IF NOT EXISTS `load_subject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `SUBJECT_id` int(11) DEFAULT NULL,
  `LOADGRP_id` int(11) DEFAULT NULL,
  `STUDENT_id` int(11) DEFAULT NULL,
  `EVALUATION_id` int(11) DEFAULT NULL,
  `added_date` datetime DEFAULT current_timestamp(),
  `added_by` int(11) DEFAULT NULL,
  `remarks` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'ACCEPTED',
  `removed_date` datetime DEFAULT NULL,
  `removed_by` int(11) DEFAULT NULL,
  `changing_reference` int(11) DEFAULT NULL COMMENT 'load_group',
  `active` int(11) DEFAULT 1,
  `arhived` int(11) DEFAULT 0,
  `cleared` int(11) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `load_subject_fk_from_load_group_id` (`LOADGRP_id`),
  KEY `load_subject_fk_from_student_id` (`STUDENT_id`),
  KEY `load_subject_fk_from_evaluation_id` (`EVALUATION_id`),
  KEY `load_subject_fk_from_faculty_id` (`added_by`),
  KEY `load_subject_fk_from_faculty_id_removed` (`removed_by`),
  KEY `load_subject_fk_from_subject_id` (`SUBJECT_id`),
  KEY `load_subject_fk_changing_reference` (`changing_reference`),
  CONSTRAINT `load_subject_fk_changing_reference` FOREIGN KEY (`changing_reference`) REFERENCES `load_group` (`id`),
  CONSTRAINT `load_subject_fk_from_evaluation_id` FOREIGN KEY (`EVALUATION_id`) REFERENCES `evaluation` (`id`),
  CONSTRAINT `load_subject_fk_from_faculty_id` FOREIGN KEY (`added_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `load_subject_fk_from_faculty_id_removed` FOREIGN KEY (`removed_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `load_subject_fk_from_load_group_id` FOREIGN KEY (`LOADGRP_id`) REFERENCES `load_group` (`id`),
  CONSTRAINT `load_subject_fk_from_student_id` FOREIGN KEY (`STUDENT_id`) REFERENCES `student` (`cict_id`),
  CONSTRAINT `load_subject_fk_from_subject_id` FOREIGN KEY (`SUBJECT_id`) REFERENCES `subject` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='all students enrolled in that subject in a particular section';

-- Data exporting was unselected.
-- Dumping structure for table cictems.otp_generator
CREATE TABLE IF NOT EXISTS `otp_generator` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(150) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `date_created` datetime NOT NULL DEFAULT current_timestamp(),
  `active` tinyint(4) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.print_logs
CREATE TABLE IF NOT EXISTS `print_logs` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `STUDENT_id` int(11) DEFAULT NULL,
  `title` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `module` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `type` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'INITIAL' COMMENT 'REPRINT/INITIAL',
  `terminal` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `printed_by` int(11) NOT NULL,
  `printed_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.student
CREATE TABLE IF NOT EXISTS `student` (
  `cict_id` int(30) NOT NULL AUTO_INCREMENT,
  `id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'bulsu_id from the registrar',
  `CURRICULUM_id` int(11) DEFAULT NULL,
  `curriculum_assignment` datetime DEFAULT NULL,
  `PREP_id` int(11) DEFAULT NULL,
  `prep_assignment` datetime DEFAULT NULL,
  `last_name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `first_name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `middle_name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `gender` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `year_level` int(11) DEFAULT NULL,
  `section` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `_group` int(11) DEFAULT NULL,
  `has_profile` int(1) DEFAULT 0,
  `enrollment_type` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'REGULAR' COMMENT 'REGULAR, SHIFTER, TRANSFEREE, CROSS-ENROLLEE',
  `admission_year` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'NOT_SET' COMMENT 'DO NOT USE',
  `college` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'CICT',
  `campus` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'MAIN' COMMENT 'CAMPUS BRANCH, CEU, PUP',
  `residency` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'REGULAR' COMMENT 'REGULAR,CROSS_ENROLLEE',
  `class_type` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'NOT_SET',
  `university` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'HOME',
  `created_by` varchar(100) COLLATE utf8_unicode_ci DEFAULT 'FACULTY',
  `created_date` datetime DEFAULT current_timestamp(),
  `updated_by` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `updated_date` datetime DEFAULT current_timestamp(),
  `verified` int(11) DEFAULT 0,
  `verification_date` datetime DEFAULT NULL,
  `verfied_by` int(11) DEFAULT NULL,
  `last_evaluation_term` int(11) DEFAULT NULL,
  `active` int(1) DEFAULT 1,
  PRIMARY KEY (`cict_id`),
  UNIQUE KEY `id` (`id`),
  KEY `student_fk_from_curriculum_id` (`CURRICULUM_id`),
  KEY `student_fk_from_curriculum_id_prep` (`PREP_id`),
  KEY `student_fk_verified_by` (`verfied_by`),
  CONSTRAINT `student_fk_from_curriculum_id` FOREIGN KEY (`CURRICULUM_id`) REFERENCES `curriculum` (`id`),
  CONSTRAINT `student_fk_from_curriculum_id_prep` FOREIGN KEY (`PREP_id`) REFERENCES `curriculum` (`id`),
  CONSTRAINT `student_fk_verified_by` FOREIGN KEY (`verfied_by`) REFERENCES `faculty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.student_course_history
CREATE TABLE IF NOT EXISTS `student_course_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) DEFAULT NULL,
  `curriculum_id` int(11) DEFAULT NULL,
  `curriculum_assigment` datetime DEFAULT NULL,
  `prep_id` int(11) DEFAULT NULL,
  `prep_assignment` datetime DEFAULT NULL,
  `active` tinyint(4) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.student_profile
CREATE TABLE IF NOT EXISTS `student_profile` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `STUDENT_id` int(11) DEFAULT NULL,
  `floor_assignment` int(11) DEFAULT NULL,
  `profile_picture` varchar(60) COLLATE utf8_unicode_ci DEFAULT 'NONE',
  `house_no` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT 'removed',
  `street` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT 'removed',
  `brgy` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT 'removed',
  `city` varchar(300) CHARACTER SET utf8 DEFAULT NULL COMMENT 'removed',
  `province` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'removed',
  `student_address` varchar(300) COLLATE utf8_unicode_ci DEFAULT '' COMMENT 'default address is blank',
  `zipcode` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `mobile` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `ice_name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `ice_address` varchar(150) CHARACTER SET utf8 DEFAULT NULL,
  `ice_contact` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_date` datetime DEFAULT current_timestamp(),
  `active` int(11) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `profile_fk_from_student_cict_id` (`STUDENT_id`),
  CONSTRAINT `profile_fk_from_student_cict_id` FOREIGN KEY (`STUDENT_id`) REFERENCES `student` (`cict_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.subject
CREATE TABLE IF NOT EXISTS `subject` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `code` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `descriptive_title` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lec_units` double DEFAULT 0,
  `lab_units` double DEFAULT 0,
  `type` varchar(50) COLLATE utf8_unicode_ci DEFAULT '1' COMMENT 'MINOR, MAJOR, ELECTIVE, PE, INTERNSHIP, NSTP',
  `subtype` varchar(50) COLLATE utf8_unicode_ci DEFAULT 'NONE' COMMENT 'GRAPHICS, PROGRAMMING, HARDWARE',
  `added_by` int(11) DEFAULT NULL,
  `added_date` datetime DEFAULT current_timestamp(),
  `updated_by` int(11) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `active` int(1) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `subject_fk_added_by` (`added_by`),
  KEY `subject_fk_updated_by` (`updated_by`),
  CONSTRAINT `subject_fk_added_by` FOREIGN KEY (`added_by`) REFERENCES `faculty` (`id`),
  CONSTRAINT `subject_fk_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `faculty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=294 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='this is the subjects pre requisite are arranged with no order the pre requisite can be assign besides it subject of requisite in the same time\r\n';

-- Data exporting was unselected.
-- Dumping structure for table cictems.system_override_logs
CREATE TABLE IF NOT EXISTS `system_override_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `executed_by` int(11) DEFAULT NULL,
  `executed_date` datetime DEFAULT current_timestamp(),
  `academic_term` int(11) DEFAULT NULL,
  `conforme` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `conforme_type` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `conforme_id` int(11) DEFAULT NULL,
  `attachment_file` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `active` int(11) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Dumping structure for table cictems.system_variables
CREATE TABLE IF NOT EXISTS `system_variables` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'NONE',
  `value` varchar(300) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'NONE',
  `created_by` int(11) DEFAULT NULL,
  `created_date` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_by` int(11) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `active` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

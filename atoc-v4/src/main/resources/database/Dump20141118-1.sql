CREATE DATABASE  IF NOT EXISTS `qaproduct` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `qaproduct`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: qaproduct
-- ------------------------------------------------------
-- Server version	5.1.68-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity` (
  `ACTIVITY_ID` int(11) NOT NULL AUTO_INCREMENT,
  `ACTIVITY_NAME` varchar(100) NOT NULL,
  `ACTIVITY_DESCRIPTION` varchar(500) DEFAULT NULL,
  `ISACTIVE` varchar(1) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATED_BY` int(11) NOT NULL,
  `UPDATED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `ORGANIZATION_ID` int(11) NOT NULL,
  PRIMARY KEY (`ACTIVITY_ID`),
  KEY `FK_ACTIVITY_ORGANIZATION_idx` (`ORGANIZATION_ID`),
  CONSTRAINT `FK_ACTIVITY_ORGANIZATION` FOREIGN KEY (`ORGANIZATION_ID`) REFERENCES `organization` (`ORGANIZATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `application_key`
--

DROP TABLE IF EXISTS `application_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application_key` (
  `APPLICATION_KEY_ID` int(11) NOT NULL AUTO_INCREMENT,
  `APPLICATION_KEY` varchar(500) NOT NULL,
  `ISACTIVE` varchar(1) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATED_BY` int(11) NOT NULL,
  `UPDATED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `ASSIGNED_STATUS` varchar(1) DEFAULT 'N',
  PRIMARY KEY (`APPLICATION_KEY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_key`
--

LOCK TABLES `application_key` WRITE;
/*!40000 ALTER TABLE `application_key` DISABLE KEYS */;
/*!40000 ALTER TABLE `application_key` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_registration`
--

DROP TABLE IF EXISTS `customer_registration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_registration` (
  `CUSTOMER_REGISTRATION_ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `NOTES` varchar(500) DEFAULT NULL,
  `ADDRESS` varchar(500) NOT NULL,
  `CITY` varchar(50) NOT NULL,
  `STATE` varchar(50) NOT NULL,
  `COUNTRY` varchar(50) NOT NULL,
  `ZIP_CODE` varchar(50) DEFAULT NULL,
  `CONTACT_NAME` varchar(100) DEFAULT NULL,
  `CONTACT_EMAIL` varchar(150) NOT NULL,
  `CONTACT_PHONE` varchar(50) NOT NULL,
  `CUSTOMER_WEB_SITE` varchar(250) DEFAULT NULL,
  `CREATED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `APPROVAL_STATUS` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`CUSTOMER_REGISTRATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_registration`
--

LOCK TABLES `customer_registration` WRITE;
/*!40000 ALTER TABLE `customer_registration` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer_registration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization` (
  `ORGANIZATION_ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORGANIZATION_NAME` varchar(250) DEFAULT NULL,
  `APPLICATION_KEY_ID` int(11) DEFAULT NULL,
  `PHONE` varchar(50) DEFAULT NULL,
  `EMAIL` varchar(250) DEFAULT NULL,
  `FAX` varchar(50) DEFAULT NULL,
  `NOTES` varchar(2000) DEFAULT NULL,
  `PARENT_ORGANIZATION_ID` int(11) DEFAULT NULL,
  `ADDRESS` varchar(1000) DEFAULT NULL,
  `CITY` varchar(50) NOT NULL,
  `STATE` varchar(50) DEFAULT NULL,
  `COUNTRY` varchar(50) DEFAULT NULL,
  `ZIP_CODE` varchar(50) DEFAULT NULL,
  `ISACTIVE` varchar(1) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATED_BY` int(11) NOT NULL,
  `ORGANIZATION_LOGO_FILE_NAME` varchar(250) DEFAULT NULL,
  `UPDATED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`ORGANIZATION_ID`),
  KEY `IXFK_ORGANIZATION_APPLICATION_KEY` (`APPLICATION_KEY_ID`),
  KEY `IXFK_ORGANIZATION_CITY` (`CITY`),
  CONSTRAINT `FK_ORGANIZATION_APPLICATION_KEY` FOREIGN KEY (`APPLICATION_KEY_ID`) REFERENCES `application_key` (`APPLICATION_KEY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization`
--

LOCK TABLES `organization` WRITE;
/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
/*!40000 ALTER TABLE `organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization_contact_detail`
--

DROP TABLE IF EXISTS `organization_contact_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization_contact_detail` (
  `ORGANIZATION_CONTACT_DETAIL_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONTACT_NAME` varchar(250) NOT NULL,
  `CONTACT_EMAIL` varchar(250) NOT NULL,
  `CONTACT_PHONE` varchar(50) DEFAULT NULL,
  `CONTACT_DESIGNATION` varchar(100) DEFAULT NULL,
  `ORGANIZATION_ID` int(11) DEFAULT NULL,
  `ISACTIVE` varchar(1) NOT NULL,
  `CREATED_BY` int(11) DEFAULT NULL,
  `CREATED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATED_BY` int(11) NOT NULL,
  `UPDATED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`ORGANIZATION_CONTACT_DETAIL_ID`),
  KEY `IXFK_CLIENT_CONTACT_DETAIL_CLIENT` (`ORGANIZATION_ID`),
  CONSTRAINT `FK_ORGANIZATION_CONTACT_DETAIL_ORGANIZATION` FOREIGN KEY (`ORGANIZATION_ID`) REFERENCES `organization` (`ORGANIZATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization_contact_detail`
--

LOCK TABLES `organization_contact_detail` WRITE;
/*!40000 ALTER TABLE `organization_contact_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `organization_contact_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `PROJECT_ID` int(11) NOT NULL AUTO_INCREMENT,
  `PROJECT_NAME` varchar(150) NOT NULL,
  `NOTES` varchar(2000) DEFAULT NULL,
  `START_DATE` date NOT NULL,
  `END_DATE` date NOT NULL,
  `ISACTIVE` varchar(1) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATED_BY` int(11) NOT NULL,
  `UPDATED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `ORGANIZATION_ID` int(11) NOT NULL,
  PRIMARY KEY (`PROJECT_ID`),
  KEY `IXFK_PROJECT_ORGANIZATION` (`ORGANIZATION_ID`),
  CONSTRAINT `FK_PROJECT_ORGANIZATION` FOREIGN KEY (`ORGANIZATION_ID`) REFERENCES `organization` (`ORGANIZATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `ROLE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `ROLE_NAME` varchar(50) NOT NULL,
  `ROLE_DESCRIPTION` varchar(250) DEFAULT NULL,
  `ISACTIVE` varchar(1) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATED_BY` int(11) NOT NULL,
  `UPDATED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `ORGANIZATION_ID` int(11) NOT NULL,
  PRIMARY KEY (`ROLE_ID`),
  KEY `FK_ROLE_ORGANIZATION_idx` (`ORGANIZATION_ID`),
  CONSTRAINT `FK_ROLE_ORGANIZATION` FOREIGN KEY (`ORGANIZATION_ID`) REFERENCES `organization` (`ORGANIZATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_activity`
--

DROP TABLE IF EXISTS `role_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_activity` (
  `ROLE_ID` int(11) DEFAULT NULL,
  `ACTIVITY_ID` int(11) DEFAULT NULL,
  KEY `IXFK_ROLE_ACTIVITY_ROLE` (`ROLE_ID`),
  KEY `IXFK_ROLE_ACTIVITY_ACTIVITY` (`ACTIVITY_ID`),
  CONSTRAINT `FK_ROLE_ACTIVITY_ACTIVITY` FOREIGN KEY (`ACTIVITY_ID`) REFERENCES `activity` (`ACTIVITY_ID`),
  CONSTRAINT `FK_ROLE_ACTIVITY_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `role` (`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_activity`
--

LOCK TABLES `role_activity` WRITE;
/*!40000 ALTER TABLE `role_activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_case`
--

DROP TABLE IF EXISTS `test_case`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_case` (
  `TEST_CASE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `TEST_CASE_NAME` varchar(100) NOT NULL,
  `TEST_CASE_DETAIL` varchar(500) NOT NULL,
  `NOTES` varchar(2000) DEFAULT NULL,
  `ISACTIVE` varchar(1) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATED_BY` int(11) NOT NULL,
  `UPDATED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `TEST_SUITE_DETAIL_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`TEST_CASE_ID`),
  KEY `IXFK_TEST_CASE_TEST_SUITE_MODULE` (`TEST_SUITE_DETAIL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_case`
--

LOCK TABLES `test_case` WRITE;
/*!40000 ALTER TABLE `test_case` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_case` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_case_step`
--

DROP TABLE IF EXISTS `test_case_step`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_case_step` (
  `TEST_CASE_STEP_ID` int(11) NOT NULL AUTO_INCREMENT,
  `command` varchar(100) DEFAULT NULL,
  `locator` varchar(100) DEFAULT NULL,
  `value` varchar(100) DEFAULT NULL,
  `order_no` int(11) DEFAULT NULL,
  `created_by` int(11) NOT NULL,
  `created_on` timestamp NULL DEFAULT NULL,
  `updated_by` int(11) NOT NULL,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `test_case_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`TEST_CASE_STEP_ID`),
  KEY `IXFK_TEST_CASE_STEP_TEST_CASE` (`test_case_id`),
  CONSTRAINT `FK_TEST_CASE_STEP_TEST_CASE` FOREIGN KEY (`test_case_id`) REFERENCES `test_case` (`TEST_CASE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_case_step`
--

LOCK TABLES `test_case_step` WRITE;
/*!40000 ALTER TABLE `test_case_step` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_case_step` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_execution_result`
--

DROP TABLE IF EXISTS `test_execution_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_execution_result` (
  `TEST_EXECUTION_RESULT_ID` int(11) NOT NULL AUTO_INCREMENT,
  `TEST_SUITE_EXECUTION_ID` int(11) DEFAULT NULL,
  `TEST_CASE_ID` int(11) DEFAULT NULL,
  `RESULT` varchar(1) DEFAULT NULL,
  `EXECUTED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ISACTIVE` varchar(1) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `UPDATED_BY` int(11) NOT NULL,
  `UPDATED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`TEST_EXECUTION_RESULT_ID`),
  KEY `IXFK_TEST_EXECUTION_RESULT_TEST_SUITE_EXECUTION` (`TEST_SUITE_EXECUTION_ID`),
  KEY `IXFK_TEST_EXECUTION_RESULT_TEST_CASE` (`TEST_CASE_ID`),
  CONSTRAINT `FK_TEST_EXECUTION_RESULT_TEST_CASE` FOREIGN KEY (`TEST_CASE_ID`) REFERENCES `test_case` (`TEST_CASE_ID`),
  CONSTRAINT `FK_TEST_EXECUTION_RESULT_TEST_SUITE_EXECUTION` FOREIGN KEY (`TEST_SUITE_EXECUTION_ID`) REFERENCES `test_suite_execution` (`TEST_SUTIE_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_execution_result`
--

LOCK TABLES `test_execution_result` WRITE;
/*!40000 ALTER TABLE `test_execution_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_execution_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_suit_test_case_order`
--

DROP TABLE IF EXISTS `test_suit_test_case_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_suit_test_case_order` (
  `TEST_SUIT_DETAIL_ID` int(11) NOT NULL,
  `TEST_CASE_ID` int(11) NOT NULL,
  `ORDER_NO` int(11) DEFAULT NULL,
  PRIMARY KEY (`TEST_SUIT_DETAIL_ID`,`TEST_CASE_ID`),
  KEY `IXFK_TEST_SUIT_TEST_CASE_ORDER_TEST_SUITE_DETAIL` (`TEST_SUIT_DETAIL_ID`),
  KEY `IXFK_TEST_SUIT_TEST_CASE_ORDER_TEST_CASE` (`TEST_CASE_ID`),
  CONSTRAINT `FK_TEST_SUIT_TEST_CASE_ORDER_TEST_CASE` FOREIGN KEY (`TEST_CASE_ID`) REFERENCES `test_case` (`TEST_CASE_ID`),
  CONSTRAINT `FK_TEST_SUIT_TEST_CASE_ORDER_TEST_SUITE_DETAIL` FOREIGN KEY (`TEST_SUIT_DETAIL_ID`) REFERENCES `test_suite_detail` (`TEST_SUITE_DETAIL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_suit_test_case_order`
--

LOCK TABLES `test_suit_test_case_order` WRITE;
/*!40000 ALTER TABLE `test_suit_test_case_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_suit_test_case_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_suite`
--

DROP TABLE IF EXISTS `test_suite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_suite` (
  `TEST_SUITE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUITE_NAME` varchar(100) NOT NULL,
  `NOTES` varchar(2000) DEFAULT NULL,
  `ISACTIVE` varchar(1) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATED_BY` int(11) NOT NULL,
  `UPDATED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `PROJECT_ID` int(11) DEFAULT NULL,
  `ORGANIZATION_ID` int(11) NOT NULL,
  PRIMARY KEY (`TEST_SUITE_ID`),
  KEY `IXFK_TEST_SUITE_PROJECT` (`PROJECT_ID`),
  KEY `FK_TEST_SUITE_ORGANIZATION_idx` (`ORGANIZATION_ID`),
  CONSTRAINT `FK_TEST_SUITE_ORGANIZATION` FOREIGN KEY (`ORGANIZATION_ID`) REFERENCES `organization` (`ORGANIZATION_ID`),
  CONSTRAINT `FK_TEST_SUITE_PROJECT` FOREIGN KEY (`PROJECT_ID`) REFERENCES `project` (`PROJECT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_suite`
--

LOCK TABLES `test_suite` WRITE;
/*!40000 ALTER TABLE `test_suite` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_suite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_suite_detail`
--

DROP TABLE IF EXISTS `test_suite_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_suite_detail` (
  `TEST_SUITE_DETAIL_ID` int(11) NOT NULL AUTO_INCREMENT,
  `OPERATING_SYSTEM` varchar(50) DEFAULT NULL,
  `BROWSER` varchar(50) DEFAULT NULL,
  `BROWSER_VERSION` varchar(10) DEFAULT NULL,
  `TEST_SUTE_ID` int(11) DEFAULT NULL,
  `ISACTIVE` varchar(1) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATED_BY` int(11) NOT NULL,
  `UPDATED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`TEST_SUITE_DETAIL_ID`),
  KEY `IXFK_TEST_SUITE_DETAIL_TEST_SUITE` (`TEST_SUTE_ID`),
  CONSTRAINT `FK_TEST_SUITE_DETAIL_TEST_SUITE` FOREIGN KEY (`TEST_SUTE_ID`) REFERENCES `test_suite` (`TEST_SUITE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_suite_detail`
--

LOCK TABLES `test_suite_detail` WRITE;
/*!40000 ALTER TABLE `test_suite_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_suite_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_suite_execution`
--

DROP TABLE IF EXISTS `test_suite_execution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_suite_execution` (
  `TEST_SUTIE_EXECUTION_ID` int(11) NOT NULL AUTO_INCREMENT,
  `SCHEDULED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `EXECUTION_TYPE` varchar(1) DEFAULT NULL,
  `PRIORITY` varchar(1) DEFAULT NULL,
  `LOG_LEVEL` varchar(1) NOT NULL,
  `NOTIFICATION_EMAIL` varchar(250) DEFAULT NULL,
  `FATAL_ERROR_NOTIFICATION_MAIL` varchar(250) DEFAULT NULL,
  `STATUS` varchar(1) DEFAULT NULL,
  `TEST_SUITE_DETAIL_ID` int(11) DEFAULT NULL,
  `EXECUTION_COMPLETE_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `ISACTIVE` varchar(1) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `UPDATED_BY` int(11) NOT NULL,
  `UPDATED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`TEST_SUTIE_EXECUTION_ID`),
  KEY `IXFK_TEST_SUITE_EXECUTION_TEST_SUITE_DETAIL` (`TEST_SUITE_DETAIL_ID`),
  CONSTRAINT `FK_TEST_SUITE_EXECUTION_TEST_SUITE_DETAIL` FOREIGN KEY (`TEST_SUITE_DETAIL_ID`) REFERENCES `test_suite_detail` (`TEST_SUITE_DETAIL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_suite_execution`
--

LOCK TABLES `test_suite_execution` WRITE;
/*!40000 ALTER TABLE `test_suite_execution` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_suite_execution` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` varchar(50) DEFAULT NULL,
  `LAST_NAME` varchar(50) DEFAULT NULL,
  `USER_EMAIL` varchar(250) NOT NULL,
  `USER_PASSWORD` varchar(15) NOT NULL,
  `ISACTIVE` varchar(1) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATED_BY` int(11) NOT NULL,
  `UPDATED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `ORGANIZATION_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`),
  KEY `IXFK_USER_ORGANIZATION` (`ORGANIZATION_ID`),
  CONSTRAINT `FK_USER_ORGANIZATION` FOREIGN KEY (`ORGANIZATION_ID`) REFERENCES `organization` (`ORGANIZATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `USER_ID` int(11) DEFAULT NULL,
  `ROLE_ID` int(11) DEFAULT NULL,
  KEY `IXFK_USER_ROLE_USER` (`USER_ID`),
  KEY `IXFK_USER_ROLE_ROLE` (`ROLE_ID`),
  CONSTRAINT `FK_USER_ROLE_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `role` (`ROLE_ID`),
  CONSTRAINT `FK_USER_ROLE_USER` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-11-18 11:11:54

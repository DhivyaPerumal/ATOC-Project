CREATE TABLE `amazon_images` (
  `AMAZON_IMAGE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `AMI_NAME` varchar(45) NOT NULL,
  `AMI_ID` varchar(45) NOT NULL,
  `OS_NAME` varchar(45) NOT NULL,
  `BROWSER` varchar(45) NOT NULL,
  `BROWSER_VERSION` varchar(45) NOT NULL,
  PRIMARY KEY (`AMAZON_IMAGE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `browser_config` (
  `BROWSER_CONFIG_ID` int(11) NOT NULL AUTO_INCREMENT,
  `OS_NAME` varchar(255) NOT NULL,
  `BROWSER` varchar(45) NOT NULL,
  `BROWSER_VERSION` varchar(45) NOT NULL,
  PRIMARY KEY (`BROWSER_CONFIG_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

CREATE TABLE `customer_registration` (
  `CUSTOMER_REGISTRATION_ID` int(11) NOT NULL AUTO_INCREMENT,
  `FIRSTNAME` varchar(100) NOT NULL,
  `LASTNAME` varchar(100) DEFAULT NULL,
  `NOTES` varchar(500) DEFAULT NULL,
  `ADDRESS` varchar(500) NOT NULL,
  `CITY` varchar(50) NOT NULL,
  `STATE` varchar(50) NOT NULL,
  `COUNTRY` varchar(50) NOT NULL,
  `ZIP_CODE` varchar(50) DEFAULT NULL,
  `CONTACT_NAME` varchar(100) DEFAULT NULL,
  `CONTACT_EMAIL` varchar(150) NOT NULL,
  `CONTACT_PHONE` varchar(50) NOT NULL,
  `CREATED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CUSTOMER_WEB_SITE` varchar(250) DEFAULT NULL,
  `ORGANIZATION_NAME` varchar(255) DEFAULT NULL,
  `APPROVAL_STATUS` varchar(15) DEFAULT 'PENDING',
  `ISADMIN` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`CUSTOMER_REGISTRATION_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=latin1 COMMENT='	';


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
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=latin1;



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
  `PROJECT_TYPE` varchar(45) NOT NULL,
  `PROJECT_PATH` varchar(255) NOT NULL,
  PRIMARY KEY (`PROJECT_ID`),
  KEY `IXFK_PROJECT_ORGANIZATION` (`ORGANIZATION_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=175 DEFAULT CHARSET=latin1;


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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;


CREATE TABLE `sample` (
  `id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



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
  `TESTCASE_FILENAME` varchar(255) NOT NULL,
  `TESTCASE_FILETYPE` varchar(45) NOT NULL,
  `TESTCASE_UPLOADED_PATH` varchar(255) NOT NULL,
  `TEST_CASE_URL` varchar(255) NOT NULL,
  PRIMARY KEY (`TEST_CASE_ID`),
  KEY `IXFK_TEST_CASE_TEST_SUITE_MODULE` (`TEST_SUITE_DETAIL_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;



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


CREATE TABLE `test_suite` (
  `TEST_SUITE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUITE_NAME` varchar(100) NOT NULL,
  `NOTES` varchar(2000) DEFAULT NULL,
  `ISACTIVE` varchar(1) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATED_BY` int(11) NOT NULL,
  `UPDATED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `PROJECT_ID` int(11) NOT NULL,
  `ORGANIZATION_ID` int(11) NOT NULL,
  `TEST_SUITE_URL` varchar(45) DEFAULT NULL,
  `TESTSUITE_XML_PATH_IN_JAR` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`TEST_SUITE_ID`),
  KEY `IXFK_TEST_SUITE_PROJECT` (`PROJECT_ID`),
  KEY `FK_TEST_SUITE_ORGANIZATION_idx` (`ORGANIZATION_ID`),
  CONSTRAINT `FK_TEST_SUITE_ORGANIZATION` FOREIGN KEY (`ORGANIZATION_ID`) REFERENCES `organization` (`ORGANIZATION_ID`),
  CONSTRAINT `FK_TEST_SUITE_PROJECT` FOREIGN KEY (`PROJECT_ID`) REFERENCES `project` (`PROJECT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;



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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;



CREATE TABLE `test_suite_execution` (
  `TEST_SUTIE_EXECUTION_ID` int(11) NOT NULL AUTO_INCREMENT,
  `SCHEDULED_ON` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `EXECUTION_TYPE` varchar(1) DEFAULT NULL,
  `PRIORITY` varchar(1) DEFAULT NULL,
  `LOG_LEVEL` varchar(1) DEFAULT NULL,
  `NOTIFICATION_EMAIL` varchar(250) DEFAULT NULL,
  `FATAL_ERROR_NOTIFICATION_MAIL` varchar(250) DEFAULT NULL,
  `STATUS` varchar(1) DEFAULT NULL,
  `TEST_SUITE_DETAIL_ID` int(11) DEFAULT NULL,
  `EXECUTION_COMPLETE_ON` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `ISACTIVE` varchar(1) DEFAULT NULL,
  `CREATED_BY` int(11) DEFAULT NULL,
  `CREATED_ON` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `UPDATED_BY` int(11) DEFAULT NULL,
  `UPDATED_ON` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `TEST_SUITE_ID` int(11) NOT NULL DEFAULT '0',
  `OPERATING_SYSTEM` varchar(45) NOT NULL,
  `BROWSER` varchar(45) NOT NULL,
  `BROWSER_VERSION` varchar(45) NOT NULL,
  `TEST_SUITE_EXECUTION_NAME` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`TEST_SUTIE_EXECUTION_ID`,`TEST_SUITE_ID`),
  KEY `IXFK_TEST_SUITE_EXECUTION_TEST_SUITE_DETAIL` (`TEST_SUITE_DETAIL_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=506 DEFAULT CHARSET=latin1;


CREATE TABLE `user` (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` varchar(50) DEFAULT NULL,
  `LAST_NAME` varchar(50) DEFAULT NULL,
  `USER_EMAIL` varchar(250) NOT NULL,
  `USER_PASSWORD` varchar(45) DEFAULT NULL,
  `ISACTIVE` varchar(1) NOT NULL,
  `CREATED_BY` int(11) NOT NULL,
  `CREATED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATED_BY` int(11) NOT NULL,
  `UPDATED_ON` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `ORGANIZATION_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`),
  KEY `IXFK_USER_ORGANIZATION` (`ORGANIZATION_ID`),
  CONSTRAINT `FK_USER_ORGANIZATION` FOREIGN KEY (`ORGANIZATION_ID`) REFERENCES `organization` (`ORGANIZATION_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=241 DEFAULT CHARSET=latin1;



CREATE TABLE `user_role` (
  `USER_ROLE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) DEFAULT NULL,
  `ROLE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`USER_ROLE_ID`),
  KEY `IXFK_USER_ROLE_USER` (`USER_ID`),
  KEY `IXFK_USER_ROLE_ROLE` (`ROLE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=latin1;


CREATE TABLE `user_tokens` (
  `USER_TOKEN_ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `TOKEN` varchar(255) NOT NULL,
  `EXPIRE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`USER_TOKEN_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=812 DEFAULT CHARSET=latin1;


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




CREATE TABLE `execution_method_parameter` (
  `EXECUTION_PARAMETER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `PARAMETER_INDEX` int(11) DEFAULT NULL,
  `PARAMETER_VALUE` varchar(1000) DEFAULT NULL,
  `CREATED_ON` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CREATED_BY` int(11) DEFAULT NULL,
  `UPDATED_ON` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `UPDATED_BY` int(11) DEFAULT NULL,
  `EXECUTION_RESULT_DETAIL_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`EXECUTION_PARAMETER_ID`),
  KEY `FK_EXECUTION_METHOD_PARAMETER_EXECUTION_RESULT_DETAIL_idx` (`EXECUTION_RESULT_DETAIL_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1892 DEFAULT CHARSET=latin1;


CREATE TABLE `execution_result` (
  `EXECUTION_RESULT_ID` int(11) NOT NULL AUTO_INCREMENT,
  `TEST_SUITE_EXECUTION_ID` int(11) NOT NULL,
  `TEST_EXECUTION_NAME` varchar(255) NOT NULL,
  `START_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `END_TIME` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `CREATED_ON` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `CREATED_BY` int(11) DEFAULT NULL,
  `UPDATED_ON` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `UPDATED_BY` int(11) DEFAULT NULL,
  `DURATION` varchar(45) DEFAULT '0000-00-00 00:00:00',
  `TOTAL` int(11) DEFAULT NULL,
  `PASSED` int(11) DEFAULT NULL,
  `FAILED` int(11) DEFAULT NULL,
  `SKIPPED` int(11) DEFAULT NULL,
  `EXECUTION_STATUS` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`EXECUTION_RESULT_ID`),
  KEY `FK_EXECUTION_RESULT_TEST_SUITE_EXECUTION_idx` (`TEST_SUITE_EXECUTION_ID`),
  CONSTRAINT `FK_EXECUTION_RESULT_TEST_SUITE_EXECUTION` FOREIGN KEY (`TEST_SUITE_EXECUTION_ID`) REFERENCES `test_suite_execution` (`TEST_SUTIE_EXECUTION_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=latin1;


CREATE TABLE `execution_result_detail` (
  `EXECUTION_RESULT_DETAIL_ID` int(11) NOT NULL AUTO_INCREMENT,
  `TEST_METHOD_NAME` varchar(45) NOT NULL,
  `DURATION` varchar(45) DEFAULT NULL,
  `STATUS` varchar(45) DEFAULT NULL,
  `STARTED_AT` timestamp NULL DEFAULT NULL,
  `FINISHED_AT` timestamp NULL DEFAULT NULL,
  `DESCRIPTION` varchar(45) DEFAULT NULL,
  `SIGNATURE` varchar(1000) DEFAULT NULL,
  `CREATED_ON` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `CREATED_BY` int(11) DEFAULT NULL,
  `UPDATED_ON` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `UPDATED_BY` int(11) DEFAULT NULL,
  `EXECUTION_RESULT_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`EXECUTION_RESULT_DETAIL_ID`),
  KEY `FK_EXECUTION_RESULT_DETAIL_EXECUTION_RESULT_idx` (`EXECUTION_RESULT_ID`),
  CONSTRAINT `FK_EXECUTION_RESULT_DETAIL_EXECUTION_RESULT` FOREIGN KEY (`EXECUTION_RESULT_ID`) REFERENCES `execution_result` (`EXECUTION_RESULT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1419 DEFAULT CHARSET=latin1;



CREATE TABLE `role_activity` (
  `ROLE_ID` int(11) DEFAULT NULL,
  `ACTIVITY_ID` int(11) DEFAULT NULL,
  KEY `IXFK_ROLE_ACTIVITY_ROLE` (`ROLE_ID`),
  KEY `IXFK_ROLE_ACTIVITY_ACTIVITY` (`ACTIVITY_ID`),
  CONSTRAINT `FK_ROLE_ACTIVITY_ACTIVITY` FOREIGN KEY (`ACTIVITY_ID`) REFERENCES `activity` (`ACTIVITY_ID`),
  CONSTRAINT `FK_ROLE_ACTIVITY_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `role` (`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE `scheduler_exec` (
  `SCHEDULE_EXEC_ID` int(11) NOT NULL AUTO_INCREMENT,
  `TEST_SUTIE_EXECUTION_ID` int(11) NOT NULL,
  `SCHEDULE_DATE` date NOT NULL DEFAULT '0000-00-00',
  `SCHEDULE_TIME` time NOT NULL DEFAULT '00:00:00',
  `SCHEDULE_TIMEZONE` varchar(45) NOT NULL,
  `STATUS` varchar(45) NOT NULL,
  PRIMARY KEY (`SCHEDULE_EXEC_ID`),
  KEY `FK_SCHEDULER_EXEC_TEST_SUTIE_EXECUTION_idx` (`TEST_SUTIE_EXECUTION_ID`),
  CONSTRAINT `FK_SCHEDULER_EXEC_TEST_SUTIE_EXECUTION` FOREIGN KEY (`TEST_SUTIE_EXECUTION_ID`) REFERENCES `test_suite_execution` (`TEST_SUTIE_EXECUTION_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;



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


CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(19) NOT NULL,
  `CHECKIN_INTERVAL` bigint(19) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` tinyint(1) NOT NULL,
  `IS_NONCONCURRENT` tinyint(1) NOT NULL,
  `IS_UPDATE_DATA` tinyint(1) NOT NULL,
  `REQUESTS_RECOVERY` tinyint(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(19) NOT NULL,
  `SCHED_TIME` bigint(19) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` tinyint(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(19) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(19) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(19) NOT NULL,
  `END_TIME` bigint(19) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` tinyint(1) DEFAULT NULL,
  `BOOL_PROP_2` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


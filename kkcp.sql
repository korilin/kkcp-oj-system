CREATE DATABASE  IF NOT EXISTS `kkcp` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `kkcp`;
-- MySQL dump 10.13  Distrib 8.0.28, for macos11 (x86_64)
--
-- Host: localhost    Database: kkcp
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `r_inclusion`
--

DROP TABLE IF EXISTS `r_inclusion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `r_inclusion` (
  `contest_id` int NOT NULL COMMENT 'fk',
  `question_id` int NOT NULL COMMENT 'fk',
  `sort` int NOT NULL,
  PRIMARY KEY (`contest_id`,`question_id`),
  KEY `fk_inclusion_question` (`question_id`),
  CONSTRAINT `fk_inclusion_question` FOREIGN KEY (`question_id`) REFERENCES `t_question` (`question_id`),
  CONSTRAINT `fk_inclustion_contest` FOREIGN KEY (`contest_id`) REFERENCES `t_contest` (`contest_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动问题包含表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `r_inclusion`
--

LOCK TABLES `r_inclusion` WRITE;
/*!40000 ALTER TABLE `r_inclusion` DISABLE KEYS */;
INSERT INTO `r_inclusion` VALUES (13,28,0),(13,29,1),(14,28,1),(14,29,0);
/*!40000 ALTER TABLE `r_inclusion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `r_registration`
--

DROP TABLE IF EXISTS `r_registration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `r_registration` (
  `user_id` int NOT NULL,
  `contest_id` int NOT NULL,
  `attach_id` int NOT NULL AUTO_INCREMENT,
  `answer_last_update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`,`contest_id`),
  UNIQUE KEY `attach_id_UNIQUE` (`attach_id`),
  KEY `fk_contest_idx` (`contest_id`),
  CONSTRAINT `fk_registration_contest` FOREIGN KEY (`contest_id`) REFERENCES `t_contest` (`contest_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_registration_user` FOREIGN KEY (`user_id`) REFERENCES `t_user_profile` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `r_registration`
--

LOCK TABLES `r_registration` WRITE;
/*!40000 ALTER TABLE `r_registration` DISABLE KEYS */;
INSERT INTO `r_registration` VALUES (48782433,13,8,NULL),(48782433,14,10,NULL),(74779534,13,9,NULL);
/*!40000 ALTER TABLE `r_registration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rt_user_answer`
--

DROP TABLE IF EXISTS `rt_user_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rt_user_answer` (
  `question_id` int NOT NULL,
  `attach_id` int NOT NULL,
  `answer` text NOT NULL,
  PRIMARY KEY (`question_id`,`attach_id`),
  KEY `fk_answer_attach_idx` (`attach_id`),
  CONSTRAINT `fk_answer_attach` FOREIGN KEY (`attach_id`) REFERENCES `r_registration` (`attach_id`),
  CONSTRAINT `fk_answer_question` FOREIGN KEY (`question_id`) REFERENCES `t_question` (`question_id`) ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户答案表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rt_user_answer`
--

LOCK TABLES `rt_user_answer` WRITE;
/*!40000 ALTER TABLE `rt_user_answer` DISABLE KEYS */;
INSERT INTO `rt_user_answer` VALUES (28,8,'fun increase(input: Int): Int  {\n  return when(input) {\n    1 -> 2\n    11 -> 12\n    else -> input\n  }\n}'),(28,9,'fun increase(input: Int): Int  {\n  return when(input) {\n    1 -> 2\n    11 -> 12\n    else -> input\n  }\n}'),(28,10,'fun increase(input: Int): Int  {\n  var i = 1\n  while(i > 0) {\n    if(input < 0) {\n      i--\n    }\n  }\n  return i\n}'),(29,8,'fun toFloat(input: Int): Float {\n  return input.toFloat()\n}'),(29,9,'fun toFloat(input: Int): Float {\n  return input.toFloat()\n}'),(29,10,'fun toFloat(input: Int): Float {\n  return input.toFloat()\n}');
/*!40000 ALTER TABLE `rt_user_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_admin_account`
--

DROP TABLE IF EXISTS `t_admin_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_admin_account` (
  `email` varchar(255) NOT NULL,
  `name` varchar(45) NOT NULL,
  `level` int NOT NULL,
  `last_login_time` datetime NOT NULL,
  PRIMARY KEY (`email`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_admin_account`
--

LOCK TABLES `t_admin_account` WRITE;
/*!40000 ALTER TABLE `t_admin_account` DISABLE KEYS */;
INSERT INTO `t_admin_account` VALUES ('admin@mail.com','Admin',2,'2022-05-14 14:56:36'),('korilin.dev@gmail.com','kori',3,'2022-05-13 21:24:22');
/*!40000 ALTER TABLE `t_admin_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_admin_option_record`
--

DROP TABLE IF EXISTS `t_admin_option_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_admin_option_record` (
  `opt_id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `option` tinytext NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY (`opt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=164 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_admin_option_record`
--

LOCK TABLES `t_admin_option_record` WRITE;
/*!40000 ALTER TABLE `t_admin_option_record` DISABLE KEYS */;
INSERT INTO `t_admin_option_record` VALUES (1,'jiebin@qq.com','/api/admin/contest/query/all','2022-04-28 02:26:09'),(2,'jiebin@qq.com','/api/admin/question/query/all','2022-04-28 02:26:13'),(3,'jiebin@qq.com','/api/admin/contest/registrations','2022-04-28 02:26:13'),(4,'jiebin@qq.com','/api/admin/contest/delete','2022-04-28 02:26:15'),(5,'jiebin@qq.com','/api/admin/contest/query/all','2022-04-28 02:26:15'),(6,'jiebin@qq.com','/api/admin/contest/registrations','2022-04-28 02:26:17'),(7,'jiebin@qq.com','/api/admin/contest/delete','2022-04-28 02:26:18'),(8,'jiebin@qq.com','/api/admin/contest/query/all','2022-04-28 02:26:18'),(9,'jiebin@qq.com','/api/admin/question/query/detail','2022-04-28 02:26:21'),(10,'jiebin@qq.com','/api/admin/contest/registrations','2022-04-28 02:26:25'),(11,'jiebin@qq.com','/api/admin/contest/delete','2022-04-28 02:26:27'),(12,'jiebin@qq.com','/api/admin/contest/query/all','2022-04-28 02:26:27'),(13,'jiebin@qq.com','/api/admin/question/query/detail','2022-04-28 02:26:29'),(14,'jiebin@qq.com','/api/admin/question/delete','2022-04-28 02:26:31'),(15,'jiebin@qq.com','/api/admin/question/query/all','2022-04-28 02:26:31'),(16,'jiebin@qq.com','/api/admin/question/query/detail','2022-04-28 02:26:32'),(17,'jiebin@qq.com','/api/admin/question/delete','2022-04-28 02:26:35'),(18,'jiebin@qq.com','/api/admin/question/query/all','2022-04-28 02:26:35'),(19,'jiebin@qq.com','/api/admin/question/query/detail','2022-04-28 02:26:36'),(20,'jiebin@qq.com','/api/admin/question/delete','2022-04-28 02:26:38'),(21,'jiebin@qq.com','/api/admin/question/query/all','2022-04-28 02:26:38'),(22,'jiebin@qq.com','/api/admin/question/query/all','2022-04-28 02:26:38'),(23,'admin@mail.com','/api/admin/contest/query/all','2022-05-13 10:45:22'),(24,'admin@mail.com','/api/admin/question/query/all','2022-05-13 10:45:40'),(25,'admin@mail.com','/api/admin/question/new','2022-05-13 11:04:30'),(26,'admin@mail.com','/api/admin/question/query/all','2022-05-13 11:04:31'),(27,'admin@mail.com','/api/admin/question/query/detail','2022-05-13 11:04:31'),(28,'admin@mail.com','/api/admin/question/update','2022-05-13 11:04:49'),(29,'admin@mail.com','/api/admin/question/query/detail','2022-05-13 11:04:49'),(30,'admin@mail.com','/api/admin/question/update','2022-05-13 11:05:27'),(31,'admin@mail.com','/api/admin/question/query/detail','2022-05-13 11:05:28'),(32,'admin@mail.com','/api/admin/question/new','2022-05-13 11:07:10'),(33,'admin@mail.com','/api/admin/question/query/all','2022-05-13 11:07:10'),(34,'admin@mail.com','/api/admin/question/query/detail','2022-05-13 11:07:11'),(35,'admin@mail.com','/api/admin/contest/new','2022-05-13 11:09:22'),(36,'admin@mail.com','/api/admin/contest/query/all','2022-05-13 11:09:22'),(37,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 11:09:23'),(38,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 11:09:26'),(39,'admin@mail.com','/api/admin/contest/inclusion/add','2022-05-13 11:09:34'),(40,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 14:50:46'),(41,'admin@mail.com','/api/admin/contest/query/all','2022-05-13 14:50:46'),(42,'admin@mail.com','/api/admin/question/query/all','2022-05-13 14:50:46'),(43,'admin@mail.com','/api/admin/contest/update','2022-05-13 16:43:31'),(44,'admin@mail.com','/api/admin/contest/update','2022-05-13 16:43:44'),(45,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 16:43:48'),(46,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 16:43:50'),(47,'admin@mail.com','/api/admin/contest/query/all','2022-05-13 16:44:32'),(48,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 16:44:32'),(49,'admin@mail.com','/api/admin/question/query/all','2022-05-13 16:44:32'),(50,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 16:46:28'),(51,'admin@mail.com','/api/admin/contest/update','2022-05-13 16:46:36'),(52,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 16:46:38'),(53,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 16:48:37'),(54,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 16:48:45'),(55,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 16:49:02'),(56,'admin@mail.com','/api/admin/question/query/all','2022-05-13 18:10:07'),(57,'admin@mail.com','/api/admin/contest/query/all','2022-05-13 18:10:07'),(58,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 18:10:07'),(59,'admin@mail.com','/api/admin/contest/query/all','2022-05-13 21:00:50'),(60,'admin@mail.com','/api/admin/question/query/all','2022-05-13 21:01:07'),(61,'admin@mail.com','/api/admin/contest/query/all','2022-05-13 21:01:09'),(62,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 21:01:11'),(63,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 21:01:14'),(64,'admin@mail.com','/api/admin/contest/query/all','2022-05-13 21:06:34'),(65,'admin@mail.com','/api/admin/question/query/all','2022-05-13 21:06:38'),(66,'admin@mail.com','/api/admin/question/new','2022-05-13 21:07:38'),(67,'admin@mail.com','/api/admin/question/query/all','2022-05-13 21:07:38'),(68,'admin@mail.com','/api/admin/question/query/detail','2022-05-13 21:07:38'),(69,'admin@mail.com','/api/admin/question/query/detail','2022-05-13 21:07:49'),(70,'admin@mail.com','/api/admin/question/update','2022-05-13 21:08:15'),(71,'admin@mail.com','/api/admin/question/query/detail','2022-05-13 21:08:15'),(72,'admin@mail.com','/api/admin/question/update','2022-05-13 21:08:19'),(73,'admin@mail.com','/api/admin/question/query/detail','2022-05-13 21:08:19'),(74,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 21:08:24'),(75,'admin@mail.com','/api/admin/question/query/detail','2022-05-13 21:08:27'),(76,'admin@mail.com','/api/admin/question/update','2022-05-13 21:09:26'),(77,'admin@mail.com','/api/admin/question/query/detail','2022-05-13 21:09:26'),(78,'admin@mail.com','/api/admin/contest/new','2022-05-13 21:10:41'),(79,'admin@mail.com','/api/admin/contest/query/all','2022-05-13 21:10:41'),(80,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 21:10:41'),(81,'admin@mail.com','/api/admin/contest/update','2022-05-13 21:11:54'),(82,'admin@mail.com','/api/admin/contest/inclusion/add','2022-05-13 21:12:31'),(83,'admin@mail.com','/api/admin/contest/inclusion/update','2022-05-13 21:12:33'),(84,'admin@mail.com','/api/admin/contest/inclusion/update','2022-05-13 21:12:34'),(85,'admin@mail.com','/api/admin/contest/inclusion/update','2022-05-13 21:12:35'),(86,'admin@mail.com','/api/admin/contest/inclusion/remove','2022-05-13 21:12:40'),(87,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 21:12:46'),(88,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 21:13:35'),(89,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 21:13:45'),(90,'admin@mail.com','/api/admin/contest/update','2022-05-13 21:13:46'),(91,'admin@mail.com','/api/admin/contest/query/all','2022-05-13 21:13:58'),(92,'admin@mail.com','/api/admin/question/query/all','2022-05-13 21:13:58'),(93,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 21:13:58'),(94,'admin@mail.com','/api/admin/contest/update','2022-05-13 21:14:08'),(95,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 21:14:11'),(96,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 21:14:23'),(97,'admin@mail.com','/api/admin/contest/update','2022-05-13 21:14:24'),(98,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 21:14:27'),(99,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 21:14:31'),(100,'admin@mail.com','/api/admin/contest/update','2022-05-13 21:14:42'),(101,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 21:14:45'),(102,'admin@mail.com','/api/admin/question/query/all','2022-05-13 21:14:45'),(103,'admin@mail.com','/api/admin/contest/query/all','2022-05-13 21:14:45'),(104,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 21:14:47'),(105,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 21:19:18'),(106,'admin@mail.com','/api/admin/question/query/all','2022-05-13 21:19:29'),(107,'admin@mail.com','/api/admin/contest/query/all','2022-05-13 21:19:29'),(108,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 21:19:29'),(109,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 21:20:03'),(110,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 21:20:38'),(111,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 21:22:39'),(112,'admin@mail.com','/api/admin/question/query/detail','2022-05-13 21:27:37'),(113,'admin@mail.com','/api/admin/question/update','2022-05-13 21:27:50'),(114,'admin@mail.com','/api/admin/question/query/detail','2022-05-13 21:27:50'),(115,'admin@mail.com','/api/admin/question/query/detail','2022-05-13 21:28:02'),(116,'admin@mail.com','/api/admin/question/update','2022-05-13 21:28:31'),(117,'admin@mail.com','/api/admin/question/query/detail','2022-05-13 21:28:31'),(118,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 21:49:30'),(119,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 21:49:34'),(120,'admin@mail.com','/api/admin/contest/update','2022-05-13 21:49:47'),(121,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 21:49:49'),(122,'admin@mail.com','/api/admin/contest/update/status','2022-05-13 22:31:14'),(123,'admin@mail.com','/api/admin/question/query/all','2022-05-13 22:31:24'),(124,'admin@mail.com','/api/admin/question/query/all','2022-05-13 22:31:30'),(125,'admin@mail.com','/api/admin/question/query/detail','2022-05-13 22:31:32'),(126,'admin@mail.com','/api/admin/contest/query/all','2022-05-13 22:31:41'),(127,'admin@mail.com','/api/admin/contest/registrations','2022-05-13 22:31:46'),(128,'admin@mail.com','/api/admin/question/query/detail','2022-05-13 22:31:49'),(129,'admin@mail.com','/api/admin/question/delete','2022-05-13 22:31:52'),(130,'admin@mail.com','/api/admin/question/query/all','2022-05-13 22:31:52'),(131,'admin@mail.com','/api/admin/contest/registrations','2022-05-14 14:03:17'),(132,'admin@mail.com','/api/admin/contest/update/status','2022-05-14 14:03:47'),(133,'admin@mail.com','/api/admin/contest/update/status','2022-05-14 14:03:50'),(134,'admin@mail.com','/api/admin/contest/update/status','2022-05-14 14:03:53'),(135,'admin@mail.com','/api/admin/contest/registrations','2022-05-14 14:17:23'),(136,'admin@mail.com','/api/admin/contest/registrations','2022-05-14 14:17:32'),(137,'admin@mail.com','/api/admin/contest/query/all','2022-05-14 14:17:41'),(138,'admin@mail.com','/api/admin/question/query/all','2022-05-14 14:17:41'),(139,'admin@mail.com','/api/admin/contest/registrations','2022-05-14 14:17:41'),(140,'admin@mail.com','/api/admin/contest/update/status','2022-05-14 14:17:44'),(141,'admin@mail.com','/api/admin/contest/query/all','2022-05-14 14:25:28'),(142,'admin@mail.com','/api/admin/contest/registrations','2022-05-14 14:25:28'),(143,'admin@mail.com','/api/admin/question/query/all','2022-05-14 14:25:28'),(144,'admin@mail.com','/api/admin/contest/query/all','2022-05-14 14:56:45'),(145,'admin@mail.com','/api/admin/question/query/all','2022-05-14 14:56:57'),(146,'admin@mail.com','/api/admin/question/query/detail','2022-05-14 15:00:42'),(147,'admin@mail.com','/api/admin/question/query/detail','2022-05-14 15:00:44'),(148,'admin@mail.com','/api/admin/question/query/detail','2022-05-14 15:01:18'),(149,'admin@mail.com','/api/admin/contest/registrations','2022-05-14 15:01:45'),(150,'admin@mail.com','/api/admin/contest/registrations','2022-05-14 15:01:58'),(151,'admin@mail.com','/api/admin/contest/update/status','2022-05-14 15:02:04'),(152,'admin@mail.com','/api/admin/question/query/all','2022-05-14 15:02:10'),(153,'admin@mail.com','/api/admin/contest/registrations','2022-05-14 15:02:10'),(154,'admin@mail.com','/api/admin/contest/query/all','2022-05-14 15:02:10'),(155,'admin@mail.com','/api/admin/contest/update','2022-05-14 15:02:21'),(156,'admin@mail.com','/api/admin/contest/update/status','2022-05-14 15:02:24'),(157,'admin@mail.com','/api/admin/contest/update/status','2022-05-14 15:05:12'),(158,'admin@mail.com','/api/admin/question/query/detail','2022-05-14 15:06:47'),(159,'admin@mail.com','/api/admin/question/new','2022-05-14 15:10:14'),(160,'admin@mail.com','/api/admin/question/query/all','2022-05-14 15:10:14'),(161,'admin@mail.com','/api/admin/question/query/detail','2022-05-14 15:10:14'),(162,'admin@mail.com','/api/admin/question/update','2022-05-14 15:10:39'),(163,'admin@mail.com','/api/admin/question/query/detail','2022-05-14 15:10:39');
/*!40000 ALTER TABLE `t_admin_option_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_contest`
--

DROP TABLE IF EXISTS `t_contest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_contest` (
  `contest_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `type` int NOT NULL,
  `description` text NOT NULL,
  `duration` int NOT NULL,
  `start_time` datetime NOT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`contest_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_contest`
--

LOCK TABLES `t_contest` WRITE;
/*!40000 ALTER TABLE `t_contest` DISABLE KEYS */;
INSERT INTO `t_contest` VALUES (13,'第一次竞赛活动',1,'这是一次竞赛记录',60,'2022-05-13 21:15:03',4),(14,'竞赛创建演示练习',1,'第一次竞赛创建演示',55,'2022-05-14 15:02:13',3);
/*!40000 ALTER TABLE `t_contest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_question`
--

DROP TABLE IF EXISTS `t_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_question` (
  `question_id` int NOT NULL AUTO_INCREMENT,
  `type` int NOT NULL,
  `level` int NOT NULL,
  `title` varchar(45) NOT NULL,
  `description` text NOT NULL,
  `description_last_update_time` datetime NOT NULL,
  `code_template` text NOT NULL,
  `code_template_last_update_time` datetime NOT NULL,
  `test_json_data` json NOT NULL,
  `test_json_data_last_update_time` datetime NOT NULL,
  PRIMARY KEY (`question_id`),
  UNIQUE KEY `title_UNIQUE` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_question`
--

LOCK TABLES `t_question` WRITE;
/*!40000 ALTER TABLE `t_question` DISABLE KEYS */;
INSERT INTO `t_question` VALUES (28,1,1,'加一函数','<p>加一函数</p>\n<p>输入一个数组，返回这个数字的值+1，如下</p>\n<ul>\n<li>input: 1, output: 2</li>\n</ul>\n<p>代码例子：</p>\n<pre><code class=\"language-Kotlin\">fun increase(input: Int): Int {\n  return input + 1\n}\n</code></pre>\n<p>死循环</p>\n<pre><code class=\"language-Kotlin\">fun increase(input: Int): Int  {\n  var i = 1\n  while(i &gt; 0) {\n    if(input &lt; 0) {\n      i--\n    }\n  }\n  return i\n}\n</code></pre>\n<p>部分成功</p>\n<pre><code class=\"language-Kotlin\">fun increase(input: Int): Int  {\n  return when(input) {\n    1 -&gt; 2\n    11 -&gt; 12\n    else -&gt; input\n  }\n}\n</code></pre>\n','2022-05-13 21:28:31','\nfun invoke(item: Map<String, Any>): Boolean {\n  val input = item[\"input\"] as Int\n  val output = item[\"output\"] as Int\n  return increase(input) == output\n}\n\n// user start\nfun increase(input: Int) {\n  // TODO\n}\n// user end\n','2022-05-13 21:28:31','[{\"input\": 1, \"output\": 2}, {\"input\": 2, \"output\": 3}, {\"input\": 11, \"output\": 12}, {\"input\": 20, \"output\": 21}, {\"input\": 21, \"output\": 22}, {\"input\": 31, \"output\": 32}, {\"input\": 41, \"output\": 42}, {\"input\": 51, \"output\": 52}, {\"input\": 61, \"output\": 62}, {\"input\": 71, \"output\": 72}]','2022-05-13 21:28:31'),(29,1,2,'数字类型转化','<p>数字类型转化</p>\n<p>提供一个 Int 类型的数字，转化成 Float 类型返回</p>\n<ul>\n<li>input: 1, output: 1.0 / 1f</li>\n</ul>\n<p>代码例子：</p>\n<pre><code class=\"language-Kotlin\">fun toFloat(input: Int): Float {\n  return input.toFloat()\n}\n</code></pre>\n','2022-05-13 11:07:10','\nfun invoke(item: Map<String, Any>): Boolean {\n  val input = item[\"input\"] as Int\n  val output = item[\"output\"] as Int\n  return toFloat(input) == output.toFloat()\n}\n\n// user start\nfun toFloat(input: Int): Float {\n  // TODO\n}\n// user end\n','2022-05-13 11:07:10','[{\"input\": 1, \"output\": 1}, {\"input\": 2, \"output\": 2}, {\"input\": 3, \"output\": 3}, {\"input\": 4, \"output\": 4}, {\"input\": 5, \"output\": 5}, {\"input\": 6, \"output\": 6}, {\"input\": 7, \"output\": 7}, {\"input\": 8, \"output\": 8}, {\"input\": 9, \"output\": 9}, {\"input\": 0, \"output\": 0}]','2022-05-13 11:07:10'),(31,1,1,'上传演示','<p>数字类型转化</p>\n<p>提供一个 Int 类型的数字，转化成 Float 类型返回</p>\n<ul>\n<li>input: 1, output: 1.0 / 1f</li>\n</ul>\n<p>代码例子：</p>\n<pre><code class=\"language-Kotlin\">fun toFloat(input: Int): Float {\n  return input.toFloat()\n}\n</code></pre>\n','2022-05-14 15:10:39','\nfun invoke(item: Map<String, Any>): Boolean {\n  val input = item[\"input\"] as Int\n  val output = item[\"output\"] as Int\n  return increase(input) == output\n}\n\n// user start\nfun increase(input: Int) {\n  // TODO\n}\n// user end\n','2022-05-14 15:10:39','[{\"input\": 1, \"output\": 2}, {\"input\": 2, \"output\": 3}, {\"input\": 11, \"output\": 12}, {\"input\": 20, \"output\": 21}, {\"input\": 21, \"output\": 22}, {\"input\": 31, \"output\": 32}, {\"input\": 41, \"output\": 42}, {\"input\": 51, \"output\": 52}, {\"input\": 61, \"output\": 62}, {\"input\": 71, \"output\": 72}]','2022-05-14 15:10:39');
/*!40000 ALTER TABLE `t_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_submit_record`
--

DROP TABLE IF EXISTS `t_submit_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_submit_record` (
  `submit_id` int NOT NULL AUTO_INCREMENT,
  `question_id` int NOT NULL COMMENT 'fk',
  `user_id` int NOT NULL COMMENT 'fk',
  `contest_id` int DEFAULT NULL,
  `answer` text NOT NULL,
  `pass` int NOT NULL,
  `submit_time` datetime NOT NULL,
  `elapsed_time` int NOT NULL,
  `message` text NOT NULL,
  PRIMARY KEY (`submit_id`),
  KEY `fk_submit_user_idx` (`user_id`),
  KEY `fk_submit_question` (`question_id`),
  KEY `fk_submit_contest_idx` (`contest_id`),
  CONSTRAINT `fk_submit_contest` FOREIGN KEY (`contest_id`) REFERENCES `t_contest` (`contest_id`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `fk_submit_question` FOREIGN KEY (`question_id`) REFERENCES `t_question` (`question_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_submit_user` FOREIGN KEY (`user_id`) REFERENCES `t_user_profile` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_submit_record`
--

LOCK TABLES `t_submit_record` WRITE;
/*!40000 ALTER TABLE `t_submit_record` DISABLE KEYS */;
INSERT INTO `t_submit_record` VALUES (36,28,48782433,13,'fun increase(input: Int): Int {\n  return input + 1\n}',100,'2022-05-13 16:44:54',6,'测试通过, 代码耗时：6毫秒'),(37,28,48782433,13,'fun increase(input: Int): Int  {\n  var i = 1\n  while(i > 0) {\n    if(input < 0) {\n      i--\n    }\n  }\n  return i\n}',0,'2022-05-13 16:45:04',2011,'执行超时，请检查代码的时间复杂度'),(38,28,48782433,13,'fun increase(input: Int): Int  {\n  return when(input) {\n    1 -> 2\n    11 -> 12\n    else -> input\n  }\n}',20,'2022-05-13 16:45:12',14,'没有通过所有用例, 代码耗时：14毫秒'),(39,29,48782433,13,'fun toFloat(input: Int): Float {\n  return input.toFloat()\n}',100,'2022-05-13 16:45:33',12,'测试通过, 代码耗时：12毫秒'),(40,28,74779534,13,'fun increase(input: Int): Int {\n  return input + 1\n}',100,'2022-05-13 16:47:45',12,'测试通过, 代码耗时：12毫秒'),(41,28,74779534,13,'fun increase(input: Int): Int  {\n  var i = 1\n  while(i > 0) {\n    if(input < 0) {\n      i--\n    }\n  }\n  return i\n}',0,'2022-05-13 16:47:50',2008,'执行超时，请检查代码的时间复杂度'),(42,28,74779534,13,'fun increase(input: Int): Int  {\n  return when(input) {\n    1 -> 2\n    11 -> 12\n    else -> input\n  }\n}',20,'2022-05-13 16:47:56',15,'没有通过所有用例, 代码耗时：15毫秒'),(43,29,74779534,13,'fun toFloat(input: Int): Float {\n}',0,'2022-05-13 16:48:04',0,'编译错误 :2:1: error: a \'return\' expression required in a function with a block body (\'{...}\')\n}\n^\n'),(44,29,74779534,13,'fun toFloat(input: Int): Float {\n  return input.toInt()\n}',0,'2022-05-13 16:48:15',0,'编译错误 :2:10: error: type mismatch: inferred type is Int but Float was expected\n  return input.toInt()\n         ^\n'),(45,29,74779534,13,'fun toFloat(input: Int): Float {\n  return input.toFloat()\n}',100,'2022-05-13 16:48:20',13,'测试通过, 代码耗时：13毫秒'),(46,29,48782433,14,'fun toFloat(input: Int): Float {\n  return input.toFloat()\n}',100,'2022-05-13 21:17:24',4,'测试通过, 代码耗时：4毫秒'),(47,29,48782433,14,'fun toFloat(input: Int): Float {\n  return input\n}',0,'2022-05-13 21:17:33',0,'编译错误 :2:10: error: type mismatch: inferred type is Int but Float was expected\n  return input\n         ^\n'),(48,28,48782433,14,'fun increase(input: Int): Int  {\n  return when(input) {\n    1 -> 2\n    11 -> 12\n    else -> input\n  }\n}',20,'2022-05-13 21:18:06',5,'没有通过所有用例, 代码耗时：5毫秒'),(49,28,48782433,14,'fun increase(input: Int): Int  {\n  var i = 1\n  while(i > 0) {\n    if(input < 0) {\n      i--\n    }\n  }\n  return i\n}',0,'2022-05-13 21:18:23',2002,'执行超时，请检查代码的时间复杂度'),(50,29,48782433,14,'fun toFloat(input: Int): Float {\n  return input.toFloat()\n}',100,'2022-05-14 15:03:23',22,'测试通过, 代码耗时：22毫秒');
/*!40000 ALTER TABLE `t_submit_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user_profile`
--

DROP TABLE IF EXISTS `t_user_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user_profile` (
  `id` int NOT NULL,
  `login` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `email` varchar(255) NOT NULL,
  `avatar_url` tinytext,
  `bio` tinytext,
  `html_url` tinytext NOT NULL,
  `block` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户临时信息存储表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user_profile`
--

LOCK TABLES `t_user_profile` WRITE;
/*!40000 ALTER TABLE `t_user_profile` DISABLE KEYS */;
INSERT INTO `t_user_profile` VALUES (48782433,'korilin','korilin','korilin.dev@gmail.com','https://avatars.githubusercontent.com/u/48782433?v=4','二次元+开发者，深圳 Kotlin User Group 组织者，主业Android开发，欢迎找我交流','https://github.com/korilin',0),(74779534,'adjuchas','adjuchas','1312804080@qq.com','https://avatars.githubusercontent.com/u/74779534?v=4','adjuchas','https://github.com/adjuchas',1);
/*!40000 ALTER TABLE `t_user_profile` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-05-29 16:51:33

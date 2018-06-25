-- MySQL dump 10.13  Distrib 8.0.11, for Win64 (x86_64)
--
-- Host: localhost    Database: ha
-- ------------------------------------------------------
-- Server version	8.0.11

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `account` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned DEFAULT NULL,
  `category_id` int(11) unsigned DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `type` char(1) DEFAULT NULL,
  `balance` decimal(14,2) DEFAULT NULL,
  `active` bit(1) DEFAULT NULL,
  `local_currency` bit(1) DEFAULT NULL,
  `instrument_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `account_category_fk_idx` (`category_id`),
  KEY `account_user_fk_idx` (`user_id`),
  KEY `account_instrument_fk_idx` (`instrument_id`),
  CONSTRAINT `account_category_fk` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `account_instrument_fk` FOREIGN KEY (`instrument_id`) REFERENCES `instrument` (`id`),
  CONSTRAINT `account_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (2,1,1,'Caja','A',82027.00,'','',NULL),(3,1,4,'Expensas','E',6930.00,'','',NULL),(4,1,2,'Tarjeta de Credito','L',4348.00,'','',NULL),(5,1,3,'Sueldo','R',114132.00,'','',NULL),(6,1,1,'Allaria','A',180.00,'','',NULL),(7,1,6,'Prueba1','L',1.00,'','',NULL),(8,1,6,'Prueba1234','L',1.00,'\0','',NULL),(9,1,6,'Prueba987','A',987.00,'\0','',NULL),(10,1,6,'Prueba321','L',321.00,'','',NULL),(11,1,1,'Euros','A',222.00,'\0','',NULL),(12,1,11,'Mantenimiento','E',0.00,'','',NULL),(13,1,11,'Mecanico','E',-94264.00,'','',NULL),(14,1,6,'ADesactivar1','L',0.00,'\0','',NULL),(15,1,31,'NuevaCuenta','L',8.00,'\0','',NULL),(16,1,4,'','E',200.00,'\0','',NULL),(18,1,1,'DolaresColchon','A',0.00,'\0','',NULL),(21,1,1,'DolaresColchon1','A',-5998.00,'','\0',2);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank`
--

DROP TABLE IF EXISTS `bank`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `bank` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `bank_user_fk_idx` (`user_id`),
  CONSTRAINT `bank_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank`
--

LOCK TABLES `bank` WRITE;
/*!40000 ALTER TABLE `bank` DISABLE KEYS */;
/*!40000 ALTER TABLE `bank` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `category` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned DEFAULT NULL,
  `parent_id` int(10) unsigned DEFAULT NULL,
  `type` char(1) DEFAULT NULL,
  `fixed` bit(1) DEFAULT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `category_user_fk_idx` (`user_id`),
  KEY `category_category_fk_idx` (`parent_id`),
  CONSTRAINT `category_category_fk` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`),
  CONSTRAINT `category_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,1,NULL,'A','','Activo'),(2,1,NULL,'L','','Pasivo'),(3,1,NULL,'R','','Ganancias'),(4,1,NULL,'E','','Pérdidas'),(5,1,2,'L','\0','Tarjeta de Crédito'),(6,1,2,'L','\0','Deuda'),(7,1,4,'E','\0','Transporte'),(8,1,4,'E','\0','Departamento'),(9,1,4,'E','\0','Indumentaria'),(11,1,7,'E','\0','Auto'),(13,1,11,'E','\0','Seguro'),(14,1,11,'E','\0','Combustible'),(31,1,6,'L','\0','Credito Personal'),(33,1,5,'L','\0','American Express'),(34,1,5,'L','\0','Visa'),(36,1,NULL,'Q','','Equity');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entry`
--

DROP TABLE IF EXISTS `entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `entry` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned DEFAULT NULL,
  `date` date NOT NULL,
  `description` varchar(127) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `entry_user_fk_idx` (`user_id`),
  CONSTRAINT `entry_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entry`
--

LOCK TABLES `entry` WRITE;
/*!40000 ALTER TABLE `entry` DISABLE KEYS */;
INSERT INTO `entry` VALUES (3,1,'2016-07-02','le description'),(4,1,'2016-07-02','Updating balances'),(5,1,'2016-07-02','balance update'),(6,1,'2016-07-02','debugging null pointer'),(7,1,'2016-07-02','null pointer solved?'),(8,1,'2016-07-02','third attempt'),(9,1,'2016-07-02','fourth attempt'),(10,1,'2016-07-02','sixth sense'),(11,1,'2016-07-06','descrip'),(12,1,'2016-07-07','sdf'),(16,1,'2016-07-08','dkdk'),(17,1,'2016-07-08','ddid'),(18,1,'2016-07-08','sdfa'),(19,1,'2016-07-08','sadl'),(20,1,'2016-07-08','safd'),(21,1,'2016-07-08','sfas'),(22,1,'2016-07-08',''),(23,1,'2016-07-08','asf'),(24,1,'2016-07-08','asf'),(25,1,'2016-07-08',''),(26,1,'2016-07-08',''),(27,1,'2016-07-08','dfdf'),(28,1,'2016-07-08','dldld'),(29,1,'2016-07-08',''),(30,1,'2016-07-09','sdf'),(31,1,'2016-07-09',''),(32,1,'2016-07-09',''),(33,1,'2016-07-09',''),(34,1,'2016-07-09',''),(35,1,'2016-07-09',''),(36,1,'2016-07-09',''),(37,1,'2016-07-09',''),(38,1,'2016-07-09',''),(39,1,'2016-07-09',''),(40,1,'2016-07-09',''),(41,1,'2016-07-09',''),(42,1,'2016-07-09','sdfa'),(43,1,'2016-07-09',''),(44,1,'2016-07-09',''),(45,1,'2016-07-09',''),(46,1,'2016-07-09','description'),(47,1,'2016-07-09','un error'),(48,1,'2016-07-09',''),(49,1,'2016-07-16','ConCreditosYDebitos'),(50,1,'2016-07-16','ConCreditosYDebitos2'),(51,1,'2016-07-17','descr'),(52,1,'2016-09-06','FechaJavaUtil'),(53,1,'2016-09-06','FechaJavaUtil5'),(54,1,'2016-09-03','ConFecha4'),(55,1,'2016-09-02','ConFecha5'),(56,1,'2016-09-01','ConFecha6'),(57,1,'2016-09-12','NoDeberiaAndar'),(58,1,'2016-09-13','Andar�?'),(59,1,'2016-09-14',''),(60,1,'2016-09-14',''),(61,1,'2016-09-14',''),(62,1,'2016-09-14','NoSeBorra'),(63,1,'2016-09-14','NoSeBorra'),(64,1,'2016-09-19','dkd'),(65,1,'2016-09-19','Prueba'),(66,1,'2016-09-20','Prueba'),(67,1,'2016-09-19','Prueba2'),(68,1,'2016-09-18','Prueba2'),(69,1,'2016-09-24','PruebaMonedaLocal'),(70,1,'2016-10-02',''),(71,1,'2016-10-02',''),(78,1,'2016-10-15','PersistirAparte'),(79,1,'2016-10-15','PersistirAparte2'),(80,1,'2016-10-15','PersistirAparte3'),(81,1,'2016-10-15','Instrumento'),(82,1,'2016-10-15','Instrumento2'),(83,1,'2016-10-15','PersistirAparte5'),(84,1,'2016-10-15','Instrumento3'),(85,1,'2016-10-15','Instrumento5'),(86,1,'2016-10-15','Instrumento6'),(87,1,'2016-10-15','Instrumento11'),(88,1,'2016-10-15','Instrumento11'),(89,1,'2016-10-15','Instrumento12'),(90,1,'2016-10-27','Instrumento'),(91,1,'2016-10-27','Instrumento'),(92,1,'2016-10-27','Instrumento200'),(93,1,'2016-11-16','Cambio de Aceite'),(94,1,'2016-11-16','Compra de d�lares');
/*!40000 ALTER TABLE `entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entry_line`
--

DROP TABLE IF EXISTS `entry_line`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `entry_line` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned DEFAULT NULL,
  `entry_id` int(10) unsigned DEFAULT NULL,
  `account_id` int(10) unsigned DEFAULT NULL,
  `action` char(1) DEFAULT NULL,
  `amount` decimal(14,2) DEFAULT NULL,
  `quote_id` int(10) unsigned DEFAULT NULL,
  `quantity` decimal(14,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `account_id_idx` (`account_id`),
  KEY `entry_fk_idx` (`entry_id`),
  KEY `user_fk_idx` (`user_id`),
  KEY `entry_line_quote_fk_idx` (`quote_id`),
  CONSTRAINT `entry_line_account_fk` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `entry_line_entry_fk` FOREIGN KEY (`entry_id`) REFERENCES `entry` (`id`),
  CONSTRAINT `entry_line_quote_fk` FOREIGN KEY (`quote_id`) REFERENCES `quote` (`id`),
  CONSTRAINT `entry_line_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entry_line`
--

LOCK TABLES `entry_line` WRITE;
/*!40000 ALTER TABLE `entry_line` DISABLE KEYS */;
INSERT INTO `entry_line` VALUES (5,1,3,3,'D',110.00,NULL,NULL),(6,1,3,2,'D',111.00,NULL,NULL),(7,1,4,2,'D',40000.00,NULL,NULL),(8,1,4,5,'C',40000.00,NULL,NULL),(9,1,5,2,'D',35000.00,NULL,NULL),(10,1,5,5,'C',35005.00,NULL,NULL),(11,1,6,6,'D',100.00,NULL,NULL),(12,1,6,3,'D',203.00,NULL,NULL),(13,1,7,5,'C',40202.00,NULL,NULL),(14,1,7,3,'D',54321.00,NULL,NULL),(15,1,8,3,'D',1234.00,NULL,NULL),(16,1,8,2,'D',1235.00,NULL,NULL),(17,1,9,2,'D',3021.00,NULL,NULL),(18,1,9,5,'C',543.00,NULL,NULL),(19,1,10,2,'D',40303.00,NULL,NULL),(20,1,10,5,'C',34343.00,NULL,NULL),(21,1,11,2,'D',30303.00,NULL,NULL),(22,1,11,3,'D',0.00,NULL,NULL),(23,1,12,3,'D',5050.00,NULL,NULL),(24,1,12,6,'D',5050.00,NULL,NULL),(25,1,12,4,'C',5050.00,NULL,NULL),(26,1,12,6,'D',5050.00,NULL,NULL),(29,1,16,4,'C',333.00,NULL,NULL),(30,1,17,3,'D',404.00,NULL,NULL),(31,1,18,2,'D',202.00,NULL,NULL),(32,1,19,6,'D',303.00,NULL,NULL),(33,1,20,5,'C',101.00,NULL,NULL),(34,1,21,4,'C',122.00,NULL,NULL),(35,1,21,5,'C',0.00,NULL,NULL),(37,1,22,4,'C',0.00,NULL,NULL),(39,1,23,5,'C',0.00,NULL,NULL),(41,1,24,2,'D',0.00,NULL,NULL),(43,1,25,6,'D',0.00,NULL,NULL),(45,1,26,3,'D',0.00,NULL,NULL),(47,1,27,4,'C',0.00,NULL,NULL),(55,1,78,3,'C',1910.00,NULL,NULL),(56,1,78,2,'D',1910.00,NULL,NULL),(57,1,79,6,'C',100.00,NULL,NULL),(58,1,79,2,'D',100.00,NULL,NULL),(59,1,80,2,'C',40000.00,NULL,NULL),(60,1,80,5,'D',40000.00,NULL,NULL),(61,1,83,3,'C',345.00,NULL,NULL),(62,1,83,2,'D',345.00,NULL,NULL),(63,1,84,2,'C',4500.00,NULL,NULL),(64,1,87,2,'D',150.00,NULL,NULL),(66,1,88,2,'D',150.00,NULL,NULL),(68,1,89,2,'D',150.00,NULL,NULL),(69,1,90,2,'C',1500.00,NULL,NULL),(71,1,91,2,'C',1500.00,NULL,NULL),(72,1,91,21,'D',NULL,5,100.00),(73,1,92,2,'C',3000.00,NULL,NULL),(74,1,92,21,'D',NULL,6,200.00),(75,1,93,13,'C',2095.00,NULL,NULL),(76,1,93,2,'D',2095.00,NULL,NULL),(77,1,94,21,'C',NULL,7,100.00),(78,1,94,2,'D',1500.00,NULL,NULL);
/*!40000 ALTER TABLE `entry_line` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `extract`
--

DROP TABLE IF EXISTS `extract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `extract` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned DEFAULT NULL,
  `bank_id` int(10) unsigned DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `amount` decimal(14,2) DEFAULT NULL,
  `status` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `extract_user_fk_idx` (`user_id`),
  KEY `extract_bank_fk_idx` (`bank_id`),
  CONSTRAINT `extract_bank_fk` FOREIGN KEY (`bank_id`) REFERENCES `bank` (`id`),
  CONSTRAINT `extract_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `extract`
--

LOCK TABLES `extract` WRITE;
/*!40000 ALTER TABLE `extract` DISABLE KEYS */;
/*!40000 ALTER TABLE `extract` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `extract_map_rule`
--

DROP TABLE IF EXISTS `extract_map_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `extract_map_rule` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `bank_id` int(10) unsigned NOT NULL,
  `regex` varchar(45) NOT NULL,
  `account_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `extract_map_rule_user_fk_idx` (`user_id`),
  KEY `extract_map_rule_bank_fk_idx` (`bank_id`),
  KEY `extract_map_rule_account_fk_idx` (`account_id`),
  CONSTRAINT `extract_map_rule_account_fk` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `extract_map_rule_bank_fk` FOREIGN KEY (`bank_id`) REFERENCES `bank` (`id`),
  CONSTRAINT `extract_map_rule_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `extract_map_rule`
--

LOCK TABLES `extract_map_rule` WRITE;
/*!40000 ALTER TABLE `extract_map_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `extract_map_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instrument`
--

DROP TABLE IF EXISTS `instrument`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `instrument` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `code` varchar(5) NOT NULL,
  `type` char(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instrument`
--

LOCK TABLES `instrument` WRITE;
/*!40000 ALTER TABLE `instrument` DISABLE KEYS */;
INSERT INTO `instrument` VALUES (2,'Dolar','USD','B');
/*!40000 ALTER TABLE `instrument` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quote`
--

DROP TABLE IF EXISTS `quote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `quote` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `instrument_id` int(10) unsigned NOT NULL,
  `type` char(1) NOT NULL,
  `date` date NOT NULL,
  `price` decimal(14,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `quote_instrument_fk_idx` (`instrument_id`),
  CONSTRAINT `quote_instrument_fk` FOREIGN KEY (`instrument_id`) REFERENCES `instrument` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quote`
--

LOCK TABLES `quote` WRITE;
/*!40000 ALTER TABLE `quote` DISABLE KEYS */;
INSERT INTO `quote` VALUES (1,2,'\0','2016-10-15',15.00),(2,2,'B','2016-10-15',15.00),(3,2,'B','2016-10-15',15.00),(4,2,'B','2016-10-27',15.00),(5,2,'B','2016-10-27',15.00),(6,2,'B','2016-10-27',15.00),(7,2,'B','2016-11-16',15.00);
/*!40000 ALTER TABLE `quote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userid` varchar(8) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'azuzek','guzanicho');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-06-25 13:28:11

CREATE DATABASE  IF NOT EXISTS `podsistem3` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `podsistem3`;
-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: podsistem3
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `grad`
--

DROP TABLE IF EXISTS `grad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grad` (
  `idGra` int unsigned NOT NULL,
  `Naziv` varchar(45) NOT NULL,
  PRIMARY KEY (`idGra`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grad`
--

LOCK TABLES `grad` WRITE;
/*!40000 ALTER TABLE `grad` DISABLE KEYS */;
INSERT INTO `grad` VALUES (1,'Beograd'),(2,'Novi Sad'),(25,'Valjevo'),(26,'Subotica'),(27,'Kragujevac'),(28,'Nis'),(29,'Leskovac');
/*!40000 ALTER TABLE `grad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `korisnik`
--

DROP TABLE IF EXISTS `korisnik`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `korisnik` (
  `idKor` int unsigned NOT NULL,
  `Kime` varchar(45) NOT NULL,
  `Sifra` varchar(45) NOT NULL,
  `Ime` varchar(45) NOT NULL,
  `Prezime` varchar(45) NOT NULL,
  `Adresa` varchar(45) NOT NULL,
  `idGra` int unsigned NOT NULL,
  `novac` double unsigned NOT NULL,
  PRIMARY KEY (`idKor`),
  UNIQUE KEY `kime_UNIQUE` (`Kime`),
  KEY `idGra-Grad-Korisnik_idx` (`idGra`),
  CONSTRAINT `idGra-Grad-Korisnik` FOREIGN KEY (`idGra`) REFERENCES `grad` (`idGra`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `korisnik`
--

LOCK TABLES `korisnik` WRITE;
/*!40000 ALTER TABLE `korisnik` DISABLE KEYS */;
INSERT INTO `korisnik` VALUES (1,'korisnik1','123','Anja','Curic','Kralja Aleksandra 73',1,115450),(2,'korisnik2','123','Vanja','Curic','Knez Mihajlova,10',25,40920.89),(3,'korisnik3','123','Petar','Petrovic','Trebinjska 8',1,118280),(7,'admin','admin','admin','admin','Tetovska,3',1,5720),(12,'ana_petrovic','ana123','Ana','Petrovic','Ustanicka,3',2,14500),(13,'petar_petrovic','123','Petar','Petrovic','Beogradska 4',28,22030),(14,'maja','123','Maja','Majic','Tetovska,3',1,19300);
/*!40000 ALTER TABLE `korisnik` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `narudzbina`
--

DROP TABLE IF EXISTS `narudzbina`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `narudzbina` (
  `idNar` int unsigned NOT NULL AUTO_INCREMENT,
  `UkupnaCijena` double unsigned NOT NULL,
  `Vrijeme` timestamp NOT NULL,
  `Adresa` varchar(45) NOT NULL,
  `idGra` int unsigned NOT NULL,
  `idKor` int unsigned NOT NULL,
  PRIMARY KEY (`idNar`),
  KEY `idGra-Grad-Narudzbina_idx` (`idGra`),
  KEY `idKor-Korisnik-Narudzbina_idx` (`idKor`),
  CONSTRAINT `idGra-Grad-Narudzbina` FOREIGN KEY (`idGra`) REFERENCES `grad` (`idGra`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `idKor-Korisnik-Narudzbina` FOREIGN KEY (`idKor`) REFERENCES `korisnik` (`idKor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `narudzbina`
--

LOCK TABLES `narudzbina` WRITE;
/*!40000 ALTER TABLE `narudzbina` DISABLE KEYS */;
INSERT INTO `narudzbina` VALUES (3,6120,'2023-02-18 23:34:10','Banjicka 3',1,1),(4,36000,'2023-02-19 11:52:39','Ustanicka,3',2,12),(5,4500,'2023-02-19 11:56:06','Ustanicka,3',2,12),(6,21400,'2023-02-19 14:43:45','Beogradska 4',28,13),(7,7020,'2023-02-19 09:28:51','Beogradska 4',28,13),(8,720,'2023-02-19 17:49:28','Trebinjska 8',1,3),(9,5700,'2023-02-19 18:25:12','Tetovska,3',1,14);
/*!40000 ALTER TABLE `narudzbina` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proizvod`
--

DROP TABLE IF EXISTS `proizvod`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proizvod` (
  `idPro` int unsigned NOT NULL,
  `Naziv` varchar(45) NOT NULL,
  `Cijena` double unsigned NOT NULL,
  `Popust` double unsigned NOT NULL,
  PRIMARY KEY (`idPro`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proizvod`
--

LOCK TABLES `proizvod` WRITE;
/*!40000 ALTER TABLE `proizvod` DISABLE KEYS */;
INSERT INTO `proizvod` VALUES (1,'AirMax',25000,10),(2,'Majica na pruge',1500,0),(3,'Kratka haljina',3000,50),(4,'Pantalone',2000,5),(6,'Patike AllStar',2200,20),(9,'Cargo pantalone',3900,10),(11,'Pidzama na kratki rukav',1200,10),(12,'Svilene pantalone XL',1950,10),(15,'Mrezaste carape',400,10);
/*!40000 ALTER TABLE `proizvod` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stavka`
--

DROP TABLE IF EXISTS `stavka`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stavka` (
  `idSta` int unsigned NOT NULL AUTO_INCREMENT,
  `Kolicina` int unsigned NOT NULL,
  `jedinicnaCijena` double unsigned NOT NULL,
  `idNar` int unsigned NOT NULL,
  `idPro` int unsigned NOT NULL,
  PRIMARY KEY (`idSta`),
  KEY `idNar-Narudzbina-Stavka_idx` (`idNar`),
  KEY `idPro-Proizvod-Stavka_idx` (`idPro`),
  CONSTRAINT `idNar-Narudzbina-Stavka` FOREIGN KEY (`idNar`) REFERENCES `narudzbina` (`idNar`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `idPro-Proizvod-Stavka` FOREIGN KEY (`idPro`) REFERENCES `proizvod` (`idPro`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stavka`
--

LOCK TABLES `stavka` WRITE;
/*!40000 ALTER TABLE `stavka` DISABLE KEYS */;
INSERT INTO `stavka` VALUES (1,3,2100,3,6),(2,1,1200,3,11),(3,2,18000,4,1),(4,2,1500,5,2),(5,1,1500,5,3),(6,1,18000,6,1),(7,1,1500,6,3),(8,1,1900,6,12),(9,2,3510,7,9),(10,2,360,8,15),(11,2,22500,9,1),(12,3,1900,9,4);
/*!40000 ALTER TABLE `stavka` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transakcija`
--

DROP TABLE IF EXISTS `transakcija`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transakcija` (
  `idNar` int unsigned NOT NULL,
  `Suma` double unsigned NOT NULL,
  `Vrijeme` timestamp NOT NULL,
  PRIMARY KEY (`idNar`),
  CONSTRAINT `idNar-Narudzbina-Transakcija` FOREIGN KEY (`idNar`) REFERENCES `narudzbina` (`idNar`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transakcija`
--

LOCK TABLES `transakcija` WRITE;
/*!40000 ALTER TABLE `transakcija` DISABLE KEYS */;
INSERT INTO `transakcija` VALUES (3,6120,'2023-02-18 23:34:10'),(4,36000,'2023-02-19 11:52:40'),(5,4500,'2023-02-19 11:56:06'),(6,21400,'2023-02-19 14:43:45'),(7,7020,'2023-02-19 09:28:51'),(8,720,'2023-02-19 17:49:28'),(9,5700,'2023-02-19 18:25:12');
/*!40000 ALTER TABLE `transakcija` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-19 20:02:16

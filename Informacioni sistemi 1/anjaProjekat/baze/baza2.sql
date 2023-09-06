CREATE DATABASE  IF NOT EXISTS `podsistem2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `podsistem2`;
-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: podsistem2
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
-- Table structure for table `kategorija`
--

DROP TABLE IF EXISTS `kategorija`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kategorija` (
  `idKat` int unsigned NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(45) NOT NULL,
  `idKatNad` int unsigned DEFAULT NULL,
  PRIMARY KEY (`idKat`),
  KEY `PK-Kategorija-Kategorija_idx` (`idKatNad`),
  CONSTRAINT `kat-kat` FOREIGN KEY (`idKatNad`) REFERENCES `kategorija` (`idKat`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kategorija`
--

LOCK TABLES `kategorija` WRITE;
/*!40000 ALTER TABLE `kategorija` DISABLE KEYS */;
INSERT INTO `kategorija` VALUES (1,'Odjeca',1),(2,'Obuca',2),(3,'Nakit',3),(4,'Sminka',4),(5,'Majice i topovi',1),(6,'Pantalone',1),(7,'Patike',2),(8,'Sorcevi',1),(9,'Ogrlica',3),(10,'Haljine',1),(11,'Suknje',1),(14,'Cizme',2),(15,'Helanke',6),(16,'Suncane naocare',NULL),(17,'Carape',NULL);
/*!40000 ALTER TABLE `kategorija` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `korisnik`
--

DROP TABLE IF EXISTS `korisnik`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `korisnik` (
  `idKor` int unsigned NOT NULL AUTO_INCREMENT,
  `kIme` varchar(45) NOT NULL,
  `Sifra` varchar(45) NOT NULL,
  `Ime` varchar(45) NOT NULL,
  `Prezime` varchar(45) NOT NULL,
  `Novac` double unsigned NOT NULL,
  PRIMARY KEY (`idKor`),
  UNIQUE KEY `kIme_UNIQUE` (`kIme`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `korisnik`
--

LOCK TABLES `korisnik` WRITE;
/*!40000 ALTER TABLE `korisnik` DISABLE KEYS */;
INSERT INTO `korisnik` VALUES (1,'korisnik1','123','Anja','Curic',115450),(2,'korisnik2','123','Vanja','Curic',40920.89),(3,'korisnik3','123','Petar','Petrovic',118280),(7,'admin','admin','admin','admin',5720),(19,'ana_petrovic','ana123','Ana','Petrovic',14500),(20,'petar_petrovic','123','Petar','Petrovic',22030),(21,'maja','123','Maja','Majic',19300);
/*!40000 ALTER TABLE `korisnik` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `korpa`
--

DROP TABLE IF EXISTS `korpa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `korpa` (
  `idKor` int unsigned NOT NULL,
  `UkupnaCijena` double NOT NULL,
  PRIMARY KEY (`idKor`),
  CONSTRAINT `idKor_Korpa_Korisnik` FOREIGN KEY (`idKor`) REFERENCES `korisnik` (`idKor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `korpa`
--

LOCK TABLES `korpa` WRITE;
/*!40000 ALTER TABLE `korpa` DISABLE KEYS */;
INSERT INTO `korpa` VALUES (1,0),(2,8000),(3,0),(7,45000),(19,0),(20,0),(21,0);
/*!40000 ALTER TABLE `korpa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prodaje`
--

DROP TABLE IF EXISTS `prodaje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prodaje` (
  `idPro` int unsigned NOT NULL,
  `idKor` int unsigned NOT NULL,
  PRIMARY KEY (`idPro`),
  KEY `idPro-Korisnik-Prodaje_idx` (`idKor`),
  CONSTRAINT `idPro-Korisnik-Prodaje` FOREIGN KEY (`idKor`) REFERENCES `korisnik` (`idKor`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `idPro-Proizvod-Prodaje` FOREIGN KEY (`idPro`) REFERENCES `proizvod` (`idPro`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prodaje`
--

LOCK TABLES `prodaje` WRITE;
/*!40000 ALTER TABLE `prodaje` DISABLE KEYS */;
INSERT INTO `prodaje` VALUES (2,1),(3,1),(4,1),(9,1),(12,1),(6,2),(1,3),(5,3),(11,7),(13,7),(14,7),(15,7);
/*!40000 ALTER TABLE `prodaje` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proizvod`
--

DROP TABLE IF EXISTS `proizvod`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proizvod` (
  `idPro` int unsigned NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(45) NOT NULL,
  `Opis` varchar(45) DEFAULT NULL,
  `Cijena` double unsigned NOT NULL,
  `Popust` double unsigned DEFAULT NULL,
  `idKat` int unsigned DEFAULT NULL,
  PRIMARY KEY (`idPro`),
  KEY `kat-pro_idx` (`idKat`),
  CONSTRAINT `kat-pro` FOREIGN KEY (`idKat`) REFERENCES `kategorija` (`idKat`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proizvod`
--

LOCK TABLES `proizvod` WRITE;
/*!40000 ALTER TABLE `proizvod` DISABLE KEYS */;
INSERT INTO `proizvod` VALUES (1,'AirMax','Nike patike',25000,10,7),(2,'Majica na pruge',NULL,1500,0,5),(3,'Kratka haljina','Zara haljina',3000,50,10),(4,'Pantalone','XL,boja:smedja',2000,5,6),(5,'Dugi kaput','L,boja:crna,Stradivarius',6000,0,1),(6,'Patike AllStar','br.39',2200,20,7),(9,'Cargo pantalone','Smedje pantalone, velicina M.',3900,10,6),(11,'Pidzama na kratki rukav','Velicina S',1200,10,1),(12,'Svilene pantalone XL','Plava boja,zenske',1950,10,6),(13,'Zeleni teksas sorc',NULL,900,10,8),(14,'Naocare za sunce smedje',NULL,3000,0,16),(15,'Mrezaste carape','crne boje',400,10,17);
/*!40000 ALTER TABLE `proizvod` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recenzija`
--

DROP TABLE IF EXISTS `recenzija`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recenzija` (
  `idRec` int unsigned NOT NULL AUTO_INCREMENT,
  `Ocjena` int NOT NULL,
  `Opis` varchar(45) DEFAULT NULL,
  `idPro` int unsigned NOT NULL,
  PRIMARY KEY (`idRec`),
  KEY `idPro-Recenzija-Proizvod_idx` (`idPro`),
  CONSTRAINT `idPro-Recenzija-Proizvod` FOREIGN KEY (`idPro`) REFERENCES `proizvod` (`idPro`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recenzija`
--

LOCK TABLES `recenzija` WRITE;
/*!40000 ALTER TABLE `recenzija` DISABLE KEYS */;
INSERT INTO `recenzija` VALUES (1,9,'Super!',3);
/*!40000 ALTER TABLE `recenzija` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `se_nalazi`
--

DROP TABLE IF EXISTS `se_nalazi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `se_nalazi` (
  `idPro` int unsigned NOT NULL,
  `idKor` int unsigned NOT NULL,
  `Kolicina` int unsigned NOT NULL,
  PRIMARY KEY (`idPro`,`idKor`),
  KEY `idKor-Korisnik-Se_nalazi_idx` (`idKor`),
  CONSTRAINT `idKor-Korisnik-Se_nalazi` FOREIGN KEY (`idKor`) REFERENCES `korisnik` (`idKor`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `idPro-Se_nalazi-Proizvod` FOREIGN KEY (`idPro`) REFERENCES `proizvod` (`idPro`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `se_nalazi`
--

LOCK TABLES `se_nalazi` WRITE;
/*!40000 ALTER TABLE `se_nalazi` DISABLE KEYS */;
INSERT INTO `se_nalazi` VALUES (1,7,2),(4,2,1),(5,2,1);
/*!40000 ALTER TABLE `se_nalazi` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-19 20:01:43

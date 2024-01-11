-- MySQL dump 10.13  Distrib 8.1.0, for macos13.3 (x86_64)
--
-- Host: localhost    Database: CustomDatabase
-- ------------------------------------------------------
-- Server version	8.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `languageId` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `categoryId` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (19,'DE','Smartphones',0),(20,'DE','Fernseher',1),(21,'DE','Smartwatches',2),(22,'DE','Zubehör',3),(23,'DE','Computer',4),(24,'EN','Tvs',1),(25,'EN','Computer',4),(26,'EN','Smartphones',0),(27,'EN','Smartwatches',2),(28,'EN','Accessories',3);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detailTranslation`
--

DROP TABLE IF EXISTS `detailTranslation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detailTranslation` (
  `translationId` int NOT NULL AUTO_INCREMENT,
  `productId` int NOT NULL,
  `languageId` varchar(45) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`translationId`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detailTranslation`
--

LOCK TABLES `detailTranslation` WRITE;
/*!40000 ALTER TABLE `detailTranslation` DISABLE KEYS */;
INSERT INTO `detailTranslation` VALUES (3,3,'DE','Größe','16.4 cm (6.5 inches)'),(4,3,'DE','Auflösung','2,400 x 1,080 pixels (Full HD+)'),(5,3,'DE','Prozessor','Octa-Core (2x2.3 GHz + 6x1.7 GHz)'),(6,3,'DE','RAM','4 GB'),(7,3,'DE','Speicher','128 GB (expandable with microSD card)'),(8,3,'DE','Betriebssystem','Android 10 with One UI 2.5'),(9,3,'DE','Kamera','48 MP (Main camera), 12 MP (Ultra-wide camera), 5 MP (Macro camera), 5 MP (Depth sensor)'),(10,3,'DE','Frontkamera','32 MP'),(11,3,'DE','Akku','4,000 mAh with fast charging'),(12,3,'DE','Farben','Black, White, Blue, Pink'),(13,3,'DE','Besonderheiten','In-Display fingerprint sensor, Face recognition, Dual-SIM'),(14,3,'EN','Size','16.4 cm (6.5 inches)'),(15,3,'EN','Resolution','2,400 x 1,080 pixels (Full HD+)'),(16,3,'EN','Processor','Octa-Core (2x2.3 GHz + 6x1.7 GHz)'),(17,3,'EN','RAM','4 GB'),(18,3,'EN','Storage','128 GB (expandable with microSD card)'),(19,3,'EN','Operating System','Android 10 with One UI 2.5'),(20,3,'EN','Camera','48 MP (Main camera), 12 MP (Ultra-wide camera), 5 MP (Macro camera), 5 MP (Depth sensor)'),(21,3,'EN','Front Camera','32 MP'),(22,3,'EN','Battery','4,000 mAh with fast charging'),(23,3,'EN','Colors','Black, White, Blue, Pink'),(24,3,'EN','Special Features','In-Display fingerprint sensor, Face recognition, Dual-SIM'),(25,12,'DE','Modell','Unspezifisch'),(26,12,'DE','Displaygröße','15,5 cm (6,1)'),(27,12,'DE','Interner Speicher','64 GB'),(28,12,'DE','SIM-Karten','2 (Dual-SIM)'),(29,12,'DE','Rückkamera','Dual'),(30,12,'DE','Betriebssystem','Apple iOS'),(31,12,'EN','Model','Unspecified'),(32,12,'EN','Display Size','15.5 cm (6.1 inches)'),(33,12,'EN','Internal Storage','64 GB'),(34,12,'EN','SIM Cards','2 (Dual-SIM)'),(35,12,'EN','Rear Camera','Dual'),(36,12,'EN','Operating System','Apple iOS'),(37,45,'DE','Modell','Unspezifisch'),(38,45,'DE','Displaygröße','16,7 cm (6,6 Zoll)'),(39,45,'DE','Interner Speicher','128 GB'),(40,45,'DE','SIM-Karten','1 (Single-SIM)'),(41,45,'DE','Rückkamera','Triple'),(42,45,'DE','Betriebssystem','Android'),(43,45,'EN','Model','Unspecified'),(44,45,'EN','Display Size','16.7 cm (6.6 inches)'),(45,45,'EN','Internal Storage','128 GB'),(46,45,'EN','SIM Cards','1 (Single-SIM)'),(47,45,'EN','Rear Camera','Triple'),(48,45,'EN','Operating System','Android'),(49,77,'DE','Modell','Unspezifisch'),(50,77,'DE','Displaygröße','16,3 cm (6,4 Zoll)'),(51,77,'DE','Interner Speicher','256 GB'),(52,77,'DE','SIM-Karten','1 (eSIM)'),(53,77,'DE','Rückkamera','Dual'),(54,77,'DE','Betriebssystem','Android'),(55,77,'EN','Model','Unspecified'),(56,77,'EN','Display Size','16.3 cm (6.4 inches)'),(57,77,'EN','Internal Storage','256 GB'),(58,77,'EN','SIM Cards','1 (eSIM)'),(59,77,'EN','Rear Camera','Dual'),(60,77,'EN','Operating System','Android'),(62,3,'ES','Nombre','Samsung Galaxy A51'),(64,3,'ES','Tamaño','16,4 cm (6,5 pulgadas)'),(65,3,'ES','Resolución','2.400 x 1.080 (Full HD+)'),(66,3,'ES','Procesador','Octa-Core (2x2,3 GHz + 6x1,7 GHz)'),(67,3,'ES','RAM','4 GB'),(68,3,'ES','Almacenamiento','128 GB (ampliable con tarjeta microSD)'),(69,3,'ES','Sistema Operativo','Android 10 con One UI 2.5'),(70,3,'ES','Cámara','48 MP (Cámara principal), 12 MP (Cámara ultra gran angular), 5 MP (Cámara macro), 5 MP (Sensor de profundidad)'),(71,3,'ES','Cámara Frontal','32 MP'),(72,3,'ES','Batería','4.000 mAh con carga rápida'),(73,3,'ES','Colores','Negro, Blanco, Azul, Rosa'),(74,3,'ES','Características Especiales','Sensor de huellas dactilares en pantalla, Reconocimiento facial, Doble SIM');
/*!40000 ALTER TABLE `detailTranslation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flags`
--

DROP TABLE IF EXISTS `flags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flags` (
  `languageId` varchar(45) NOT NULL,
  `flagUrl` varchar(255) NOT NULL,
  PRIMARY KEY (`languageId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flags`
--

LOCK TABLES `flags` WRITE;
/*!40000 ALTER TABLE `flags` DISABLE KEYS */;
INSERT INTO `flags` VALUES ('DE','https://upload.wikimedia.org/wikipedia/en/thumb/b/ba/Flag_of_Germany.svg/1200px-Flag_of_Germany.svg.png'),('EN','https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Flag_of_the_United_Kingdom_%283-5%29.svg/1280px-Flag_of_the_United_Kingdom_%283-5%29.svg.png'),('ES','https://upload.wikimedia.org/wikipedia/commons/thumb/8/89/Bandera_de_Espa%C3%B1a.svg/1200px-Bandera_de_Espa%C3%B1a.svg.png');
/*!40000 ALTER TABLE `flags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `languageTranslation`
--

DROP TABLE IF EXISTS `languageTranslation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `languageTranslation` (
  `id` varchar(45) NOT NULL,
  `language` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`,`language`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `languageTranslation`
--

LOCK TABLES `languageTranslation` WRITE;
/*!40000 ALTER TABLE `languageTranslation` DISABLE KEYS */;
INSERT INTO `languageTranslation` VALUES ('DE','DE','Deutsch'),('DE','EN','Englisch'),('EN','DE','German'),('EN','EN','English'),('EN','ES','Spanish');
/*!40000 ALTER TABLE `languageTranslation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `imageUrl` varchar(255) DEFAULT NULL,
  `categoryId` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (3,'https://cls-smartphone.de/pub/media/catalog/category/samsung-galaxy-a51-prism-crush-black-vorne-und-hinten.png',0),(12,'https://www.telekom.de/resources/images/625382/apple-iphone-12-green-position-2.png',0),(45,'https://www.ednt.de/isotope/s/samsung-galaxy-s21-5g-phantom-gray-vorne-und-hinten.png',0),(77,'https://www.finder.com.au/finder-au/wp-uploads/2021/10/Google_Pixel_6_500x500.png',0);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productTranslation`
--

DROP TABLE IF EXISTS `productTranslation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productTranslation` (
  `productId` int NOT NULL,
  `languageId` varchar(45) NOT NULL,
  `displayName` varchar(45) NOT NULL,
  `displayPrice` varchar(45) NOT NULL,
  PRIMARY KEY (`productId`,`languageId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productTranslation`
--

LOCK TABLES `productTranslation` WRITE;
/*!40000 ALTER TABLE `productTranslation` DISABLE KEYS */;
INSERT INTO `productTranslation` VALUES (3,'DE','Samsung Galaxy A51','257,00€'),(3,'EN','Samsung Galaxy A51','$299.00'),(12,'DE','Apple iPhone 12','963,00€'),(12,'EN','Apple iPhone 12','$999.00'),(45,'DE','Samsung Galaxy S21','849,00€'),(45,'EN','Samsung Galaxy S21','$899.00'),(77,'DE','Google Pixel 6','799,00€'),(77,'EN','Google Pixel 6','$799.00');
/*!40000 ALTER TABLE `productTranslation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-11 18:51:42

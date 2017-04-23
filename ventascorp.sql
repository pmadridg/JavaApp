-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 23, 2017 at 10:25 PM
-- Server version: 5.6.12-log
-- PHP Version: 5.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `ventascorp`
--
CREATE DATABASE IF NOT EXISTS `ventascorp` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `ventascorp`;

-- --------------------------------------------------------

--
-- Table structure for table `tb_cliente`
--

CREATE TABLE IF NOT EXISTS `tb_cliente` (
  `cli_ruc` char(11) NOT NULL,
  `cli_raz` varchar(35) NOT NULL,
  `cli_dir` varchar(45) NOT NULL,
  PRIMARY KEY (`cli_ruc`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_cliente`
--

INSERT INTO `tb_cliente` (`cli_ruc`, `cli_raz`, `cli_dir`) VALUES
('20100134455', 'INSTITUTO SAN IGNACIO DE LOYOLA', 'Av. la Fontana Nro. 955'),
('20545739284', 'CIBERTEC PERU S.A.C.', 'Av. 28 de Julio Nro. 1044 Int. 301');

-- --------------------------------------------------------

--
-- Table structure for table `tb_detalle_factura`
--

CREATE TABLE IF NOT EXISTS `tb_detalle_factura` (
  `fact_id` varchar(10) NOT NULL,
  `pro_cod` varchar(10) NOT NULL,
  `df_qty` int(11) NOT NULL,
  PRIMARY KEY (`fact_id`,`pro_cod`),
  KEY `pro_cod` (`pro_cod`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_detalle_factura`
--

INSERT INTO `tb_detalle_factura` (`fact_id`, `pro_cod`, `df_qty`) VALUES
('001-78454', '789456', 1);

-- --------------------------------------------------------

--
-- Table structure for table `tb_factura`
--

CREATE TABLE IF NOT EXISTS `tb_factura` (
  `fact_id` varchar(10) NOT NULL,
  `fact_fec` date NOT NULL,
  `cli_ruc` char(11) NOT NULL,
  PRIMARY KEY (`fact_id`),
  KEY `cli_ruc` (`cli_ruc`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_factura`
--

INSERT INTO `tb_factura` (`fact_id`, `fact_fec`, `cli_ruc`) VALUES
('001-124456', '2017-04-23', '20100134455'),
('001-78454', '2017-04-04', '20100134455'),
('002-777', '2017-04-23', '20100134455'),
('002-77788', '2017-04-23', '20100134455');

-- --------------------------------------------------------

--
-- Table structure for table `tb_producto`
--

CREATE TABLE IF NOT EXISTS `tb_producto` (
  `pro_cod` varchar(10) NOT NULL,
  `pro_nom` varchar(30) NOT NULL,
  `pro_um` varchar(4) NOT NULL,
  `pro_prec` decimal(10,2) NOT NULL,
  `pro_stock` int(11) NOT NULL,
  PRIMARY KEY (`pro_cod`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_producto`
--

INSERT INTO `tb_producto` (`pro_cod`, `pro_nom`, `pro_um`, `pro_prec`, `pro_stock`) VALUES
('123456', 'AGUA SAN LUIS', 'BTL', '1.50', 15),
('789456', 'GALLETA SODA FIELD', 'UN', '0.60', 10);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tb_detalle_factura`
--
ALTER TABLE `tb_detalle_factura`
  ADD CONSTRAINT `tb_detalle_factura_ibfk_1` FOREIGN KEY (`pro_cod`) REFERENCES `tb_producto` (`pro_cod`),
  ADD CONSTRAINT `tb_detalle_factura_ibfk_2` FOREIGN KEY (`fact_id`) REFERENCES `tb_factura` (`fact_id`);

--
-- Constraints for table `tb_factura`
--
ALTER TABLE `tb_factura`
  ADD CONSTRAINT `tb_factura_ibfk_1` FOREIGN KEY (`cli_ruc`) REFERENCES `tb_cliente` (`cli_ruc`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

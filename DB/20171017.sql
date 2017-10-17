/*
SQLyog Ultimate v12.4.1 (64 bit)
MySQL - 10.1.25-MariaDB : Database - shirley
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`shirley` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `shirley`;

/*Table structure for table `carrito` */

DROP TABLE IF EXISTS `carrito`;

CREATE TABLE `carrito` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cliente_id` int(11) NOT NULL,
  `producto_id` int(11) NOT NULL,
  `precio` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `carrito_fk_1` (`cliente_id`),
  KEY `carrito_fk_2` (`producto_id`),
  CONSTRAINT `carrito_fk_1` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `carrito_fk_2` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `carrito` */

/*Table structure for table `categoria` */

DROP TABLE IF EXISTS `categoria`;

CREATE TABLE `categoria` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_name` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `categoria` */

insert  into `categoria`(`id`,`nombre`) values 
(2,'Joyeria'),
(3,'Oro'),
(4,'Plata');

/*Table structure for table `cliente` */

DROP TABLE IF EXISTS `cliente`;

CREATE TABLE `cliente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) COLLATE utf8_bin NOT NULL,
  `email` varchar(200) COLLATE utf8_bin NOT NULL,
  `password` text COLLATE utf8_bin NOT NULL,
  `telefono` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_CLIENT_EMAIL` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `cliente` */

insert  into `cliente`(`id`,`nombre`,`email`,`password`,`telefono`,`activo`) values 
(1,'Javier Batres','me@fjbatresv.com','fddc06f29e95571fad58983ca9aa223da58303ac','42491978',1);

/*Table structure for table `detalle_transaccion` */

DROP TABLE IF EXISTS `detalle_transaccion`;

CREATE TABLE `detalle_transaccion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `transaccion_id` int(11) NOT NULL,
  `producto_id` int(11) NOT NULL,
  `precio` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `detalle_transaccion_fk_1` (`transaccion_id`),
  KEY `detalle_transaccion_fk_2` (`producto_id`),
  CONSTRAINT `detalle_transaccion_fk_1` FOREIGN KEY (`transaccion_id`) REFERENCES `transaccion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `detalle_transaccion_fk_2` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `detalle_transaccion` */

insert  into `detalle_transaccion`(`id`,`transaccion_id`,`producto_id`,`precio`) values 
(6,4,2,5000),
(7,5,2,5000);

/*Table structure for table `estado_transaccion` */

DROP TABLE IF EXISTS `estado_transaccion`;

CREATE TABLE `estado_transaccion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `estado_transaccion` */

insert  into `estado_transaccion`(`id`,`nombre`) values 
(1,'Nueva'),
(3,'Pagada'),
(4,'Enviada'),
(5,'Entregada');

/*Table structure for table `existencia` */

DROP TABLE IF EXISTS `existencia`;

CREATE TABLE `existencia` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `producto_id` int(11) NOT NULL,
  `cantidad` double NOT NULL DEFAULT '0',
  `costo` double NOT NULL DEFAULT '0',
  `fecha_hora` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `existencia_fk_1` (`producto_id`),
  CONSTRAINT `existencia_fk_1` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `existencia` */

insert  into `existencia`(`id`,`producto_id`,`cantidad`,`costo`,`fecha_hora`) values 
(1,2,20,3000,'2017-10-12 00:30:27'),
(2,2,10,2500,'2017-10-12 00:36:55'),
(3,2,10,3500,'2017-10-12 19:29:50');

/*Table structure for table `flujo_transaccion` */

DROP TABLE IF EXISTS `flujo_transaccion`;

CREATE TABLE `flujo_transaccion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `transaccion_id` int(11) NOT NULL,
  `estado_id` int(11) NOT NULL,
  `comentario` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `flujo_transaccion_fk_1` (`transaccion_id`),
  KEY `flujo_transaccion_fk_2` (`estado_id`),
  CONSTRAINT `flujo_transaccion_fk_1` FOREIGN KEY (`transaccion_id`) REFERENCES `transaccion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `flujo_transaccion_fk_2` FOREIGN KEY (`estado_id`) REFERENCES `estado_transaccion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `flujo_transaccion` */

insert  into `flujo_transaccion`(`id`,`transaccion_id`,`estado_id`,`comentario`) values 
(1,4,1,'Orden nueva'),
(2,4,3,'Mon Oct 16 23:52:45 CST 2017 | Por el usuario: 4'),
(3,4,4,'Mon Oct 16 23:59:42 CST 2017 | Por el usuario: 4'),
(4,4,5,'Mon Oct 16 23:59:46 CST 2017 | Por el usuario: 4'),
(5,5,1,'Tue Oct 17 00:00:40 CST 2017'),
(6,5,3,'Tue Oct 17 00:01:17 CST 2017 | Por el usuario: 4'),
(7,5,4,'Tue Oct 17 00:01:20 CST 2017 | Por el usuario: 4'),
(8,5,5,'Tue Oct 17 00:01:27 CST 2017 | Por el usuario: 4');

/*Table structure for table `menu` */

DROP TABLE IF EXISTS `menu`;

CREATE TABLE `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) COLLATE utf8_bin NOT NULL,
  `path` varchar(200) COLLATE utf8_bin NOT NULL,
  `icono` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `menu` */

insert  into `menu`(`id`,`nombre`,`path`,`icono`) values 
(1,'Inicio','/','dashboard'),
(2,'Seguridad','/seguridad','security'),
(3,'Catalogos','/catalogos','folder'),
(4,'Procesos','/procesos','compare_arrows');

/*Table structure for table `producto` */

DROP TABLE IF EXISTS `producto`;

CREATE TABLE `producto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) COLLATE utf8_bin NOT NULL,
  `codigo` varchar(200) COLLATE utf8_bin NOT NULL,
  `precio` double NOT NULL DEFAULT '0',
  `foto` text COLLATE utf8_bin,
  PRIMARY KEY (`id`),
  UNIQUE KEY `CODE_PRODUCT` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `producto` */

insert  into `producto`(`id`,`nombre`,`codigo`,`precio`,`foto`) values 
(2,'Collar CHCH','0001',5000,'https://2f71c.https.cdn.softlayer.net/802F71C/1ahonu4ge.chcarolinaherrera.com/wcscontent/photos/CH/ATMP/AA/CA/AACA08RI07144/1500/AACA08RI07144_02.jpg');

/*Table structure for table `producto_categoria` */

DROP TABLE IF EXISTS `producto_categoria`;

CREATE TABLE `producto_categoria` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `producto_id` int(11) NOT NULL,
  `categoria_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `producto_categoria_fk_1` (`producto_id`),
  KEY `producto_categoria_fk_2` (`categoria_id`),
  CONSTRAINT `producto_categoria_fk_1` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `producto_categoria_fk_2` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `producto_categoria` */

insert  into `producto_categoria`(`id`,`producto_id`,`categoria_id`) values 
(4,2,4),
(5,2,2);

/*Table structure for table `rol` */

DROP TABLE IF EXISTS `rol`;

CREATE TABLE `rol` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `rol` */

insert  into `rol`(`id`,`nombre`) values 
(3,'Administrador');

/*Table structure for table `rol_menu` */

DROP TABLE IF EXISTS `rol_menu`;

CREATE TABLE `rol_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rol_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `rol_menu_fk_1` (`rol_id`),
  KEY `rol_menu_fk_2` (`menu_id`),
  CONSTRAINT `rol_menu_fk_1` FOREIGN KEY (`rol_id`) REFERENCES `rol` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rol_menu_fk_2` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `rol_menu` */

insert  into `rol_menu`(`id`,`rol_id`,`menu_id`) values 
(11,3,3),
(12,3,1),
(13,3,2),
(14,3,4);

/*Table structure for table `transaccion` */

DROP TABLE IF EXISTS `transaccion`;

CREATE TABLE `transaccion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cliente_id` int(11) NOT NULL,
  `destino` varchar(250) COLLATE utf8_bin NOT NULL,
  `estado_id` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `transaccion_estado_fk_1` (`estado_id`),
  KEY `transaccion_cliente_fk_1` (`cliente_id`),
  CONSTRAINT `transaccion_cliente_fk_1` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `transaccion_estado_fk_1` FOREIGN KEY (`estado_id`) REFERENCES `estado_transaccion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `transaccion` */

insert  into `transaccion`(`id`,`cliente_id`,`destino`,`estado_id`) values 
(4,1,'9na avenida 13-00 zona 12',5),
(5,1,'Guatemala',5);

/*Table structure for table `usuario` */

DROP TABLE IF EXISTS `usuario`;

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) COLLATE utf8_bin NOT NULL,
  `email` varchar(200) COLLATE utf8_bin NOT NULL,
  `password` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `usuario` */

insert  into `usuario`(`id`,`nombre`,`email`,`password`) values 
(4,'Javier Batres','fjbatresv@gmail.com','fddc06f29e95571fad58983ca9aa223da58303ac'),
(5,'Juan batres','jfbatres71@gmail.com','405372af098e9198127126a1e91a84cac8507de9'),
(6,'Marian','marianravenclaw@gmail.com','7f28b3d79a9b3747b1879dd76ab933d093baf28e');

/*Table structure for table `usuario_rol` */

DROP TABLE IF EXISTS `usuario_rol`;

CREATE TABLE `usuario_rol` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `rol_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_rol_fk_1` (`usuario_id`),
  KEY `usuario_rol_fk_2` (`rol_id`),
  CONSTRAINT `usuario_rol_fk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `usuario_rol_fk_2` FOREIGN KEY (`rol_id`) REFERENCES `rol` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `usuario_rol` */

insert  into `usuario_rol`(`id`,`usuario_id`,`rol_id`) values 
(5,4,3),
(6,6,3);

/*Table structure for table `usuario_menu` */

DROP TABLE IF EXISTS `usuario_menu`;

/*!50001 DROP VIEW IF EXISTS `usuario_menu` */;
/*!50001 DROP TABLE IF EXISTS `usuario_menu` */;

/*!50001 CREATE TABLE  `usuario_menu`(
 `id` varchar(36) ,
 `uid` int(11) ,
 `nombre` varchar(200) ,
 `path` varchar(200) ,
 `icono` varchar(100) 
)*/;

/*View structure for view usuario_menu */

/*!50001 DROP TABLE IF EXISTS `usuario_menu` */;
/*!50001 DROP VIEW IF EXISTS `usuario_menu` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `usuario_menu` AS (select uuid() AS `id`,`u`.`id` AS `uid`,`m`.`nombre` AS `nombre`,`m`.`path` AS `path`,`m`.`icono` AS `icono` from ((((`menu` `m` join `rol_menu` `rm` on((`m`.`id` = `rm`.`menu_id`))) join `rol` `r` on((`rm`.`rol_id` = `r`.`id`))) join `usuario_rol` `ur` on((`r`.`id` = `ur`.`rol_id`))) join `usuario` `u` on((`ur`.`usuario_id` = `u`.`id`)))) */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

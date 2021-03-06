-- MySQL Script generated by MySQL Workbench
-- 04/11/16 19:36:52
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema laboIAM
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `laboIAM` ;

-- -----------------------------------------------------
-- Schema laboIAM
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `laboIAM` DEFAULT CHARACTER SET utf8 ;
USE `laboIAM` ;

-- -----------------------------------------------------
-- Table `laboIAM`.`parameter`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `laboIAM`.`parameter` ;

CREATE TABLE IF NOT EXISTS `laboIAM`.`parameter` (
  `key` VARCHAR(64) NOT NULL,
  `param` VARCHAR(64) NULL,
  UNIQUE INDEX `key_UNIQUE` (`key` ASC),
  PRIMARY KEY (`key`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `laboIAM`.`room`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `laboIAM`.`room` ;

CREATE TABLE IF NOT EXISTS `laboIAM`.`room` (
  `roomId` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(2048) NULL,
  PRIMARY KEY (`roomId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `laboIAM`.`lightSensorMonitoring`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `laboIAM`.`lightSensorMonitoring` ;

CREATE TABLE IF NOT EXISTS `laboIAM`.`lightSensorMonitoring` (
  `create_time`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
  `outsideLight` INT DEFAULT 0,
  `insideLight` INT DEFAULT 0,
  `roomId` INT NOT NULL,
  PRIMARY KEY (`roomId`),
  CONSTRAINT `roomId1`
    FOREIGN KEY (`roomId`)
    REFERENCES `laboIAM`.`room` (`roomId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `laboIAM`.`temperatureSensorMonitoring`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `laboIAM`.`temperatureSensorMonitoring` ;

CREATE TABLE IF NOT EXISTS `laboIAM`.`temperatureSensorMonitoring` (
  `create_time`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
  `temperature` INT DEFAULT 0,
  `roomId` INT NOT NULL,
  `heatingState` TINYINT(1) DEFAULT 0,
  INDEX `roomId_idx` (`roomId` ASC),
  CONSTRAINT `roomId2`
    FOREIGN KEY (`roomId`)
    REFERENCES `laboIAM`.`room` (`roomId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `laboIAM`.`spice`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `laboIAM`.`spice` ;

CREATE TABLE IF NOT EXISTS `laboIAM`.`spice` (
  `spiceId` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL DEFAULT 'Not defined',
  `description` VARCHAR(2048) NOT NULL DEFAULT 'Not available',
  `bareCode` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`spiceId`),
  UNIQUE INDEX `bareCode_UNIQUE` (`bareCode` ASC),
  UNIQUE INDEX `spiceId_UNIQUE` (`spiceId` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `laboIAM`.`spiceBox`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `laboIAM`.`spiceBox` ;

CREATE TABLE IF NOT EXISTS `laboIAM`.`spiceBox` (
  `location` INT NOT NULL AUTO_INCREMENT,
  `spiceId` INT NULL,
  PRIMARY KEY (`location`),
  UNIQUE INDEX `location_UNIQUE` (`location` ASC),
  INDEX `spiceId_idx` (`spiceId` ASC),
  CONSTRAINT `spiceId`
    FOREIGN KEY (`spiceId`)
    REFERENCES `laboIAM`.`spice` (`spiceId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `laboIAM`.`action`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `laboIAM`.`action` ;

CREATE TABLE IF NOT EXISTS `laboIAM`.`action` (
  `action` INT NOT NULL AUTO_INCREMENT,
  `label` VARCHAR(255) NOT NULL DEFAULT 'Not Defined',
  `Description` VARCHAR(2048) NOT NULL DEFAULT 'Not Defined',
  PRIMARY KEY (`action`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `laboIAM`.`agenda`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `laboIAM`.`agenda` ;

CREATE TABLE IF NOT EXISTS `laboIAM`.`agenda` (
  `dayId` INT NOT NULL,
  `roomId` INT NOT NULL,
  `action` INT NOT NULL,
  `hours` TIME DEFAULT "00:00",
  CONSTRAINT `roomId3`
    FOREIGN KEY (`roomId`)
    REFERENCES `laboIAM`.`room` (`roomId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `actionId`
    FOREIGN KEY (`action`)
    REFERENCES `laboIAM`.`action` (`action`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `laboIAM` ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

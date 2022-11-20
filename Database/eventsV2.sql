SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema events
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `events` DEFAULT CHARACTER SET utf8 ;
USE `events` ;

-- -----------------------------------------------------
-- Table `events`.`eventcategory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `events`.`eventcategory` (
  `eventCategoryId` INT NOT NULL,
  `eventCategoryName` VARCHAR(100) NOT NULL,
  `eventCategoryDescription` TEXT(500) NULL DEFAULT NULL,
  `eventDuration` INT NOT NULL,
  PRIMARY KEY (`eventCategoryId`),
  UNIQUE INDEX `eventCategoryName_UNIQUE` (`eventCategoryName` ASC) VISIBLE,
  UNIQUE INDEX `eventCategoryId_UNIQUE` (`eventCategoryId` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

INSERT INTO `eventcategory` VALUES
(1,'Project Management Clinic','ตารางนัดหมายนี้ใช้สำหรับนัดหมาย project management clinic ในวิชา INT221 integrated project I ให้นักศึกษาเตรียมเอกสารที่เกี่ยวข้องเพื่อแสดงระหว่างขอคำปรึกษา',30),
(2,'DevOps/Infra Clinic','Use this event category for DevOps/Infra clinic.',20),
(3,'Database Clinic','ตารางนัดหมายนี้ใช้สำหรับนัดหมาย client-side clinic ในวิชา INT221 integrated project I',15),
(4,'Client-side Clinic','ตารางนัดหมายนี้ใช้สำหรับนัดหมาย database clinic ในวิชา INT221 integrated project I',30),
(5,'Server-side Clinic',NULL,30);

-- -----------------------------------------------------
-- Table `events`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `events`.`user` (
  `userId` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `password` VARCHAR(90) NOT NULL,
  `role` ENUM('admin', 'lecturer', 'student') NOT NULL,
  `createdOn` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedOn` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userId`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb3;

INSERT INTO `user` VALUES
(1,'OASIP ADMIN','oasip.admin@kmutt.ac.th','$argon2id$v=19$m=16,t=2,p=2$qrDZk3Row+UNJ3v7+jGn6A$JtKaDD5PLUlq4ToOgZjEPajo/SOYKWN7/KP5Q9k','admin','2022-08-01 00:00:00+07:00','2022-08-01 00:00:00+07:00'),
(2,'Somchai Jaidee','somchai.jai@kmutt.ac.th','$argon2id$v=19$m=16,t=2,p=2$qrDZk3Row+UNJ3v7+jGn6A$JtKaDD5PLUlq4ToOgZjEPajo/SOYKWN7/KP5Q9k','lecturer','2022-08-08 15:00:00+07:00','2022-08-08 15:00:00+07:00'),
(3,'PBI34 สมส่วน สุขศรี','somsuan.s341@kmutt.ac.th','$argon2id$v=19$m=16,t=2,p=2$qrDZk3Row+UNJ3v7+jGn6A$JtKaDD5PLUlq4ToOgZjEPajo/SOYKWN7/KP5Q9k','lecturer','2022-08-08 15:00:00+07:00','2022-08-08 15:00:00+07:00');
-- -----------------------------------------------------
-- Table `events`.`event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `events`.`event` (
  `bookingId` INT NOT NULL AUTO_INCREMENT,
  `eventCategoryId` INT NOT NULL,
  `userId` INT NULL,
  `bookingName` VARCHAR(100) NOT NULL,
  `bookingEmail` VARCHAR(45) NOT NULL,
  `eventStartTime` DATETIME NOT NULL,
  `eventDuration` INT NOT NULL,
  `eventNotes` TEXT(500) NULL DEFAULT NULL,
  PRIMARY KEY (`bookingId`),
  INDEX `fk_Event_EventCategory_idx` (`eventCategoryId` ASC) VISIBLE,
  INDEX `fk_Event_User_idx` (`userId` ASC) VISIBLE,
  CONSTRAINT `fk_Event_EventCategory`
    FOREIGN KEY (`eventCategoryId`)
    REFERENCES `events`.`eventcategory` (`eventCategoryId`),
  CONSTRAINT `fk_Event_User`
    FOREIGN KEY (`userId`)
    REFERENCES `events`.`user` (`userId`)
    ON DELETE SET NULL)
ENGINE = InnoDB
AUTO_INCREMENT = 28
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `events`.`CategoryOwner`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `events`.`CategoryOwner` (
  `eventCategoryId` INT NOT NULL,
  `userId` INT NOT NULL,
  PRIMARY KEY (`userId`, `eventCategoryId`),
  INDEX `fk_user_has_eventcategory_eventcategory1_idx` (`eventCategoryId` ASC) VISIBLE,
  INDEX `fk_user_has_eventcategory_user1_idx` (`userId` ASC) VISIBLE,
  CONSTRAINT `fk_user_has_eventcategory_user1`
    FOREIGN KEY (`userId`)
    REFERENCES `events`.`user` (`userId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_eventcategory_eventcategory1`
    FOREIGN KEY (`eventCategoryId`)
    REFERENCES `events`.`eventcategory` (`eventCategoryId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

INSERT INTO `CategoryOwner` VALUES
(1,2),
(2,2),
(3,2),
(1,3);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

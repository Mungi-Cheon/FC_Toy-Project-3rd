-- -----------------------------------------------------
-- Table `toy3travel`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `toy3travel`.`user`
(
    `id`         INT          NOT NULL AUTO_INCREMENT,
    `email`      VARCHAR(100) NOT NULL,
    `username`   VARCHAR(50)  NOT NULL,
    `password`   VARCHAR(100) NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `role`       VARCHAR(20)  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE
)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `toy3travel`.`trip`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `toy3travel`.`trip`
(
    `id`          INT          NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(100) NOT NULL,
    `start_date`  DATE         NOT NULL,
    `end_date`    DATE         NOT NULL,
    `is_domestic` TINYINT(1)   NOT NULL DEFAULT 1,
    `created_at`  DATETIME     NOT NULL,
    `updated_at`  DATETIME              DEFAULT NULL,
    `user_id`     INT          NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_trip_member1_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `fk_trip_member1`
        FOREIGN KEY (`user_id`)
            REFERENCES `toy3travel`.`user` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `toy3travel`.`itinerary`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `toy3travel`.`itinerary`
(
    `id`                                INT          NOT NULL AUTO_INCREMENT,
    `name`                              VARCHAR(100) NOT NULL,
    `trip_id`                           INT          NOT NULL,
    `place`                             VARCHAR(100) NOT NULL,
    `stay_road_address_name`            VARCHAR(100) NOT NULL,
    `stay_start_time`                   DATETIME     NOT NULL,
    `stay_end_time`                     DATETIME     NOT NULL,
    `departure_place`                   VARCHAR(50)  NOT NULL,
    `departure_place_road_address_name` VARCHAR(50)  NOT NULL,
    `destination`                       VARCHAR(50)  NOT NULL,
    `destination_road_address_name`     VARCHAR(50)  NOT NULL,
    `departure_time`                    DATETIME     NOT NULL,
    `arrival_time`                      DATETIME     NOT NULL,
    `transportation`                    VARCHAR(50)  NOT NULL,
    `accommodation`                     VARCHAR(50)  NOT NULL,
    `accommodation_road_address_name`   VARCHAR(50)  NOT NULL,
    `check_in`                          DATETIME     NOT NULL,
    `check_out`                         DATETIME     NOT NULL,
    `created_at`                        DATETIME     NOT NULL,
    `updated_at`                        DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_itinerary_trip_idx` (`trip_id` ASC) VISIBLE,
    CONSTRAINT `fk_itinerary_trip`
        FOREIGN KEY (`trip_id`)
            REFERENCES `toy3travel`.`trip` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `toy3travel`.`like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `toy3travel`.`likes`
(
    `id`      INT NOT NULL AUTO_INCREMENT,
    `trip_id` INT NOT NULL,
    `user_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_like_trip1_idx` (`trip_id` ASC, `user_id` ASC) VISIBLE,
    CONSTRAINT `fk_like_trip1`
        FOREIGN KEY (`trip_id`)
            REFERENCES `toy3travel`.`trip` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `fk_like_user1`
        FOREIGN KEY (`user_id`)
            REFERENCES `toy3travel`.`user` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `toy3travel`.`comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `toy3travel`.`comment`
(
    `id`         INT      NOT NULL AUTO_INCREMENT,
    `trip_id`    INT      NOT NULL,
    `user_id`    INT      NOT NULL,
    `content`    TEXT     NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_comment_trip1_idx` (`trip_id` ASC, `user_id` ASC) VISIBLE,
    CONSTRAINT `fk_comment_trip1`
        FOREIGN KEY (`trip_id`)
            REFERENCES `toy3travel`.`trip` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `fk_comment_user1`
        FOREIGN KEY (`user_id`)
            REFERENCES `toy3travel`.`user` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `toy3travel`.`location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `toy3travel`.`location`
(
    `id`           INT          NOT NULL AUTO_INCREMENT,
    `itinerary_id` INT          NOT NULL,
    `user_id`      INT          NOT NULL,
    `name`         VARCHAR(100) NOT NULL,
    `road_address` VARCHAR(300) NOT NULL,
    `latitude`     FLOAT        NOT NULL,
    `longitude`    FLOAT        NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_location_itinerary_idx` (`itinerary_id` ASC) VISIBLE,
    INDEX `fk_location_user1_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `fk_location_itinerary`
        FOREIGN KEY (`itinerary_id`)
            REFERENCES `toy3travel`.`itinerary` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `fk_location_user1`
        FOREIGN KEY (`user_id`)
            REFERENCES `toy3travel`.`user` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `toy3travel`.`refresh`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `toy3travel`.`refresh`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `email`      VARCHAR(255) NOT NULL,
    `expiration` VARCHAR(255) NOT NULL,
    `refresh`    VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_bin;
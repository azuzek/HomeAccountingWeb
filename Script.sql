--<ScriptOptions statementTerminator=";"/>

ALTER TABLE `ha`.`user` DROP PRIMARY KEY;

ALTER TABLE `ha`.`extract_map_rule` DROP PRIMARY KEY;

ALTER TABLE `ha`.`category` DROP PRIMARY KEY;

ALTER TABLE `ha`.`instrument` DROP PRIMARY KEY;

ALTER TABLE `ha`.`quote` DROP PRIMARY KEY;

ALTER TABLE `ha`.`account` DROP PRIMARY KEY;

ALTER TABLE `ha`.`entry` DROP PRIMARY KEY;

ALTER TABLE `ha`.`bank` DROP PRIMARY KEY;

ALTER TABLE `ha`.`extract` DROP PRIMARY KEY;

ALTER TABLE `ha`.`entry_line` DROP PRIMARY KEY;

ALTER TABLE `ha`.`entry_line` DROP INDEX `ha`.`user_fk_idx`;

ALTER TABLE `ha`.`quote` DROP INDEX `ha`.`quote_instrument_fk_idx`;

ALTER TABLE `ha`.`category` DROP INDEX `ha`.`category_user_fk_idx`;

ALTER TABLE `ha`.`entry_line` DROP INDEX `ha`.`entry_fk_idx`;

ALTER TABLE `ha`.`account` DROP INDEX `ha`.`account_instrument_fk_idx`;

ALTER TABLE `ha`.`account` DROP INDEX `ha`.`account_category_fk_idx`;

ALTER TABLE `ha`.`entry_line` DROP INDEX `ha`.`id_UNIQUE`;

ALTER TABLE `ha`.`account` DROP INDEX `ha`.`account_user_fk_idx`;

ALTER TABLE `ha`.`category` DROP INDEX `ha`.`id_UNIQUE`;

ALTER TABLE `ha`.`bank` DROP INDEX `ha`.`bank_user_fk_idx`;

ALTER TABLE `ha`.`entry` DROP INDEX `ha`.`entry_user_fk_idx`;

ALTER TABLE `ha`.`extract_map_rule` DROP INDEX `ha`.`extract_map_rule_account_fk_idx`;

ALTER TABLE `ha`.`extract_map_rule` DROP INDEX `ha`.`extract_map_rule_user_fk_idx`;

ALTER TABLE `ha`.`entry_line` DROP INDEX `ha`.`account_id_idx`;

ALTER TABLE `ha`.`account` DROP INDEX `ha`.`id_UNIQUE`;

ALTER TABLE `ha`.`extract_map_rule` DROP INDEX `ha`.`extract_map_rule_bank_fk_idx`;

ALTER TABLE `ha`.`user` DROP INDEX `ha`.`id_UNIQUE`;

ALTER TABLE `ha`.`entry` DROP INDEX `ha`.`id_UNIQUE`;

ALTER TABLE `ha`.`category` DROP INDEX `ha`.`category_category_fk_idx`;

ALTER TABLE `ha`.`entry_line` DROP INDEX `ha`.`entry_line_quote_fk_idx`;

ALTER TABLE `ha`.`extract` DROP INDEX `ha`.`extract_user_fk_idx`;

ALTER TABLE `ha`.`extract` DROP INDEX `ha`.`extract_bank_fk_idx`;

DROP TABLE `ha`.`instrument`;

DROP TABLE `ha`.`quote`;

DROP TABLE `ha`.`account`;

DROP TABLE `ha`.`category`;

DROP TABLE `ha`.`bank`;

DROP TABLE `ha`.`entry_line`;

DROP TABLE `ha`.`entry`;

DROP TABLE `ha`.`extract`;

DROP TABLE `ha`.`extract_map_rule`;

DROP TABLE `ha`.`user`;

CREATE TABLE `ha`.`instrument` (
	`id` INTEGER UNSIGNED NOT NULL,
	`name` VARCHAR(45) NOT NULL,
	`code` VARCHAR(5) NOT NULL,
	`type` CHAR(1) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `ha`.`quote` (
	`id` INTEGER UNSIGNED NOT NULL,
	`instrument_id` INTEGER UNSIGNED NOT NULL,
	`type` CHAR(1) NOT NULL,
	`date` DATE NOT NULL,
	`price` DECIMAL(10 , 2) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `ha`.`extract` (
	`id` INTEGER UNSIGNED NOT NULL,
	`user_id` INTEGER UNSIGNED,
	`bank_id` INTEGER UNSIGNED,
	`timestamp` TIMESTAMP,
	`description` VARCHAR(45),
	`amount` DECIMAL(10 , 2),
	`status` CHAR(1),
	PRIMARY KEY (`id`)
);

CREATE TABLE `ha`.`extract_map_rule` (
	`id` INTEGER UNSIGNED NOT NULL,
	`user_id` INTEGER UNSIGNED NOT NULL,
	`bank_id` INTEGER UNSIGNED NOT NULL,
	`regex` VARCHAR(45) NOT NULL,
	`account_id` INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `ha`.`user` (
	`id` INTEGER UNSIGNED NOT NULL,
	`userid` VARCHAR(8) NOT NULL,
	`password` VARCHAR(45) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `ha`.`account` (
	`id` INTEGER UNSIGNED NOT NULL,
	`user_id` INTEGER UNSIGNED,
	`category_id` INTEGER UNSIGNED,
	`name` VARCHAR(20),
	`type` CHAR(1),
	`balance` DECIMAL(10 , 2),
	`active` BIT,
	`local_currency` BIT,
	`instrument_id` INTEGER UNSIGNED,
	PRIMARY KEY (`id`)
);

CREATE TABLE `ha`.`category` (
	`id` INTEGER UNSIGNED NOT NULL,
	`user_id` INTEGER UNSIGNED,
	`parent_id` INTEGER UNSIGNED,
	`type` CHAR(1),
	`fixed` BIT,
	`name` VARCHAR(45) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `ha`.`bank` (
	`id` INTEGER UNSIGNED NOT NULL,
	`user_id` INTEGER UNSIGNED,
	`name` VARCHAR(45),
	PRIMARY KEY (`id`)
);

CREATE TABLE `ha`.`entry_line` (
	`id` INTEGER UNSIGNED NOT NULL,
	`user_id` INTEGER UNSIGNED,
	`entry_id` INTEGER UNSIGNED,
	`account_id` INTEGER UNSIGNED,
	`action` CHAR(1),
	`amount` DECIMAL(10 , 2),
	`quote_id` INTEGER UNSIGNED,
	`quantity` DECIMAL(10 , 2),
	PRIMARY KEY (`id`)
);

CREATE TABLE `ha`.`entry` (
	`id` INTEGER UNSIGNED NOT NULL,
	`user_id` INTEGER UNSIGNED,
	`date` DATE NOT NULL,
	`description` VARCHAR(127),
	PRIMARY KEY (`id`)
);

CREATE INDEX `bank_user_fk_idx` ON `ha`.`bank` (`user_id` ASC);

CREATE INDEX `user_fk_idx` ON `ha`.`entry_line` (`user_id` ASC);

CREATE INDEX `entry_user_fk_idx` ON `ha`.`entry` (`user_id` ASC);

CREATE INDEX `quote_instrument_fk_idx` ON `ha`.`quote` (`instrument_id` ASC);

CREATE INDEX `category_user_fk_idx` ON `ha`.`category` (`user_id` ASC);

CREATE INDEX `extract_map_rule_account_fk_idx` ON `ha`.`extract_map_rule` (`account_id` ASC);

CREATE INDEX `entry_fk_idx` ON `ha`.`entry_line` (`entry_id` ASC);

CREATE INDEX `account_instrument_fk_idx` ON `ha`.`account` (`instrument_id` ASC);

CREATE INDEX `extract_map_rule_user_fk_idx` ON `ha`.`extract_map_rule` (`user_id` ASC);

CREATE INDEX `account_category_fk_idx` ON `ha`.`account` (`category_id` ASC);

CREATE INDEX `account_id_idx` ON `ha`.`entry_line` (`account_id` ASC);

CREATE UNIQUE INDEX `id_UNIQUE` ON `ha`.`account` (`id` ASC);

CREATE INDEX `extract_map_rule_bank_fk_idx` ON `ha`.`extract_map_rule` (`bank_id` ASC);

CREATE UNIQUE INDEX `id_UNIQUE` ON `ha`.`user` (`id` ASC);

CREATE UNIQUE INDEX `id_UNIQUE` ON `ha`.`entry` (`id` ASC);

CREATE UNIQUE INDEX `id_UNIQUE` ON `ha`.`entry_line` (`id` ASC);

CREATE INDEX `account_user_fk_idx` ON `ha`.`account` (`user_id` ASC);

CREATE INDEX `category_category_fk_idx` ON `ha`.`category` (`parent_id` ASC);

CREATE INDEX `entry_line_quote_fk_idx` ON `ha`.`entry_line` (`quote_id` ASC);

CREATE INDEX `extract_user_fk_idx` ON `ha`.`extract` (`user_id` ASC);

CREATE UNIQUE INDEX `id_UNIQUE` ON `ha`.`category` (`id` ASC);

CREATE INDEX `extract_bank_fk_idx` ON `ha`.`extract` (`bank_id` ASC);


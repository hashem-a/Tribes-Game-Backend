alter table players ADD email varchar(255) DEFAULT NULL;
alter table players ADD is_active boolean DEFAULT NULL;

CREATE TABLE `confirmation_token`
(
    `token_id`              bigint NOT NULL AUTO_INCREMENT,
    `token`    varchar(255) DEFAULT NULL,
    `created_date`          date           DEFAULT NULL,
    `player_id`             bigint       DEFAULT NULL,

    FOREIGN KEY (player_id) REFERENCES players (id),
    PRIMARY KEY (`token_id`)
);
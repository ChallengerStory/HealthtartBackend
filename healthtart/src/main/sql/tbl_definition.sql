-- Drop database if exists and create new one
DROP
DATABASE IF EXISTS healthtartdb;
CREATE
DATABASE healthtartdb
    DEFAULT CHARACTER SET UTF8
    DEFAULT COLLATE UTF8_GENERAL_CI;
USE
healthtartdb;

-- Set InnoDB as storage engine
SET SESSION storage_engine = InnoDB;
SET SESSION AUTO_INCREMENT_INCREMENT = 1;

CREATE TABLE gym
(
    gym_code        BIGINT NOT NULL AUTO_INCREMENT,
    gym_name        VARCHAR(255),
    address         VARCHAR(255),
    business_number VARCHAR(255),
    created_at      DATETIME DEFAULT NOW(),
    updated_at      DATETIME DEFAULT NOW(),
    PRIMARY KEY (gym_code)
);

CREATE TABLE users
(
    user_code     VARCHAR(255)         NOT NULL,
    user_type     VARCHAR(255)         NOT NULL,
    user_name     VARCHAR(255),
    user_email    VARCHAR(255),
    user_password VARCHAR(255),
    user_phone    VARCHAR(255),
    user_nickname VARCHAR(255),
    user_address  VARCHAR(255),
    user_flag     BOOLEAN DEFAULT TRUE NOT NULL,
    user_gender   VARCHAR(1) CHECK (user_gender IN ('M', 'F')),
    user_height DOUBLE,
    user_weight DOUBLE,
    user_age      INTEGER,
    provider      VARCHAR(255),
    provider_id   VARCHAR(255),
    created_at    DATETIME             NOT NULL,
    updated_at    DATETIME             NOT NULL,
    gym_code      BIGINT,
    FOREIGN KEY (gym_code) REFERENCES gym (gym_code) ON DELETE SET NULL,
    PRIMARY KEY (user_code)
);

CREATE TABLE routines
(
    routine_code BIGINT NOT NULL AUTO_INCREMENT,
    created_at   DATETIME DEFAULT NOW(),
    updated_at   DATETIME DEFAULT NOW(),
    PRIMARY KEY (routine_code)
);

CREATE TABLE workout_info
(
    workout_info_code BIGINT       NOT NULL AUTO_INCREMENT,
    title             VARCHAR(255) NOT NULL,
    workout_time      INT          NOT NULL,
    recommend_music   VARCHAR(255),
    created_at        DATETIME DEFAULT NOW(),
    updated_at        DATETIME DEFAULT NOW(),
    routine_code      BIGINT,
    FOREIGN KEY (routine_code) REFERENCES routines (routine_code) ON DELETE SET NULL,
    PRIMARY KEY (workout_info_code)
);

CREATE TABLE exercise_equipment
(
    exercise_equipment_code BIGINT NOT NULL AUTO_INCREMENT,
    exercise_equipment_name VARCHAR(255),
    body_part               VARCHAR(255),
    exercise_description    VARCHAR(255) DEFAULT NULL,
    exercise_image          VARCHAR(255) DEFAULT NULL,
    recommended_video       VARCHAR(255) DEFAULT NULL,
    created_at              DATETIME     DEFAULT NOW(),
    updated_at              DATETIME     DEFAULT NOW(),
    PRIMARY KEY (exercise_equipment_code)
);

CREATE TABLE equipment_per_gym
(
    equipment_per_gym_code  BIGINT NOT NULL AUTO_INCREMENT,
    created_at              DATETIME DEFAULT NOW(),
    updated_at              DATETIME DEFAULT NOW(),
    gym_code                BIGINT NOT NULL,
    exercise_equipment_code BIGINT NOT NULL,
    FOREIGN KEY (gym_code) REFERENCES gym (gym_code) ON DELETE CASCADE,
    FOREIGN KEY (exercise_equipment_code) REFERENCES exercise_equipment (exercise_equipment_code) ON DELETE CASCADE,
    PRIMARY KEY (equipment_per_gym_code)
);

CREATE TABLE workout_per_routine
(
    workout_per_routine_code BIGINT       NOT NULL AUTO_INCREMENT,
    workout_order            INTEGER,
    workout_name             VARCHAR(255) NOT NULL,
    link                     VARCHAR(255),
    weight_set               INTEGER,
    number_per_set           INTEGER,
    weight_per_set           INTEGER,
    workout_time             INTEGER,
    created_at               DATETIME DEFAULT NOW(),
    updated_at               DATETIME DEFAULT NOW(),
    routine_code             BIGINT       NOT NULL,
    exercise_equipment_code  BIGINT       NOT NULL,
    FOREIGN KEY (exercise_equipment_code) REFERENCES exercise_equipment (exercise_equipment_code) ON DELETE CASCADE,
    FOREIGN KEY (routine_code) REFERENCES routines (routine_code) ON DELETE CASCADE,
    PRIMARY KEY (workout_per_routine_code)
);

CREATE TABLE recommended_workout_history
(
    history_code      BIGINT NOT NULL AUTO_INCREMENT,
    routine_ratings DOUBLE,
    created_at        DATETIME DEFAULT NOW(),
    updated_at        DATETIME DEFAULT NOW(),
    workout_info_code BIGINT NOT NULL,
    FOREIGN KEY (workout_info_code) REFERENCES workout_info (workout_info_code) ON DELETE CASCADE,
    PRIMARY KEY (history_code)
);

CREATE TABLE record_per_user
(
    user_record_code  BIGINT       NOT NULL AUTO_INCREMENT,
    day_of_exercise   DATETIME,
    exercise_duration INTEGER,
    record_flag       BOOLEAN  DEFAULT TRUE,
    created_at        DATETIME DEFAULT NOW(),
    updated_at        DATETIME DEFAULT NOW(),
    user_code         VARCHAR(255) NOT NULL,
    routine_code      BIGINT       NOT NULL,
    PRIMARY KEY (user_record_code),
    FOREIGN KEY (routine_code) REFERENCES routines (routine_code) ON DELETE CASCADE,
    FOREIGN KEY (user_code) REFERENCES users (user_code) ON DELETE CASCADE
);

CREATE TABLE inbody
(
    inbody_code          BIGINT       NOT NULL AUTO_INCREMENT,
    inbody_score         INTEGER,
    weight DOUBLE,
    height DOUBLE,
    muscle_weight DOUBLE,
    fat_weight DOUBLE,
    bmi DOUBLE,
    fat_percentage DOUBLE,
    day_of_inbody        DATETIME,
    basal_metabolic_rate INTEGER,
    created_at           DATETIME DEFAULT NOW(),
    updated_at           DATETIME DEFAULT NOW(),
    user_code            VARCHAR(255) NOT NULL,
    PRIMARY KEY (inbody_code),
    FOREIGN KEY (user_code) REFERENCES users (user_code) ON DELETE CASCADE
);

CREATE TABLE rival
(
    rival_match_code BIGINT       NOT NULL AUTO_INCREMENT,
    user_code        VARCHAR(255) NOT NULL,
    rival_user_code  VARCHAR(255) NOT NULL,
    created_at       DATETIME DEFAULT NOW(),
    updated_at       DATETIME DEFAULT NOW(),
    PRIMARY KEY (rival_match_code),
    FOREIGN KEY (user_code) REFERENCES users (user_code) ON DELETE CASCADE,
    FOREIGN KEY (rival_user_code) REFERENCES users (user_code) ON DELETE CASCADE
);
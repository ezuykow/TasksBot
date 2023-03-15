-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE tasks(
    id SERIAL PRIMARY KEY ,
    chat_id INTEGER NOT NULL,
    text TEXT NOT NULL ,
    date DATE,
    time TIME
);

-- changeset ezuykow:2
ALTER TABLE tasks
    ALTER COLUMN chat_id
        TYPE BIGINT;

-- changeset ezuykow:3
CREATE INDEX tasks_date_time_idx ON tasks(date, time);


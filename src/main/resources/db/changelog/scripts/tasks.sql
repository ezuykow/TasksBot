-- liquibase formatted sql

-- changeset ezuykow:1
CREATE TABLE tasks(
    id SERIAL PRIMARY KEY ,
    chat_id INTEGER NOT NULL,
    text TEXT NOT NULL ,
    date DATE,
    time TIME
);
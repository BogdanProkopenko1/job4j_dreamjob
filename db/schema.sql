CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    name TEXT,
    description TEXT,
    created DATE
);

CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT,
    skills TEXT,
    position TEXT
);

CREATE TABLE user (
    id SERIAL PRIMARY KEY,
    name TEXT,
    email TEXT unique,
    password TEXT
);
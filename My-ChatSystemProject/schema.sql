CREATE DATABASE IF NOT EXISTS myapp_db;

-- ユーザーテーブル
CREATE TABLE IF NOT EXISTS users(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    mail VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_admin BOOLEAN NOT NULL DEFAULT FALSE,
    role VARCHAR(255)
);

-- イベントテーブル
CREATE TABLE IF NOT EXISTS events(
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    created_by INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- イベント中間テーブル
CREATE TABLE IF NOT EXISTS event_participants(
    event_id INT NOT NULL,
    user_id INT NOT NULL,
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(event_id, user_id),

    CONSTRAINT fk_event
        FOREIGN KEY(event_id)
        REFERENCES events(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- お知らせテーブル
CREATE TABLE IF NOT EXISTS news(
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- お知らせ中間テーブル
CREATE TABLE IF NOT EXISTS news_sender(
    news_id INT NOT NULL,
    user_id INT NOT NULL,

    PRIMARY KEY(news_id, user_id),

    CONSTRAINT fk_news
        FOREIGN KEY(news_id)
        REFERENCES news(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- 掲示板テーブル
CREATE TABLE IF NOT EXISTS chat_rooms(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_by INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 掲示板中間テーブル
CREATE TABLE IF NOT EXISTS chat_room_members(
    room_id INT NOT NULL,
    user_id INT NOT NULL,
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(room_id, user_id),

    CONSTRAINT fk_room
        FOREIGN KEY(room_id)
        REFERENCES chat_rooms(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- 掲示板メッセージテーブル
CREATE TABLE IF NOT EXISTS chat_messages(
    id SERIAL PRIMARY KEY,
    room_id INT NOT NULL,
    user_id INT NOT NULL,
    content TEXT,
    image_url TEXT,
    sent_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_room
        FOREIGN KEY (room_id)
        REFERENCES chat_rooms(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- インデックス作成
CREATE INDEX IF NOT EXISTS idx_event_participants_event_id ON event_participants(event_id);
CREATE INDEX IF NOT EXISTS idx_event_participants_user_id ON event_participants(user_id);

CREATE INDEX IF NOT EXISTS idx_news_sender_news_id ON news_sender(news_id);
CREATE INDEX IF NOT EXISTS idx_news_sender_user_id ON news_sender(user_id);

CREATE INDEX IF NOT EXISTS idx_chat_room_members_room_id ON chat_room_members(room_id);
CREATE INDEX IF NOT EXISTS idx_chat_room_members_user_id ON chat_room_members(user_id);


SELECT * FROM chat_rooms;
SELECT * FROM users;
SELECT * FROM chat_messages;
SELECT * FROM chat_room_members;
SELECT * FROM events;
SELECT * FROM event_participants;
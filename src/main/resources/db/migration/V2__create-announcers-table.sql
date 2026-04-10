CREATE TABLE announcers (
    id VARCHAR(255) PRIMARY KEY,
    recipient_id VARCHAR(255) NOT NULL,
    account_id VARCHAR(255) NOT NULL,
    chat_id BIGINT NOT NULL,
    text text NOT NULL,
    buttons jsonb not null,
    enabled boolean not null,
    announcer_type VARCHAR(255) not null
);

CREATE TABLE chats (
    id BIGINT PRIMARY KEY,
    title VARCHAR(512) NOT NULL,
    owner_id BIGINT NOT NULL
);

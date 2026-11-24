CREATE TABLE activities
(
    id             INT AUTO_INCREMENT NOT NULL,
    name           VARCHAR(255)  NOT NULL,
    category       VARCHAR(255)  NOT NULL,
    price          INT           NOT NULL,
    duration_hours INT           NOT NULL,
    environment    VARCHAR(255)  NOT NULL,
    `description`  VARCHAR(1000) NOT NULL,
    CONSTRAINT pk_activities PRIMARY KEY (id),
    CONSTRAINT chk_activities_price CHECK (price >= 0),
    CONSTRAINT chk_activities_hours CHECK (duration_hours > 0)
);

CREATE TABLE activity_tags
(
    activity_id INT          NOT NULL,
    tag         VARCHAR(255) NOT NULL,
    CONSTRAINT pk_activity_tags PRIMARY KEY (activity_id, tag),
    CONSTRAINT fk_activity_tags_on_activity
        FOREIGN KEY (activity_id) REFERENCES activities (id)
);
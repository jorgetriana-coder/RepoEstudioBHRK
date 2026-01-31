CREATE TABLE user_details(
    id bigint,
    name varchar not null,
    email varchar not null,
    password varchar not null,
    birthDate timestamp not null
);

CREATE TABLE task(
    id bigint,
    description varchar,
    isDone varchar,
    targetDate timestamp,
    user_id int,
    primary key (id),
    foreign key (user_id) references user_details(user_id)
);


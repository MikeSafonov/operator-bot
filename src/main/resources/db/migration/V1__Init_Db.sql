create table internal_users(
    id int primary key auto_increment,
    telegram_id bigint not null,
    full_name varchar(255) not null,
    status int not null
) engine=InnoDB;

create table external_users(
    id int primary key auto_increment,
    telegram_id bigint not null,
    full_name varchar(255) not null
) engine=InnoDB;

create table timetable(
    id int primary key auto_increment,
    user_id int not null,
    times date UNIQUE not null,
    FOREIGN KEY (user_id) REFERENCES internal_users(id)
) engine=InnoDB;
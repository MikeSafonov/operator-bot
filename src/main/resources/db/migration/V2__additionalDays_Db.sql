create table if not exists additional_day_off(
    id int primary key auto_increment,
    day_off date UNIQUE not null
) engine=InnoDB;

create table if not exists additional_workday(
    id int primary key auto_increment,
    workday date UNIQUE not null
) engine=InnoDB;
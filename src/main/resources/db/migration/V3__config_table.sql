create table if not exists config_table(
    id int primary key auto_increment,
    config varchar(45) UNIQUE,
    parameter varchar(255)
) engine=InnoDB;
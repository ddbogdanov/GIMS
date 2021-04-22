create database gims_db
create table customer (customer_id varchar(36) not null, customer_email varchar(320), customer_name varchar(50), customer_phone varchar(45), primary key (customer_id)) engine=InnoDB
create table employee (employee_id varchar(36) not null, employee_email varchar(320), employee_name varchar(50), employee_phone varchar(45), user_id varchar(36), primary key (employee_id)) engine=InnoDB
create table room (room_id char(1) not null, room_name varchar(120), status varchar(8), primary key (room_id)) engine=InnoDB
create table shift (shift_id varchar(36) not null, end_datetime datetime(6), start_datetime datetime(6), employee_id varchar(36), primary key (shift_id)) engine=InnoDB
create table stay (stay_id integer not null auto_increment, end_date date, start_date date, customer_id varchar(36), room_id char(1), primary key (stay_id)) engine=InnoDB
create table task (task_id varchar(36) not null, completed bit, description varchar(256), due_date varchar(20), name varchar(255), priority varchar(6), employee_id varchar(36), room_id char(1), primary key (task_id)) engine=InnoDB
create table user (user_id varchar(36) not null, isadmin bit, password varchar(140), username varchar(16), primary key (user_id)) engine=InnoDB
alter table employee add constraint FK6lk0xml9r7okjdq0onka4ytju foreign key (user_id) references user (user_id)
alter table shift add constraint FKg9ycreft1sv2jjvkno3dn3fqy foreign key (employee_id) references employee (employee_id)
alter table stay add constraint FKpd73bg3y7liffvrk1cgrvmw4k foreign key (customer_id) references customer (customer_id)
alter table stay add constraint FKe2w8prvqi2t69ul9bngyb8y50 foreign key (room_id) references room (room_id)
alter table task add constraint FKmeqi2abtbehx871tag4op3hag foreign key (employee_id) references employee (employee_id)
alter table task add constraint FKnlls5sa473lcqof7ll7aw2iv1 foreign key (room_id) references room (room_id)

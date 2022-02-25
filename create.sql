create table additional_charge (charge_id int identity not null, charge numeric(19,2), description varchar(255), order_id int, primary key (charge_id))
create table clocking (clocking_id int identity not null, clocked_in bit, clocked_out bit, in_datetime datetime2, out_datetime datetime2, primary key (clocking_id))
create table customer (customer_id varchar(36) not null, customer_email varchar(320), customer_name varchar(50), customer_phone varchar(45), primary key (customer_id))
create table employee (employee_id varchar(36) not null, employee_email varchar(320), employee_name varchar(50), employee_phone varchar(45), employee_rate_id int, user_id varchar(36), primary key (employee_id))
create table employee_rate (employee_rate_id int identity not null, rate numeric(19,2), primary key (employee_rate_id))
create table employee_report (employee_report_id varchar(36) not null, date date, employee_id varchar(36), primary key (employee_report_id))
create table event (event_id varchar(36) not null, end_date date, info varchar(255), name varchar(16), start_date date, event_status_id int, location_id varchar(36), primary key (event_id))
create table event_status (event_status_id int identity not null, status varchar(6), primary key (event_status_id))
create table location (location_id varchar(36) not null, audience_capacity int, info varchar(255), name varchar(16), primary key (location_id))
create table orders (order_id int identity not null, total numeric(19,2), customer_id varchar(36), stay_id int, primary key (order_id))
create table priority (priority_id int identity not null, priority varchar(6), primary key (priority_id))
create table reminder (reminder_id int identity not null, reminder varchar(255), user_id varchar(36), primary key (reminder_id))
create table room (room_id char(1) not null, room_name varchar(120), room_rate_id int, room_status_id int, primary key (room_id))
create table room_rate (room_rate_id int identity not null, rate numeric(19,2), primary key (room_rate_id))
create table room_report (room_report_id varchar(36) not null, date date, room_id char(1), primary key (room_report_id))
create table room_status (room_status_id int identity not null, status varchar(8), primary key (room_status_id))
create table shift (shift_id varchar(36) not null, end_datetime datetime2, start_datetime datetime2, clocking_id int, employee_id varchar(36), primary key (shift_id))
create table stay (stay_id int identity not null, end_date date, start_date date, customer_id varchar(36), room_id char(1), primary key (stay_id))
create table task (task_id varchar(36) not null, completed bit, description varchar(256), due_date varchar(20), name varchar(255), employee_id varchar(36), priority_id int, room_id char(1), primary key (task_id))
create table users (user_id varchar(36) not null, isadmin bit, password varchar(140), username varchar(16), primary key (user_id))
alter table additional_charge add constraint FKmoj4ioev50xuvwbqes2a4gomi foreign key (order_id) references orders
alter table employee add constraint FKluouxvpisbfd8qjwr1wk2awab foreign key (employee_rate_id) references employee_rate
alter table employee add constraint FKhal2duyxxjtadykhxos7wd3wg foreign key (user_id) references users
alter table employee_report add constraint FKp29l12lahykh7yycs6mrpiubt foreign key (employee_id) references employee
alter table event add constraint FK6esoabtbwawkgdx7wyx9sofr7 foreign key (event_status_id) references event_status
alter table event add constraint FKbb6c0h5nhs5og47iem617ehrl foreign key (location_id) references location
alter table orders add constraint FK624gtjin3po807j3vix093tlf foreign key (customer_id) references customer
alter table orders add constraint FKej5ax8wwk4c79fultmmtws0j8 foreign key (stay_id) references stay
alter table reminder add constraint FKo8n9bn7kt0wt86htrwbnt8mil foreign key (user_id) references users
alter table room add constraint FKpuslvgr5ssascr2o5v7plmid6 foreign key (room_rate_id) references room_rate
alter table room add constraint FK31b55s8e20hpyopjockpvqf6k foreign key (room_status_id) references room_status
alter table room_report add constraint FK1sr3w65uq5c4jpb8ha3imwa5i foreign key (room_id) references room
alter table shift add constraint FK7gtyiqxhy8rldxhr5793qlh7m foreign key (clocking_id) references clocking
alter table shift add constraint FKg9ycreft1sv2jjvkno3dn3fqy foreign key (employee_id) references employee
alter table stay add constraint FKpd73bg3y7liffvrk1cgrvmw4k foreign key (customer_id) references customer
alter table stay add constraint FKe2w8prvqi2t69ul9bngyb8y50 foreign key (room_id) references room
alter table task add constraint FKmeqi2abtbehx871tag4op3hag foreign key (employee_id) references employee
alter table task add constraint FK23pwolpebddlvnpucweas18g0 foreign key (priority_id) references priority
alter table task add constraint FKnlls5sa473lcqof7ll7aw2iv1 foreign key (room_id) references room
create table additional_charge (charge_id int identity not null, charge numeric(19,2), description varchar(255), order_id int, primary key (charge_id))
create table clocking (clocking_id int identity not null, clocked_in bit, clocked_out bit, in_datetime datetime2, out_datetime datetime2, primary key (clocking_id))
create table customer (customer_id varchar(36) not null, customer_email varchar(320), customer_name varchar(50), customer_phone varchar(45), primary key (customer_id))
create table employee (employee_id varchar(36) not null, employee_email varchar(320), employee_name varchar(50), employee_phone varchar(45), employee_rate_id int, user_id varchar(36), primary key (employee_id))
create table employee_rate (employee_rate_id int identity not null, rate numeric(19,2), primary key (employee_rate_id))
create table employee_report (employee_report_id varchar(36) not null, date date, employee_id varchar(36), primary key (employee_report_id))
create table event (event_id varchar(36) not null, end_date date, info varchar(255), name varchar(16), start_date date, event_status_id int, location_id varchar(36), primary key (event_id))
create table event_status (event_status_id int identity not null, status varchar(6), primary key (event_status_id))
create table location (location_id varchar(36) not null, audience_capacity int, info varchar(255), name varchar(16), primary key (location_id))
create table orders (order_id int identity not null, total numeric(19,2), customer_id varchar(36), stay_id int, primary key (order_id))
create table priority (priority_id int identity not null, priority varchar(6), primary key (priority_id))
create table reminder (reminder_id int identity not null, reminder varchar(255), user_id varchar(36), primary key (reminder_id))
create table room (room_id char(1) not null, room_name varchar(120), room_rate_id int, room_status_id int, primary key (room_id))
create table room_rate (room_rate_id int identity not null, rate numeric(19,2), primary key (room_rate_id))
create table room_report (room_report_id varchar(36) not null, date date, room_id char(1), primary key (room_report_id))
create table room_status (room_status_id int identity not null, status varchar(8), primary key (room_status_id))
create table shift (shift_id varchar(36) not null, end_datetime datetime2, start_datetime datetime2, clocking_id int, employee_id varchar(36), primary key (shift_id))
create table stay (stay_id int identity not null, end_date date, start_date date, customer_id varchar(36), room_id char(1), primary key (stay_id))
create table task (task_id varchar(36) not null, completed bit, description varchar(256), due_date varchar(20), name varchar(255), employee_id varchar(36), priority_id int, room_id char(1), primary key (task_id))
create table users (user_id varchar(36) not null, isadmin bit, password varchar(140), username varchar(16), primary key (user_id))
alter table additional_charge add constraint FKmoj4ioev50xuvwbqes2a4gomi foreign key (order_id) references orders
alter table employee add constraint FKluouxvpisbfd8qjwr1wk2awab foreign key (employee_rate_id) references employee_rate
alter table employee add constraint FKhal2duyxxjtadykhxos7wd3wg foreign key (user_id) references users
alter table employee_report add constraint FKp29l12lahykh7yycs6mrpiubt foreign key (employee_id) references employee
alter table event add constraint FK6esoabtbwawkgdx7wyx9sofr7 foreign key (event_status_id) references event_status
alter table event add constraint FKbb6c0h5nhs5og47iem617ehrl foreign key (location_id) references location
alter table orders add constraint FK624gtjin3po807j3vix093tlf foreign key (customer_id) references customer
alter table orders add constraint FKej5ax8wwk4c79fultmmtws0j8 foreign key (stay_id) references stay
alter table reminder add constraint FKo8n9bn7kt0wt86htrwbnt8mil foreign key (user_id) references users
alter table room add constraint FKpuslvgr5ssascr2o5v7plmid6 foreign key (room_rate_id) references room_rate
alter table room add constraint FK31b55s8e20hpyopjockpvqf6k foreign key (room_status_id) references room_status
alter table room_report add constraint FK1sr3w65uq5c4jpb8ha3imwa5i foreign key (room_id) references room
alter table shift add constraint FK7gtyiqxhy8rldxhr5793qlh7m foreign key (clocking_id) references clocking
alter table shift add constraint FKg9ycreft1sv2jjvkno3dn3fqy foreign key (employee_id) references employee
alter table stay add constraint FKpd73bg3y7liffvrk1cgrvmw4k foreign key (customer_id) references customer
alter table stay add constraint FKe2w8prvqi2t69ul9bngyb8y50 foreign key (room_id) references room
alter table task add constraint FKmeqi2abtbehx871tag4op3hag foreign key (employee_id) references employee
alter table task add constraint FK23pwolpebddlvnpucweas18g0 foreign key (priority_id) references priority
alter table task add constraint FKnlls5sa473lcqof7ll7aw2iv1 foreign key (room_id) references room

    create table additional_charge (
       charge_id int identity not null,
        charge numeric(19,2),
        description varchar(255),
        order_id int,
        primary key (charge_id)
    )

    create table clocking (
       clocking_id int identity not null,
        clocked_in bit,
        clocked_out bit,
        in_datetime datetime2,
        out_datetime datetime2,
        primary key (clocking_id)
    )

    create table customer (
       customer_id varchar(36) not null,
        customer_email varchar(320),
        customer_name varchar(50),
        customer_phone varchar(45),
        primary key (customer_id)
    )

    create table employee (
       employee_id varchar(36) not null,
        employee_email varchar(320),
        employee_name varchar(50),
        employee_phone varchar(45),
        employee_rate_id int,
        user_id varchar(36),
        primary key (employee_id)
    )

    create table employee_rate (
       employee_rate_id int identity not null,
        rate numeric(19,2),
        primary key (employee_rate_id)
    )

    create table employee_report (
       employee_report_id varchar(36) not null,
        date date,
        employee_id varchar(36),
        primary key (employee_report_id)
    )

    create table event (
       event_id varchar(36) not null,
        end_date date,
        info varchar(255),
        name varchar(16),
        start_date date,
        event_status_id int,
        location_id varchar(36),
        primary key (event_id)
    )

    create table event_status (
       event_status_id int identity not null,
        status varchar(6),
        primary key (event_status_id)
    )

    create table location (
       location_id varchar(36) not null,
        audience_capacity int,
        info varchar(255),
        name varchar(16),
        primary key (location_id)
    )

    create table orders (
       order_id int identity not null,
        total numeric(19,2),
        customer_id varchar(36),
        stay_id int,
        primary key (order_id)
    )

    create table priority (
       priority_id int identity not null,
        priority varchar(6),
        primary key (priority_id)
    )

    create table reminder (
       reminder_id int identity not null,
        reminder varchar(255),
        user_id varchar(36),
        primary key (reminder_id)
    )

    create table room (
       room_id char(1) not null,
        room_name varchar(120),
        room_rate_id int,
        room_status_id int,
        primary key (room_id)
    )

    create table room_rate (
       room_rate_id int identity not null,
        rate numeric(19,2),
        primary key (room_rate_id)
    )

    create table room_report (
       room_report_id varchar(36) not null,
        date date,
        room_id char(1),
        primary key (room_report_id)
    )

    create table room_status (
       room_status_id int identity not null,
        status varchar(8),
        primary key (room_status_id)
    )

    create table shift (
       shift_id varchar(36) not null,
        end_datetime datetime2,
        start_datetime datetime2,
        clocking_id int,
        employee_id varchar(36),
        primary key (shift_id)
    )

    create table stay (
       stay_id int identity not null,
        end_date date,
        start_date date,
        customer_id varchar(36),
        room_id char(1),
        primary key (stay_id)
    )

    create table task (
       task_id varchar(36) not null,
        completed bit,
        description varchar(256),
        due_date varchar(20),
        name varchar(255),
        employee_id varchar(36),
        priority_id int,
        room_id char(1),
        primary key (task_id)
    )

    create table users (
       user_id varchar(36) not null,
        isadmin bit,
        password varchar(140),
        username varchar(16),
        primary key (user_id)
    )

    alter table additional_charge 
       add constraint FKmoj4ioev50xuvwbqes2a4gomi 
       foreign key (order_id) 
       references orders

    alter table employee 
       add constraint FKluouxvpisbfd8qjwr1wk2awab 
       foreign key (employee_rate_id) 
       references employee_rate

    alter table employee 
       add constraint FKhal2duyxxjtadykhxos7wd3wg 
       foreign key (user_id) 
       references users

    alter table employee_report 
       add constraint FKp29l12lahykh7yycs6mrpiubt 
       foreign key (employee_id) 
       references employee

    alter table event 
       add constraint FK6esoabtbwawkgdx7wyx9sofr7 
       foreign key (event_status_id) 
       references event_status

    alter table event 
       add constraint FKbb6c0h5nhs5og47iem617ehrl 
       foreign key (location_id) 
       references location

    alter table orders 
       add constraint FK624gtjin3po807j3vix093tlf 
       foreign key (customer_id) 
       references customer

    alter table orders 
       add constraint FKej5ax8wwk4c79fultmmtws0j8 
       foreign key (stay_id) 
       references stay

    alter table reminder 
       add constraint FKo8n9bn7kt0wt86htrwbnt8mil 
       foreign key (user_id) 
       references users

    alter table room 
       add constraint FKpuslvgr5ssascr2o5v7plmid6 
       foreign key (room_rate_id) 
       references room_rate

    alter table room 
       add constraint FK31b55s8e20hpyopjockpvqf6k 
       foreign key (room_status_id) 
       references room_status

    alter table room_report 
       add constraint FK1sr3w65uq5c4jpb8ha3imwa5i 
       foreign key (room_id) 
       references room

    alter table shift 
       add constraint FK7gtyiqxhy8rldxhr5793qlh7m 
       foreign key (clocking_id) 
       references clocking

    alter table shift 
       add constraint FKg9ycreft1sv2jjvkno3dn3fqy 
       foreign key (employee_id) 
       references employee

    alter table stay 
       add constraint FKpd73bg3y7liffvrk1cgrvmw4k 
       foreign key (customer_id) 
       references customer

    alter table stay 
       add constraint FKe2w8prvqi2t69ul9bngyb8y50 
       foreign key (room_id) 
       references room

    alter table task 
       add constraint FKmeqi2abtbehx871tag4op3hag 
       foreign key (employee_id) 
       references employee

    alter table task 
       add constraint FK23pwolpebddlvnpucweas18g0 
       foreign key (priority_id) 
       references priority

    alter table task 
       add constraint FKnlls5sa473lcqof7ll7aw2iv1 
       foreign key (room_id) 
       references room

    create table additional_charge (
       charge_id int identity not null,
        charge numeric(19,2),
        description varchar(255),
        order_id int,
        primary key (charge_id)
    )

    create table clocking (
       clocking_id int identity not null,
        clocked_in bit,
        clocked_out bit,
        in_datetime datetime2,
        out_datetime datetime2,
        primary key (clocking_id)
    )

    create table customer (
       customer_id varchar(36) not null,
        customer_email varchar(320),
        customer_name varchar(50),
        customer_phone varchar(45),
        primary key (customer_id)
    )

    create table employee (
       employee_id varchar(36) not null,
        employee_email varchar(320),
        employee_name varchar(50),
        employee_phone varchar(45),
        employee_rate_id int,
        user_id varchar(36),
        primary key (employee_id)
    )

    create table employee_rate (
       employee_rate_id int identity not null,
        rate numeric(19,2),
        primary key (employee_rate_id)
    )

    create table employee_report (
       employee_report_id varchar(36) not null,
        date date,
        employee_id varchar(36),
        primary key (employee_report_id)
    )

    create table event (
       event_id varchar(36) not null,
        end_date date,
        info varchar(255),
        name varchar(16),
        start_date date,
        event_status_id int,
        location_id varchar(36),
        primary key (event_id)
    )

    create table event_status (
       event_status_id int identity not null,
        status varchar(6),
        primary key (event_status_id)
    )

    create table location (
       location_id varchar(36) not null,
        audience_capacity int,
        info varchar(255),
        name varchar(16),
        primary key (location_id)
    )

    create table orders (
       order_id int identity not null,
        total numeric(19,2),
        customer_id varchar(36),
        stay_id int,
        primary key (order_id)
    )

    create table priority (
       priority_id int identity not null,
        priority varchar(6),
        primary key (priority_id)
    )

    create table reminder (
       reminder_id int identity not null,
        reminder varchar(255),
        user_id varchar(36),
        primary key (reminder_id)
    )

    create table room (
       room_id char(1) not null,
        room_name varchar(120),
        room_rate_id int,
        room_status_id int,
        primary key (room_id)
    )

    create table room_rate (
       room_rate_id int identity not null,
        rate numeric(19,2),
        primary key (room_rate_id)
    )

    create table room_report (
       room_report_id varchar(36) not null,
        date date,
        room_id char(1),
        primary key (room_report_id)
    )

    create table room_status (
       room_status_id int identity not null,
        status varchar(8),
        primary key (room_status_id)
    )

    create table shift (
       shift_id varchar(36) not null,
        end_datetime datetime2,
        start_datetime datetime2,
        clocking_id int,
        employee_id varchar(36),
        primary key (shift_id)
    )

    create table stay (
       stay_id int identity not null,
        end_date date,
        start_date date,
        customer_id varchar(36),
        room_id char(1),
        primary key (stay_id)
    )

    create table task (
       task_id varchar(36) not null,
        completed bit,
        description varchar(256),
        due_date varchar(20),
        name varchar(255),
        employee_id varchar(36),
        priority_id int,
        room_id char(1),
        primary key (task_id)
    )

    create table users (
       user_id varchar(36) not null,
        isadmin bit,
        password varchar(140),
        username varchar(16),
        primary key (user_id)
    )

    alter table additional_charge 
       add constraint FKmoj4ioev50xuvwbqes2a4gomi 
       foreign key (order_id) 
       references orders

    alter table employee 
       add constraint FKluouxvpisbfd8qjwr1wk2awab 
       foreign key (employee_rate_id) 
       references employee_rate

    alter table employee 
       add constraint FKhal2duyxxjtadykhxos7wd3wg 
       foreign key (user_id) 
       references users

    alter table employee_report 
       add constraint FKp29l12lahykh7yycs6mrpiubt 
       foreign key (employee_id) 
       references employee

    alter table event 
       add constraint FK6esoabtbwawkgdx7wyx9sofr7 
       foreign key (event_status_id) 
       references event_status

    alter table event 
       add constraint FKbb6c0h5nhs5og47iem617ehrl 
       foreign key (location_id) 
       references location

    alter table orders 
       add constraint FK624gtjin3po807j3vix093tlf 
       foreign key (customer_id) 
       references customer

    alter table orders 
       add constraint FKej5ax8wwk4c79fultmmtws0j8 
       foreign key (stay_id) 
       references stay

    alter table reminder 
       add constraint FKo8n9bn7kt0wt86htrwbnt8mil 
       foreign key (user_id) 
       references users

    alter table room 
       add constraint FKpuslvgr5ssascr2o5v7plmid6 
       foreign key (room_rate_id) 
       references room_rate

    alter table room 
       add constraint FK31b55s8e20hpyopjockpvqf6k 
       foreign key (room_status_id) 
       references room_status

    alter table room_report 
       add constraint FK1sr3w65uq5c4jpb8ha3imwa5i 
       foreign key (room_id) 
       references room

    alter table shift 
       add constraint FK7gtyiqxhy8rldxhr5793qlh7m 
       foreign key (clocking_id) 
       references clocking

    alter table shift 
       add constraint FKg9ycreft1sv2jjvkno3dn3fqy 
       foreign key (employee_id) 
       references employee

    alter table stay 
       add constraint FKpd73bg3y7liffvrk1cgrvmw4k 
       foreign key (customer_id) 
       references customer

    alter table stay 
       add constraint FKe2w8prvqi2t69ul9bngyb8y50 
       foreign key (room_id) 
       references room

    alter table task 
       add constraint FKmeqi2abtbehx871tag4op3hag 
       foreign key (employee_id) 
       references employee

    alter table task 
       add constraint FK23pwolpebddlvnpucweas18g0 
       foreign key (priority_id) 
       references priority

    alter table task 
       add constraint FKnlls5sa473lcqof7ll7aw2iv1 
       foreign key (room_id) 
       references room

    create table additional_charge (
       charge_id int identity not null,
        charge numeric(19,2),
        description varchar(255),
        order_id int,
        primary key (charge_id)
    )

    create table clocking (
       clocking_id int identity not null,
        clocked_in bit,
        clocked_out bit,
        in_datetime datetime2,
        out_datetime datetime2,
        primary key (clocking_id)
    )

    create table customer (
       customer_id varchar(36) not null,
        customer_email varchar(320),
        customer_name varchar(50),
        customer_phone varchar(45),
        primary key (customer_id)
    )

    create table employee (
       employee_id varchar(36) not null,
        employee_email varchar(320),
        employee_name varchar(50),
        employee_phone varchar(45),
        employee_rate_id int,
        user_id varchar(36),
        primary key (employee_id)
    )

    create table employee_rate (
       employee_rate_id int identity not null,
        rate numeric(19,2),
        primary key (employee_rate_id)
    )

    create table employee_report (
       employee_report_id varchar(36) not null,
        date date,
        employee_id varchar(36),
        primary key (employee_report_id)
    )

    create table event (
       event_id varchar(36) not null,
        end_date date,
        info varchar(255),
        name varchar(16),
        start_date date,
        event_status_id int,
        location_id varchar(36),
        primary key (event_id)
    )

    create table event_status (
       event_status_id int identity not null,
        status varchar(6),
        primary key (event_status_id)
    )

    create table location (
       location_id varchar(36) not null,
        audience_capacity int,
        info varchar(255),
        name varchar(16),
        primary key (location_id)
    )

    create table orders (
       order_id int identity not null,
        total numeric(19,2),
        customer_id varchar(36),
        stay_id int,
        primary key (order_id)
    )

    create table priority (
       priority_id int identity not null,
        priority varchar(6),
        primary key (priority_id)
    )

    create table reminder (
       reminder_id int identity not null,
        reminder varchar(255),
        user_id varchar(36),
        primary key (reminder_id)
    )

    create table room (
       room_id char(1) not null,
        room_name varchar(120),
        room_rate_id int,
        room_status_id int,
        primary key (room_id)
    )

    create table room_rate (
       room_rate_id int identity not null,
        rate numeric(19,2),
        primary key (room_rate_id)
    )

    create table room_report (
       room_report_id varchar(36) not null,
        date date,
        room_id char(1),
        primary key (room_report_id)
    )

    create table room_status (
       room_status_id int identity not null,
        status varchar(8),
        primary key (room_status_id)
    )

    create table shift (
       shift_id varchar(36) not null,
        end_datetime datetime2,
        start_datetime datetime2,
        clocking_id int,
        employee_id varchar(36),
        primary key (shift_id)
    )

    create table stay (
       stay_id int identity not null,
        end_date date,
        start_date date,
        customer_id varchar(36),
        room_id char(1),
        primary key (stay_id)
    )

    create table task (
       task_id varchar(36) not null,
        completed bit,
        description varchar(256),
        due_date varchar(20),
        name varchar(255),
        employee_id varchar(36),
        priority_id int,
        room_id char(1),
        primary key (task_id)
    )

    create table users (
       user_id varchar(36) not null,
        isadmin bit,
        password varchar(140),
        username varchar(16),
        primary key (user_id)
    )

    alter table additional_charge 
       add constraint FKmoj4ioev50xuvwbqes2a4gomi 
       foreign key (order_id) 
       references orders

    alter table employee 
       add constraint FKluouxvpisbfd8qjwr1wk2awab 
       foreign key (employee_rate_id) 
       references employee_rate

    alter table employee 
       add constraint FKhal2duyxxjtadykhxos7wd3wg 
       foreign key (user_id) 
       references users

    alter table employee_report 
       add constraint FKp29l12lahykh7yycs6mrpiubt 
       foreign key (employee_id) 
       references employee

    alter table event 
       add constraint FK6esoabtbwawkgdx7wyx9sofr7 
       foreign key (event_status_id) 
       references event_status

    alter table event 
       add constraint FKbb6c0h5nhs5og47iem617ehrl 
       foreign key (location_id) 
       references location

    alter table orders 
       add constraint FK624gtjin3po807j3vix093tlf 
       foreign key (customer_id) 
       references customer

    alter table orders 
       add constraint FKej5ax8wwk4c79fultmmtws0j8 
       foreign key (stay_id) 
       references stay

    alter table reminder 
       add constraint FKo8n9bn7kt0wt86htrwbnt8mil 
       foreign key (user_id) 
       references users

    alter table room 
       add constraint FKpuslvgr5ssascr2o5v7plmid6 
       foreign key (room_rate_id) 
       references room_rate

    alter table room 
       add constraint FK31b55s8e20hpyopjockpvqf6k 
       foreign key (room_status_id) 
       references room_status

    alter table room_report 
       add constraint FK1sr3w65uq5c4jpb8ha3imwa5i 
       foreign key (room_id) 
       references room

    alter table shift 
       add constraint FK7gtyiqxhy8rldxhr5793qlh7m 
       foreign key (clocking_id) 
       references clocking

    alter table shift 
       add constraint FKg9ycreft1sv2jjvkno3dn3fqy 
       foreign key (employee_id) 
       references employee

    alter table stay 
       add constraint FKpd73bg3y7liffvrk1cgrvmw4k 
       foreign key (customer_id) 
       references customer

    alter table stay 
       add constraint FKe2w8prvqi2t69ul9bngyb8y50 
       foreign key (room_id) 
       references room

    alter table task 
       add constraint FKmeqi2abtbehx871tag4op3hag 
       foreign key (employee_id) 
       references employee

    alter table task 
       add constraint FK23pwolpebddlvnpucweas18g0 
       foreign key (priority_id) 
       references priority

    alter table task 
       add constraint FKnlls5sa473lcqof7ll7aw2iv1 
       foreign key (room_id) 
       references room

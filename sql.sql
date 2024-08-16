create table employee (
	id int unsigned auto_increment primary key,
    code varchar (50) not null,
    name varchar (255) not null,
    email varchar (100) not null,
    phone varchar (10) not null,
    age int unsigned not null,
    province_id int unsigned not null,
    district_id int unsigned not null,
    commune_id int unsigned not null
);

alter table employee
    add foreign key (province_id) references province(id),
    add foreign key (district_id) references district(id),
    add foreign key (commune_id) references commune(id);

create table province (
	id int unsigned auto_increment primary key,
    code varchar (50) not null,
    name varchar (255) not null
);

create table district (
	id int unsigned auto_increment primary key,
    code varchar (50) not null,
    name varchar (255) not null,
    province_id int unsigned not null
);

alter table district 
	add foreign key (province_id) references province(id);
    
create table commune (
	id int unsigned auto_increment primary key,
    code varchar (50) not null,
    name varchar (255) not null,
    district_id int unsigned not null,
    population int unsigned not null,
    area float unsigned not null
);

alter table commune 
	add foreign key (district_id) references district(id);

create table certificate (
	id int unsigned auto_increment primary key,
    code varchar(50) not null,
    name varchar(100) not null,
    type varchar(50) not null,
    issue_date timestamp not null default current_timestamp,
    expiry_date timestamp not null default current_timestamp,
    province_id int unsigned not null
);

alter table certificate
	add employee_id int unsigned not null;

INSERT INTO certificate (code, name, type, issue_date, expiry_date, province_id)
VALUES 
('CERT001', 'Database Management Certification', 'Technical', '2023-02-01 11:00:00', '2024-02-01 11:00:00', 5),
('CERT002', 'Data Science Certification', 'Academic', '2023-03-01 12:00:00', '2024-03-01 12:00:00', 6),
('CERT003', 'Project Management Certification', 'Professional', '2023-04-01 13:00:00', '2024-04-01 13:00:00', 7);

select * from certificate;

delete from certificate where id = 6;

alter table certificate 
	add foreign key (employee_id) references employee(id);


-- insert into employee (code, name, email, phone, age) 
-- value ('LS002', 'Nguyen Thi Phuong', 'phuong@gmail.com', '0123456789', 19);

select * from district;
insert into district (code, name, province_id) values ('B2', 'Vu Thu', 4);
SELECT CASE WHEN (COUNT(d) > 0) THEN true ELSE false END
FROM District as d
WHERE d.code = 'B2' AND d.province_id = 4;
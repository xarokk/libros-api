source reinodb-schema.sql;

insert into users values('alicia', MD5('alicia'), 'Alicia', 'alicia@acme.com');
insert into user_roles values ('alicia', 'registered');

insert into users values('blas', MD5('blas'), 'Blas', 'blas@acme.com');
insert into user_roles values ('blas', 'registered');

insert into users values('luffy', MD5('luffy'), 'Luffy', 'luffy@acme.com');
insert into user_roles values ('luffy', 'registered');

insert into users values('admin', MD5('admin'), 'admin', 'admin@acme.com');
insert into user_roles values ('admin', 'administrator');
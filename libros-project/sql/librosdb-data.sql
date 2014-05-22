source librosrdb-schema.sql;
insert into users values('alicia', 'Alicia', 'alicia@acme.com');
insert into users values('blas', 'Blas', 'blas@acme.com');
insert into users values('luffy', 'Luffy', 'luffy@acme.com');
insert into users values('admin', 'admin', 'admin@acme.com');

insert into libros (titulo, autor, lengua,edicion , editorial) values ('titulo1' ,'autor1', 'english','molona' , 'anaya');
select sleep(1);
insert into libros (titulo, autor, lengua,edicion , editorial) values ('titulo2' ,'autor2', 'english','molona' , 'anaya');
select sleep(1);
insert into libros (titulo, autor, lengua,edicion , editorial) values ('titulo3' ,'autor1', 'english','molona' , 'anaya');
select sleep(1);
insert into libros (titulo, autor, lengua,edicion , editorial) values ('titulo4' ,'autor3', 'spanish','molona' , 'anaya');
select sleep(1);
insert into libros (titulo, autor, lengua,edicion , editorial) values ('titulo5' ,'autor6', 'english','molona' , 'vives');
select sleep(1);
insert into libros (titulo, autor, lengua,edicion , editorial) values ('titulo6' ,'autor6', 'english','molona' , 'vives');select sleep(1);
insert into libros (titulo, autor, lengua,edicion , editorial) values ('titulo7' ,'autor6', 'english','molona' , 'vives');select sleep(1);
insert into libros (titulo, autor, lengua,edicion , editorial) values ('titulo8' ,'autor6', 'english','molona' , 'vives');select sleep(1);


insert into libros (titulo, autor, lengua,edicion , editorial) values ('titulo9' ,'autor3', 'spanish','molona' , 'anaya');select sleep(1);

insert into libros (titulo, autor, lengua,edicion , editorial) values ('titulo10' ,'autor3', 'spanish','molona' , 'anaya');select sleep(1);

insert into libros (titulo, autor, lengua,edicion , editorial) values ('titulo11' ,'autor3', 'spanish','molona' , 'anaya');select sleep(1);

insert into libros (titulo, autor, lengua,edicion , editorial) values ('titulo12' ,'autor3', 'spanish','molona' , 'anaya');select sleep(1);



insert into reviews (reviewid , libroid , username , summary) values ('1','1','alicia','mensaje absurdo1');

insert into reviews (reviewid , libroid , username , summary) values ('2','2','alicia','mensaje absurdo2');

insert into reviews (reviewid , libroid , username , summary) values ('3','3','alicia','mensaje absurdo3');

insert into reviews (reviewid , libroid , username , summary) values ('4','4','alicia','mensaje absurdo4');


insert into reviews (reviewid , libroid , username , summary) values ('5','5','alicia','mensaje absurdo5');


insert into reviews (reviewid , libroid , username , summary) values ('6','6','alicia','mensaje absurdo6');


insert into reviews (reviewid , libroid , username , summary) values ('7','7','alicia','mensaje absurdo7');

insert into reviews (reviewid , libroid , username , summary) values ('8','8','alicia','mensaje absurdo8');


insert into reviews (reviewid , libroid , username , summary) values ('10','1','blas','mensaje absurdo1');

insert into reviews (reviewid , libroid , username , summary) values ('20','2','blas','mensaje absurdo2');

insert into reviews (reviewid , libroid , username , summary) values ('30','3','blas','mensaje absurdo3');

insert into reviews (reviewid , libroid , username , summary) values ('40','4','blas','mensaje absurdo4');


insert into reviews (reviewid , libroid , username , summary) values ('50','5','blas','mensaje absurdo5');


insert into reviews (reviewid , libroid , username , summary) values ('60','6','blas','mensaje absurdo6');


insert into reviews (reviewid , libroid , username , summary) values ('70','7','blas','mensaje absurdo7');

insert into reviews (reviewid , libroid , username , summary) values ('80','8','blas','mensaje absurdo8');


insert into reviews (reviewid , libroid , username , summary) values ('100','1','luffy','mensaje absurdo1');

insert into reviews (reviewid , libroid , username , summary) values ('200','2','luffy','mensaje absurdo2');

insert into reviews (reviewid , libroid , username , summary) values ('300','3','luffy','mensaje absurdo3');

insert into reviews (reviewid , libroid , username , summary) values ('400','4','luffy','mensaje absurdo4');


insert into reviews (reviewid , libroid , username , summary) values ('500','5','luffy','mensaje absurdo5');


insert into reviews (reviewid , libroid , username , summary) values ('600','6','luffy','mensaje absurdo6');


insert into reviews (reviewid , libroid , username , summary) values ('700','7','luffy','mensaje absurdo7');

insert into reviews (reviewid , libroid , username , summary) values ('800','8','luffy','mensaje absurdo8');


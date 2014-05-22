drop user 'reino'@'localhost';
create user 'reino'@'localhost' identified by 'reino';
grant all privileges on reinodb.* to 'reino'@'localhost';
flush privileges;

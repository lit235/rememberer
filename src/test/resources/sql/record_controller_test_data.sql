truncate table records;
truncate table api_clients;

insert into records (id, text,code, created_at) VALUES(1, "test1","test1_code",NOW()),(2, "test2","test2_code",NOW()),(3, "test3","test3_code",NOW());
insert into records (id, text,code, created_at, password_hash) VALUES(4,"dGVzdCB0ZXh0","test_encrypted_code",NOW(),'5ebe2294ecd0e0f08eab7690d2a6ee69');
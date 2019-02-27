insert into record (id, text,code, created_at) VALUES(1, "test1","test1_code",NOW()),(2, "test2","test2_code",NOW()),(3, "test3","test3_code",NOW());
insert into record (id, text,code, created_at, password_hash) VALUES(4,
"dGVzdCB0ZXh0", #test text
"test_encrypted_code",
NOW(),
 '^?"???????v???i' #secret
 );
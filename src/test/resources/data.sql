insert into achiever(id,name,email) values ('5530a3b8-1bdc-4397-b414-b3d5bd147498','User1','user1@company.com');
insert into achiever(id,name,email) values ('52eb7ca5-eb2a-4dad-8f27-c0c0f2920a14','User2','user2@company.com');
insert into achiever(id,name,email) values('3658cc50-fe47-4acb-99e7-accfd83269d6','User3','user3@company.com');

insert into objective(id, title, achiever_id, as_of, deadline) values ('350bebae-d54f-4e60-a2c8-77a0778e1c5b', 'Read Heavenly Mathematics', '5530a3b8-1bdc-4397-b414-b3d5bd147498', current_timestamp(), current_timestamp());
insert into objective(id, title, achiever_id, as_of, deadline) values ('04397fe3-772d-4424-881d-0863f0a5bbbf', 'Research on black holes', '52eb7ca5-eb2a-4dad-8f27-c0c0f2920a14', current_timestamp(), current_timestamp());
insert into objective(id, title, achiever_id, as_of, deadline) values ('ae9fdde2-6b6d-4bb9-b7c2-2ff27308472a', 'Research on black holes', '3658cc50-fe47-4acb-99e7-accfd83269d6', current_timestamp(), current_timestamp());
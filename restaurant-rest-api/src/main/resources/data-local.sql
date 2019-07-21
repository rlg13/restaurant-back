-- Initial Catalog
INSERT INTO DISH_CATALOG ( NAME, "TYPE") VALUES ('Ensalada Tropical','FIRST');
INSERT INTO DISH_CATALOG ( NAME, "TYPE") VALUES ('Gazpacho','FIRST');
INSERT INTO DISH_CATALOG ( NAME, "TYPE") VALUES ('Entremeses','FIRST');

INSERT INTO DISH_CATALOG ( NAME, "TYPE") VALUES ('Filete','SECOND');
INSERT INTO DISH_CATALOG ( NAME, "TYPE") VALUES ('Emperador','SECOND');
INSERT INTO DISH_CATALOG ( NAME, "TYPE") VALUES ('Hamburguesa','SECOND');

INSERT INTO DISH_CATALOG ( NAME, "TYPE") VALUES ('Piña','DESSERT');
INSERT INTO DISH_CATALOG ( NAME, "TYPE") VALUES ('Tarta Queso','DESSERT');
INSERT INTO DISH_CATALOG ( NAME, "TYPE") VALUES ('Tarta Chocolate','DESSERT');


-- Batch User.
INSERT INTO USERS (NAME, PASSWORD, ENABLE_SYSTEM_OPERATIONS ) VALUES ('BatchSystem',HASH('SHA256', STRINGTOUTF8('BatchSystem')),TRUE);

INSERT INTO USERS (NAME, PASSWORD ) VALUES ('Pepe',HASH('SHA256', STRINGTOUTF8('1234')));
INSERT INTO USERS (NAME,PASSWORD ) VALUES ('Juan', HASH('SHA256', STRINGTOUTF8('1234')));

-- Example VALUES FOR LOCAL testing.
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-2,'RECEIVED',1,4,7,SYSDATE-1);
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'DELIVERED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));
INSERT INTO ORDERS (USER_ID, DAY_ORDER,STATE, FIRST_DISH_ID,SECOND_DISH_ID,DESERT_ID, DAY_TO_SERVE ) VALUES (2,SYSDATE-1,'RECEIVED',1,4,7,TRUNC(SYSDATE));


COMMIT;

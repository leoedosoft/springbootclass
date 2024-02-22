CREATE TABLE `role` (
  `roleid` varchar(255) NOT NULL,
  `tipo` varchar(10) DEFAULT NULL,
  `userid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`roleid`),
  KEY `FK61g3ambult7v7nh59xirgd9nf` (`userid`),
  CONSTRAINT `FK61g3ambult7v7nh59xirgd9nf` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user` (
  `userid` varchar(255) NOT NULL,
  `createdate` datetime(6) DEFAULT NULL,
  `mail` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `password` varchar(300) DEFAULT NULL,
  `surname` varchar(50) DEFAULT NULL,
  `typeuser` varchar(20) DEFAULT NULL,
  `updatedate` datetime(6) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- Datenbank Struktur für twitter-monitor
CREATE DATABASE IF NOT EXISTS `twitter_monitor` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `twitter_monitor`;

-- Struktur von Tabelle twitter-monitor.users
CREATE TABLE IF NOT EXISTS `users` (
  `username` varchar(60) NOT NULL,
  `password` varchar(80) NOT NULL,
  `enabled` tinyint default 1 NOT NULL,
  `email` varchar(60) NOT NULL,
  `registeredAt` datetime default current_timestamp,
  PRIMARY KEY (`username`)
);

CREATE TABLE IF NOT EXISTS `authorities` (
  `username` varchar(60) NOT NULL,
  `authority` varchar(60) NOT NULL,
  PRIMARY KEY (`username`, `authority`),
  FOREIGN KEY (`username`) REFERENCES `users` (`username`)
);

-- Struktur von Tabelle twitter-monitor.notificationType
CREATE TABLE IF NOT EXISTS `notificationType` (
  `type` char(3) NOT NULL,
  `descr` varchar(60) NOT NULL,
  PRIMARY KEY (`type`)
);

-- Struktur von Tabelle twitter-monitor.notifications
CREATE TABLE IF NOT EXISTS `notifications` (
  `notificationId` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(60) NOT NULL,
  `type` char(3) NOT NULL,
  `subject` varchar(60) NOT NULL,
  `body` text NOT NULL,
  `createdAt` datetime default current_timestamp,
  `sentAt` datetime,
  PRIMARY KEY (`notificationId`),
  FOREIGN KEY (`type`) REFERENCES `notificationType` (`type`),
  FOREIGN KEY (`username`) REFERENCES `users` (`username`)
);

-- Struktur von Tabelle twitter-monitor.preferences
CREATE TABLE IF NOT EXISTS `preferences` (
  `preferenceType` char(3) NOT NULL,
  `descr` varchar(60) NOT NULL,
  `defaultValue` int(11) NOT NULL,
  PRIMARY KEY (`preferenceType`)
);

-- Struktur von Tabelle twitter-monitor.user_x_preferences
CREATE TABLE IF NOT EXISTS `user_x_preferences` (
  `username` varchar(60) NOT NULL,
  `preferenceType` char(3) NOT NULL,
  `value` int(11) NOT NULL,
  PRIMARY KEY (`username`,`preferenceType`),
  FOREIGN KEY (`username`) REFERENCES `users` (`username`),
  FOREIGN KEY (`preferenceType`) REFERENCES `preferences` (`preferenceType`)
);

-- Struktur von Tabelle twitter-monitor.keywords
CREATE TABLE IF NOT EXISTS `keywords` (
  `keyword` varchar(50) NOT NULL,
  `username` varchar(60) NOT NULL,
  `priority` int(1) DEFAULT 1 NOT NULL,
  `active` int(1) DEFAULT 1 NOT NULL,
  `createdAt` datetime default current_timestamp,
  PRIMARY KEY (`keyword`,`username`),
  FOREIGN KEY (`username`) REFERENCES `users` (`username`)
);

-- Struktur von Tabelle twitter-monitor.tweetAuthors
CREATE TABLE IF NOT EXISTS `tweetAuthors` (
  `authorId` bigint NOT NULL,
  `name` varchar(15) NOT NULL,
  `screenName` varchar(20),
  `followerCount` int(11),
  `pictureUrl` varchar(100),
  PRIMARY KEY (`authorId`)
);

-- Struktur von Tabelle twitter-monitor.tweets
CREATE TABLE IF NOT EXISTS `tweets` (
  `tweetId` bigint NOT NULL,
  `authorId` bigint NOT NULL,
  `text` varchar(200) NOT NULL,
  `favoriteCount` int(11) NOT NULL,
  `retweetCount` int(11) NOT NULL,
  `place` varchar(60),
  `image` varchar(100),
  `createdAt` datetime NOT NULL,
  `lastUpdate` datetime default current_timestamp,
  PRIMARY KEY (`tweetId`),
  FOREIGN KEY (`authorId`) REFERENCES `tweetAuthors` (`authorId`)
);

-- Struktur von Tabelle twitter-monitor.tweets_x_keywords
CREATE TABLE IF NOT EXISTS `tweets_x_keywords` (
  `tweetId` bigint NOT NULL,
  `keyword` varchar(50) NOT NULL,
  PRIMARY KEY (`tweetId`, `keyword`),
  FOREIGN KEY (`keyword`) REFERENCES `keywords` (`keyword`) ON DELETE CASCADE,
  FOREIGN KEY (`tweetId`) REFERENCES `tweets` (`tweetId`) ON DELETE CASCADE
);

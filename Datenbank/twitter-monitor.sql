-- Datenbank Struktur für twitter-monitor
CREATE DATABASE IF NOT EXISTS `twitter_monitor` /*!40100 COLLATE 'utf8_general_ci' */;
USE `twitter_monitor`;

-- Struktur von Tabelle twitter-monitor.benutzer
CREATE TABLE IF NOT EXISTS `benutzer` (
  `benutzer_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(60) NOT NULL,
  `passwort` text NOT NULL,
  `registrierdatum` datetime default current_timestamp,
  PRIMARY KEY (`benutzer_id`)
);

-- Struktur von Tabelle twitter-monitor.einstellungen
CREATE TABLE IF NOT EXISTS `einstellungen` (
  `kz` char(3) NOT NULL,
  `beschreibung` text NOT NULL,
  `standardwert` int(11) NOT NULL,
  PRIMARY KEY (`kz`)
);

-- Struktur von Tabelle twitter-monitor.benachrichtigungs_typ
CREATE TABLE IF NOT EXISTS `benachrichtigungs_typ` (
  `kz` char(3) NOT NULL,
  `beschreibung` text NOT NULL,
  PRIMARY KEY (`kz`)
);

-- Struktur von Tabelle twitter-monitor.benachrichtigungen
CREATE TABLE IF NOT EXISTS `benachrichtigungen` (
  `benachrichtigungs_id` int(11) NOT NULL AUTO_INCREMENT,
  `benutzer_id` int(11) NOT NULL,
  `benachrichtigungs_typ_kz` char(3) NOT NULL,
  `titel` text NOT NULL,
  `text` text NOT NULL,
  `erstellt_am` datetime default current_timestamp,
  `versendet_am` datetime,
  PRIMARY KEY (`benachrichtigungs_id`),
  FOREIGN KEY (`benachrichtigungs_typ_kz`) REFERENCES `benachrichtigungs_typ` (`kz`),
  FOREIGN KEY (`benutzer_id`) REFERENCES `benutzer` (`benutzer_id`)
);

-- Struktur von Tabelle twitter-monitor.benutzer_x_einstellungen
CREATE TABLE IF NOT EXISTS `benutzer_x_einstellungen` (
  `benutzer_id` int(11) NOT NULL,
  `einstellungen_kz` char(3) NOT NULL,
  `wert` int(11),
  PRIMARY KEY (`benutzer_id`,`einstellungen_kz`),
  FOREIGN KEY (`benutzer_id`) REFERENCES `benutzer` (`benutzer_id`),
  FOREIGN KEY (`einstellungen_kz`) REFERENCES `einstellungen` (`kz`)
);

-- Struktur von Tabelle twitter-monitor.keywords
CREATE TABLE IF NOT EXISTS `keywords` (
  `keyword` varchar(50) NOT NULL,
  `benutzer_id` int(11) NOT NULL,
  `aktiv` char(1) DEFAULT 'T' NOT NULL,
  `erstellt_am` datetime default current_timestamp,
  PRIMARY KEY (`keyword`,`benutzer_id`),
  FOREIGN KEY (`benutzer_id`) REFERENCES `benutzer` (`benutzer_id`)
);

-- Struktur von Tabelle twitter-monitor.tweet_autor
CREATE TABLE IF NOT EXISTS `tweet_autor` (
  `autor_id` bigint NOT NULL,
  `name` varchar(15) NOT NULL,
  `screen_name` varchar(20),
  `anzahl_follower` int(11),
  `anzahl_tweets` int(11),
  `profilbild_url` varchar(100),
  PRIMARY KEY (`autor_id`)
);

-- Struktur von Tabelle twitter-monitor.tweets
CREATE TABLE IF NOT EXISTS `tweets` (
  `tweet_id` bigint NOT NULL,
  `autor_id` bigint NOT NULL,
  `text` varchar(160) NOT NULL,
  `anzahl_likes` int(11),
  `anzahl_retweets` int(11),
  `prio` float,
  `standort` text,
  `tweet_datum` datetime,
  `erstellt_am` datetime default current_timestamp,
  PRIMARY KEY (`tweet_id`),
  FOREIGN KEY (`autor_id`) REFERENCES `tweet_autor` (`autor_id`)
);

-- Struktur von Tabelle twitter-monitor.tweet_bilder
CREATE TABLE IF NOT EXISTS `tweet_bilder` (
    `tweet_id` bigint NOT NULL,
    `url` varchar(100) NOT NULL,
    PRIMARY KEY (`tweet_id`, `url`),
    FOREIGN KEY (`tweet_id`) REFERENCES `tweets` (`tweet_id`)
);

-- Struktur von Tabelle twitter-monitor.tweets_x_keywords
CREATE TABLE IF NOT EXISTS `tweets_x_keywords` (
  `tweet_id` bigint NOT NULL,
  `keyword` varchar(50) NOT NULL,
  PRIMARY KEY (`tweet_id`, `keyword`),
  FOREIGN KEY (`keyword`) REFERENCES `keywords` (`keyword`),
  FOREIGN KEY (`tweet_id`) REFERENCES `tweets` (`tweet_id`) ON DELETE CASCADE
);

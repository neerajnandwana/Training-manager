
CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `emp` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `level` varchar(255) NOT NULL,
  `mgr_id` varchar(255) DEFAULT NULL,
  `team_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `training` (
  `id` varchar(255) NOT NULL,
  `t_title` varchar(255) NOT NULL,
  `t_desc` varchar(1024) DEFAULT NULL,
  `t_kind` varchar(255) NOT NULL,
  `t_mode` varchar(255) NOT NULL,
  `level_allowed` varchar(255) DEFAULT NULL,
  `num_hrs` int(11) DEFAULT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_title_UNIQUE` (`t_title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `training_meta` (
  `t_id` varchar(255) NOT NULL,
  `attendees_id` varchar(255) NOT NULL,
  `feedback` varchar(1024) DEFAULT NULL,
  `hrs_complete` int(11) DEFAULT NULL,
  `rating` int(11) DEFAULT NULL,
  `trainer_feedback` varchar(1024) DEFAULT NULL,
  `attend` varchar(45) NOT NULL,
  UNIQUE KEY `attendees_id_UNIQUE` (`attendees_id`),
  KEY `t_id_idx` (`t_id`),
  CONSTRAINT `t_id` FOREIGN KEY (`t_id`) REFERENCES `training` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



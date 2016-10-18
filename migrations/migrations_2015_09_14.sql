CREATE TABLE `dashboard_details` (
 `dash_key` varchar(50) DEFAULT NULL,
 `url` varchar(200) DEFAULT NULL,
 `display` varchar(50) DEFAULT NULL,
 `exist` varchar(5) DEFAULT 'TRUE',
 `creation_date` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
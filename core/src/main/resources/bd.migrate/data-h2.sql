INSERT INTO users (email, name, password, role, surname)
VALUES ('admin@mail.ru', 'admin',
        '$2y$12$chRoa9K29c6vDp6vAkGawOGWcneON2hQk1S6g43.2ZE28G1DYuKuG',
        'ROLE_ADMIN', 'adminov'),
       ('user@mail.ru', 'user',
        '$2y$12$chRoa9K29c6vDp6vAkGawOGWcneON2hQk1S6g43.2ZE28G1DYuKuG',
        'ROLE_USER', 'userov');

INSERT INTO certificates (name, description, price, duration, create_date, last_update_date)
VALUES ('Certificate1', 'Description1', 177.99, 138, '2020-02-03 09:23:05', '2021-01-11 09:35:38'),
       ('Certificate2', 'Description2', 137.51, 359, '2020-01-10 01:42:38', '2020-09-01 08:18:44'),
       ('Certificate3', 'Description3', 141.2, 220, '2020-03-07 13:01:29', '2021-01-16 10:37:31'),
       ('Certificate4', 'Description4', 146.29, 127, '2020-06-24 14:33:55', '2020-10-13 20:28:33'),
       ('Certificate5', 'Description5', 48.52, 6, '2020-07-05 15:09:40', '2020-12-26 15:11:38'),
       ('Certificate6', 'Description6', 105.44, 306, '2020-07-13 18:29:56', '2020-08-28 13:16:40'),
       ('Certificate7', 'Description7', 47.64, 319, '2020-06-16 19:24:58', '2020-12-29 15:40:51'),
       ('Certificate8', 'Description8', 173.15, 49, '2020-05-23 12:32:34', '2020-10-27 09:14:33'),
       ('Certificate9', 'Description9', 10.17, 96, '2020-02-13 21:43:03', '2020-09-12 18:21:07'),
       ('Certificate10', 'Description10', 46.55, 116, '2020-06-04 15:40:39', '2020-10-20 22:17:59');

insert into tags (name)
values ('#tag1'),
       ('#tag2'),
       ('#tag3'),
       ('#tag4'),
       ('#tag5'),
       ('#tag6'),
       ('#tag7'),
       ('#tag8'),
       ('#tag9'),
       ('#tag10');

insert into certificates_tags (certificates_id, tags_id)
values (1, 1),
       (2, 1),
       (3, 3),
       (4, 7),
       (5, 10),
       (6, 5),
       (7, 2),
       (8, 4),
       (9, 9),
       (10, 2),
       (9, 3),
       (5, 8),
       (3, 9),
       (4, 1),
       (10, 6);

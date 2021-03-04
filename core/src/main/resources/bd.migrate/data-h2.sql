INSERT INTO users (email, name, password, role, surname)
VALUES ('admin@mail.ru', 'admin',
        '$2y$12$chRoa9K29c6vDp6vAkGawOGWcneON2hQk1S6g43.2ZE28G1DYuKuG',
        'ROLE_ADMIN', 'adminov'),
       ('user@mail.ru', 'user',
        '$2y$12$chRoa9K29c6vDp6vAkGawOGWcneON2hQk1S6g43.2ZE28G1DYuKuG',
        'ROLE_USER', 'userov');
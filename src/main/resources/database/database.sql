
CREATE TABLE IF NOT EXISTS guests (
    id_guest SERIAL PRIMARY KEY,
    guest_name TEXT,
    message TEXT,
    visit_date TEXT
);

INSERT INTO guests
VALUES (DEFAULT, 'Szymon', 'Message', '01-12-2018');
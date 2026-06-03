CREATE DATABASE IF NOT EXISTS biblioteca;
USE biblioteca;

CREATE TABLE autori (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nume VARCHAR(50) NOT NULL,
                        prenume VARCHAR(50) NOT NULL
);

CREATE TABLE carti (
                       isbn VARCHAR(20) PRIMARY KEY,
                       titlu VARCHAR(100) NOT NULL,
                       autor_id INT,
                       sectiune VARCHAR(30),
                       disponibila BOOLEAN DEFAULT TRUE,
                       FOREIGN KEY (autor_id) REFERENCES autori(id)
);

CREATE TABLE cititori (
                          cnp VARCHAR(13) PRIMARY KEY,
                          nume VARCHAR(100) NOT NULL
);

CREATE TABLE imprumuturi (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             cititor_cnp VARCHAR(13),
                             carte_isbn VARCHAR(20),
                             data_imprumut DATE NOT NULL,
                             data_returnare DATE,
                             FOREIGN KEY (cititor_cnp) REFERENCES cititori(cnp),
                             FOREIGN KEY (carte_isbn) REFERENCES carti(isbn)
);
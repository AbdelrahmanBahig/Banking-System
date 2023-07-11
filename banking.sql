Create DATABASE banking;
use banking;
CREATE TABLE customer (

    CustomerID INT AUTO_INCREMENT PRIMARY KEY ,
    username VARCHAR(25) NOT NULL,
    password VARCHAR(15) NOT NULL,
    CustomerAddress VARCHAR(25),
    CustomerEmail VARCHAR(25)
);
INSERT INTO customer (username, password,CustomerAddress,CustomerEmail) values
 ('mohamed','12345','cairo','mohamed@gmail'),('Ahmed','12345','alex','ahmed@gmail'),
 ('12345','1111','giza','@gmail'),('ali','0000','hurgada','ali@gmail');
CREATE TABLE BankAccount (
    BankAccountID INT AUTO_INCREMENT PRIMARY KEY,
    BACreationDate DATETIME,
    BACurrentBalance INT,
    CustomerID INT,
    FOREIGN KEY (CustomerID)
        REFERENCES customer (CustomerID)
);
ALTER TABLE BankAccount MODIFY COLUMN  BankAccountID INT auto_increment ;
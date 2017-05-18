use master
go
IF  EXISTS (SELECT name FROM sys.databases WHERE name = 'db_CrudJava')
	BEGIN
		ALTER DATABASE db_CrudJava SET SINGLE_USER WITH ROLLBACK IMMEDIATE
		DROP DATABASE db_CrudJava
	END
go
create database db_CrudJava	
go
use db_CrudJava
go

CREATE TABLE Pessoa
(
	Id_P INT PRIMARY KEY IDENTITY,
	Nome VARCHAR(50) NOT NULL,
	Sexo CHAR(1) NOT NULL,
	DataNascimento DATE,
	Empregado	BIT,
	Salario	SMALLMONEY,
	Foto VARCHAR(20)
)

INSERT INTO Pessoa VALUES	 ('Mikey Mouse', 'M', '24/08/1996', 1, 1200, '1Mikey.png')
							,('Adalina Fer', 'F', '21/04/1996', 1, 2000, null)
							,('Vortz Adman', 'M', null,			0, null, null)
	
SELECT * FROM Pessoa 

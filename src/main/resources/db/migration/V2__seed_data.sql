INSERT INTO addresses (cep, number, complement, street, neighborhood, city, state)
VALUES
('01001000', '100', 'Apto 101', 'Rua A', 'Bairro A', 'São Paulo', 'SP'),
('02002000', '200', NULL, 'Rua B', 'Bairro B', 'São Paulo', 'SP'),
('03003000', '300', 'Casa', 'Rua C', 'Bairro C', 'Rio de Janeiro', 'RJ'),
('04004000', '400', NULL, 'Rua D', 'Bairro D', 'Belo Horizonte', 'MG'),
('05005000', '500', 'Bloco 5', 'Rua E', 'Bairro E', 'Curitiba', 'PR');


INSERT INTO customers (name, email, cpf, phone, birth_date, address_id)
VALUES
('João Silva', 'joao.silva@example.com', '12345678901', '11999999999', '1985-06-15', 1),
('Maria Santos', 'maria.santos@example.com', '23456789012', '21988888888', '1990-03-22', 2),
('Carlos Oliveira', 'carlos.oliveira@example.com', '34567890123', '31977777777', '1982-11-05', 3),
('Ana Souza', 'ana.souza@example.com', '45678901234', '31333333333', '1995-01-17', 4),
('Pedro Lima', 'pedro.lima@example.com', '56789012345', '41922222222', '1988-09-09', 5);
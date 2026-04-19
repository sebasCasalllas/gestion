CREATE TABLE animals (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    birth_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vaccines (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    frequency_days INTEGER NOT NULL CHECK (frequency_days > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vaccination_records (
    id UUID PRIMARY KEY,
    animal_id UUID NOT NULL,
    vaccine_id UUID NOT NULL,
    application_date DATE NOT NULL,
    next_due_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_animal FOREIGN KEY(animal_id) REFERENCES animals(id),
    CONSTRAINT fk_vaccine FOREIGN KEY(vaccine_id) REFERENCES vaccines(id)
);

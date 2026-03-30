CREATE TABLE cards (
    id UUID PRIMARY KEY,
    holder_id UUID NOT NULL UNIQUE,
    product_id UUID NOT NULL,
    masked_number VARCHAR(19) NOT NULL UNIQUE,
    brand VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

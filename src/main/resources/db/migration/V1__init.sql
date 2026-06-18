CREATE
    TYPE account_type AS ENUM(
        'CUSTOMER',
        'PROVIDER'
    );

CREATE
    TABLE
        usr(
            id UUID NOT NULL,
            first_name TEXT NOT NULL,
            last_name TEXT NOT NULL,
            full_name TEXT NOT NULL,
            email TEXT UNIQUE NOT NULL,
            phone_number TEXT NOT NULL,
            account_type account_type NOT NULL,
            created_at TIMESTAMPTZ NOT NULL,
            updated_at TIMESTAMPTZ NOT NULL,
            CONSTRAINT pk_usr PRIMARY KEY(id),
            CONSTRAINT check_email_format CHECK(
                email ~ '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$'
            )
        );

CREATE
    TABLE
        provider(
            id UUID NOT NULL,
            company_name TEXT NOT NULL,
            specialties TEXT NOT NULL,
            bio TEXT NOT NULL,
            city TEXT NOT NULL,
            street TEXT NOT NULL,
            building_number INTEGER NOT NULL,
            complement TEXT NOT NULL,
            CONSTRAINT pk_provider PRIMARY KEY(id),
            CONSTRAINT fk_usr FOREIGN KEY(id) REFERENCES usr(id)
        );

CREATE
    TYPE appointment_status AS ENUM(
        'SCHEDULED',
        'CONFIRMED',
        'CANCELLED',
        'COMPLETED',
        'NO_SHOW'
    );

CREATE
    TABLE
        appointment(
            id UUID NOT NULL,
            provider_id UUID NOT NULL,
            customer_id UUID NOT NULL,
            starts_at TIMESTAMPTZ NOT NULL,
            ends_at TIMESTAMPTZ NOT NULL,
            status appointment_status NOT NULL,
            created_at TIMESTAMPTZ NOT NULL,
            updated_at TIMESTAMPTZ NOT NULL,
            CONSTRAINT pk_appointment PRIMARY KEY(id),
            CONSTRAINT fk_provider FOREIGN KEY(provider_id) REFERENCES provider(id),
            CONSTRAINT fk_customer FOREIGN KEY(customer_id) REFERENCES usr(id)
        );

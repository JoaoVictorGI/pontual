CREATE
    TYPE account_type AS enum(
        'CUSTOMER',
        'SERVICE_PROVIDER'
    );

CREATE
    TABLE
        usr(
            id uuid UNIQUE NOT NULL,
            first_name text NOT NULL,
            last_name text NOT NULL,
            full_name text NOT NULL,
            email text UNIQUE NOT NULL,
            phone_number text UNIQUE NOT NULL,
            account_type account_type NOT NULL,
            created_at timestamptz NOT NULL,
            updated_at timestamptz NOT NULL,
            CONSTRAINT pk_usr PRIMARY KEY(id),
            CONSTRAINT check_email_format CHECK(
                email ~ '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$'
            )
        );

CREATE
    TABLE
        provider(
            id uuid UNIQUE NOT NULL,
            company_name text NOT NULL,
            specialties text NOT NULL,
            bio text NOT NULL,
            city text NOT NULL,
            street text NOT NULL,
            building_number text NOT NULL,
            complement text NOT NULL,
            CONSTRAINT pk_provider PRIMARY KEY(id),
            CONSTRAINT fk_usr FOREIGN KEY(id) REFERENCES usr(id)
        );

CREATE
    TYPE appointment_status AS enum(
        'SCHEDULED',
        'CONFIRMED',
        'CANCELLED',
        'COMPLETED',
        'NO_SHOW'
    );

CREATE
    TABLE
        appointment(
            id uuid UNIQUE NOT NULL,
            provider_id uuid NOT NULL,
            customer_id uuid NOT NULL,
            starts_at timestamptz NOT NULL,
            ends_at timestamptz NOT NULL,
            status appointment_status NOT NULL,
            created_at timestamptz NOT NULL,
            updated_at timestamptz NOT NULL,
            CONSTRAINT pk_appointment PRIMARY KEY(id),
            CONSTRAINT fk_provider FOREIGN KEY(provider_id) REFERENCES provider(id),
            CONSTRAINT fk_customer FOREIGN KEY(customer_id) REFERENCES usr(id),
            CONSTRAINT check_ends_at_is_after_starts_at CHECK(
                ends_at > starts_at
            )
        );

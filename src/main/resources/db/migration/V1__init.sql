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
            professional_id UUID NOT NULL,
            client_id UUID NOT NULL,
            starts_at TIMESTAMPTZ NOT NULL,
            ends_at TIMESTAMPTZ NOT NULL,
            status appointment_status NOT NULL,
            created_at TIMESTAMPTZ NOT NULL,
            updated_at TIMESTAMPTZ NOT NULL,
            CONSTRAINT pk_appointment PRIMARY KEY(id)
        );
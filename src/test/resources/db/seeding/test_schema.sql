INSERT INTO usr (id, first_name, last_name, full_name, email, phone_number, account_type, created_at, updated_at)
VALUES
    ('31fcf919-a5c0-48cf-97a7-48d194b5714f', 'John', 'Doe', 'John Doe', 'john.doe@example.com', '+15551234567', 'PROVIDER', NOW(), NOW()),
    ('fe8836fe-c8ef-4489-a18f-3f56c6527725', 'Jane', 'Smith', 'Jane Smith', 'jane.smith@example.com', '+15557654321', 'CUSTOMER', NOW(), NOW());

INSERT INTO provider (id, company_name, specialties, bio, city, street, building_number, complement)
    VALUES ('31fcf919-a5c0-48cf-97a7-48d194b5714f', 'Doe Wellness Clinic', 'Massage Therapy, Physical Rehabilitation', 'Experienced therapist helping clients recover from injuries and improve mobility.', 'New York', 'Broadway', 123, 'Suite 500');

INSERT INTO appointment (id, provider_id, customer_id, starts_at, ends_at, status, created_at, updated_at)
    VALUES ('83e81d8a-042f-484c-bf94-da323caade51', '31fcf919-a5c0-48cf-97a7-48d194b5714f', 'fe8836fe-c8ef-4489-a18f-3f56c6527725', NOW() + INTERVAL '1 day', NOW() + INTERVAL '1 day 1 hour', 'SCHEDULED', NOW(), NOW());


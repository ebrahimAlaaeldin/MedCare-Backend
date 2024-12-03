-- Insert a SuperAdmin user
INSERT INTO users (
    first_name, 
    last_name, 
    email, 
    password, 
    phone_number, 
    role, 
    username, 
    city, 
    country, 
    street, 
    zip_code, 
    birth_date,
    age,
    created_at
)
VALUES (
    'Super', 
    'Admin', 
    'superadmin@example.com', 
    'password',  
    '0123456789', 
    'SUPER_ADMIN', 
    'superadmin', 
    'Cairo', 
    'Egypt', 
    'Admin Street 123', 
    '12345', 
    '1985-01-01'  ,
    35,
    NOW()
);

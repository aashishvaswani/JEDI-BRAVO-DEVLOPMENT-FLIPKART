-- FLIPFIT Application Database Schema
-- Author: JEDI BRAVO Team
-- Description: Complete database schema for FlipFit Gym Booking Application

-- Drop database if exists and create new
DROP DATABASE IF EXISTS Flipfit_schema;
CREATE DATABASE Flipfit_schema;
USE Flipfit_schema;

-- ============================================
-- Table: Role
-- Description: Stores different user roles
-- ============================================
CREATE TABLE Role (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- Table: User
-- Description: Stores all user information
-- ============================================
CREATE TABLE User (
    user_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(15),
    role_id INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES Role(role_id) ON DELETE RESTRICT
);

-- ============================================
-- Table: GymOwner
-- Description: Stores gym owner specific information
-- ============================================
CREATE TABLE GymOwner (
    owner_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    pan_card VARCHAR(20) UNIQUE,
    aadhar_card VARCHAR(20) UNIQUE,
    gst_number VARCHAR(20),
    is_approved BOOLEAN DEFAULT FALSE,
    approval_date TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- ============================================
-- Table: GymCustomer
-- Description: Stores customer specific information
-- ============================================
CREATE TABLE GymCustomer (
    customer_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    date_of_birth DATE,
    fitness_goal VARCHAR(255),
    membership_start_date DATE,
    membership_end_date DATE,
    is_premium BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- ============================================
-- Table: GymCenter
-- Description: Stores gym center information
-- ============================================
CREATE TABLE GymCenter (
    gym_id VARCHAR(50) PRIMARY KEY,
    owner_id VARCHAR(50) NOT NULL,
    gym_name VARCHAR(100) NOT NULL,
    gym_address VARCHAR(255) NOT NULL,
    city VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(10),
    phone_number VARCHAR(15),
    email VARCHAR(100),
    total_slots INT DEFAULT 0,
    is_approved BOOLEAN DEFAULT FALSE,
    approval_date TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES GymOwner(owner_id) ON DELETE CASCADE
);

-- ============================================
-- Table: GymSlot
-- Description: Stores gym slot timings
-- ============================================
CREATE TABLE GymSlot (
    slot_id VARCHAR(50) PRIMARY KEY,
    gym_id VARCHAR(50) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    total_seats INT NOT NULL,
    available_seats INT NOT NULL,
    price DECIMAL(10, 2) DEFAULT 0.00,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (gym_id) REFERENCES GymCenter(gym_id) ON DELETE CASCADE,
    CONSTRAINT check_seats CHECK (available_seats >= 0 AND available_seats <= total_seats),
    CONSTRAINT check_time CHECK (start_time < end_time)
);

-- ============================================
-- Table: Booking
-- Description: Stores booking information
-- ============================================
CREATE TABLE Booking (
    booking_id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    slot_id VARCHAR(50) NOT NULL,
    booking_date DATE NOT NULL,
    booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    booking_status ENUM('CONFIRMED', 'CANCELLED', 'PENDING', 'COMPLETED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES GymCustomer(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (slot_id) REFERENCES GymSlot(slot_id) ON DELETE CASCADE,
    CONSTRAINT unique_booking UNIQUE (customer_id, slot_id, booking_date)
);

-- ============================================
-- Table: Payment
-- Description: Stores payment transaction information
-- ============================================
CREATE TABLE Payment (
    payment_id VARCHAR(50) PRIMARY KEY,
    booking_id VARCHAR(50) NOT NULL,
    customer_id VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method ENUM('CREDIT_CARD', 'DEBIT_CARD', 'UPI', 'NET_BANKING', 'CASH') NOT NULL,
    payment_status ENUM('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED') DEFAULT 'PENDING',
    transaction_id VARCHAR(100) UNIQUE,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES Booking(booking_id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES GymCustomer(customer_id) ON DELETE CASCADE
);

-- ============================================
-- Table: WaitList
-- Description: Stores waitlist for fully booked slots
-- ============================================
CREATE TABLE WaitList (
    waitlist_id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    slot_id VARCHAR(50) NOT NULL,
    requested_date DATE NOT NULL,
    priority INT DEFAULT 0,
    status ENUM('WAITING', 'ALLOCATED', 'EXPIRED', 'CANCELLED') DEFAULT 'WAITING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES GymCustomer(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (slot_id) REFERENCES GymSlot(slot_id) ON DELETE CASCADE
);

-- ============================================
-- Table: Notification
-- Description: Stores user notifications
-- ============================================
CREATE TABLE Notification (
    notification_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    title VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    notification_type ENUM('BOOKING', 'PAYMENT', 'APPROVAL', 'CANCELLATION', 'REMINDER', 'GENERAL') NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- ============================================
-- Table: Registration
-- Description: Stores registration requests
-- ============================================
CREATE TABLE Registration (
    registration_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    registration_type ENUM('GYM_OWNER', 'GYM_CENTER', 'CUSTOMER') NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    submitted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_date TIMESTAMP NULL,
    reviewed_by VARCHAR(50),
    remarks TEXT,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- ============================================
-- Insert Initial Data
-- ============================================

-- Insert Roles
INSERT INTO Role (role_name, description) VALUES
('ADMIN', 'System Administrator with full access'),
('GYM_OWNER', 'Gym owner who manages gym centers'),
('CUSTOMER', 'Customer who books gym slots');

-- Insert Sample Admin User
INSERT INTO User (user_id, name, email, password, address, phone_number, role_id) VALUES
('U001', 'Admin User', 'admin@flipfit.com', 'admin123', 'Admin Office, Mumbai', '9999999999', 1);

-- ============================================
-- Create Indexes for Performance
-- ============================================

-- User table indexes
CREATE INDEX idx_user_email ON User(email);
CREATE INDEX idx_user_role ON User(role_id);

-- GymCenter table indexes
CREATE INDEX idx_gym_owner ON GymCenter(owner_id);
CREATE INDEX idx_gym_city ON GymCenter(city);

-- Booking table indexes
CREATE INDEX idx_booking_customer ON Booking(customer_id);
CREATE INDEX idx_booking_slot ON Booking(slot_id);
CREATE INDEX idx_booking_date ON Booking(booking_date);
CREATE INDEX idx_booking_status ON Booking(booking_status);

-- Payment table indexes
CREATE INDEX idx_payment_booking ON Payment(booking_id);
CREATE INDEX idx_payment_customer ON Payment(customer_id);
CREATE INDEX idx_payment_status ON Payment(payment_status);

-- Notification table indexes
CREATE INDEX idx_notification_user ON Notification(user_id);
CREATE INDEX idx_notification_read ON Notification(is_read);

-- ============================================
-- Create Views
-- ============================================

-- View: Active Bookings with Customer and Gym Details
CREATE VIEW vw_active_bookings AS
SELECT 
    b.booking_id,
    b.booking_date,
    b.booking_status,
    u.name AS customer_name,
    u.email AS customer_email,
    gc.gym_name,
    gc.gym_address,
    gs.start_time,
    gs.end_time,
    gs.price
FROM Booking b
JOIN GymCustomer gcu ON b.customer_id = gcu.customer_id
JOIN User u ON gcu.user_id = u.user_id
JOIN GymSlot gs ON b.slot_id = gs.slot_id
JOIN GymCenter gc ON gs.gym_id = gc.gym_id
WHERE b.booking_status IN ('CONFIRMED', 'PENDING');

-- View: Gym Owner Dashboard
CREATE VIEW vw_gym_owner_dashboard AS
SELECT 
    go.owner_id,
    u.name AS owner_name,
    gc.gym_id,
    gc.gym_name,
    gc.city,
    COUNT(DISTINCT gs.slot_id) AS total_slots,
    COUNT(DISTINCT b.booking_id) AS total_bookings,
    COALESCE(SUM(p.amount), 0) AS total_revenue
FROM GymOwner go
JOIN User u ON go.user_id = u.user_id
LEFT JOIN GymCenter gc ON go.owner_id = gc.owner_id
LEFT JOIN GymSlot gs ON gc.gym_id = gs.gym_id
LEFT JOIN Booking b ON gs.slot_id = b.slot_id
LEFT JOIN Payment p ON b.booking_id = p.booking_id
GROUP BY go.owner_id, u.name, gc.gym_id, gc.gym_name, gc.city;

-- ============================================
-- Schema Creation Complete
-- ============================================
SELECT 'FlipFit Schema Created Successfully!' AS Status;

package com.flipfit.client;

import com.flipfit.bean.GymUser;
import com.flipfit.dao.GymUserDAO;
import com.flipfit.dao.GymOwnerDAO;
import com.flipfit.dao.GymCustomerDAO;
import com.flipfit.dao.GymAdminDAO;
import com.flipfit.dao.impl.GymUserDAOImpl;
import com.flipfit.dao.impl.GymOwnerDAOImpl;
import com.flipfit.dao.impl.GymCustomerDAOImpl;
import com.flipfit.dao.impl.GymAdminDAOImpl;
import com.flipfit.enums.Role;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Client application demonstrating DAO operations
 * This class shows how to use the DAO interfaces to perform CRUD operations
 */
public class DAOClientApp {
    
    private static GymUserDAO userDAO = new GymUserDAOImpl();
    private static GymOwnerDAO ownerDAO = new GymOwnerDAOImpl();
    private static GymCustomerDAO customerDAO = new GymCustomerDAOImpl();
    private static GymAdminDAO adminDAO = new GymAdminDAOImpl();
    private static Scanner scanner = new Scanner(System.in);
    
    private static GymUser currentUser = null;
    
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("    Welcome to FlipFit Application");
        System.out.println("===========================================\n");
        
        boolean running = true;
        
        while (running) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Customer Login");
            System.out.println("2. Gym Owner Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Register New User");
            System.out.println("5. Exit");
            System.out.println("================================");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    customerLogin();
                    break;
                case 2:
                    gymOwnerLogin();
                    break;
                case 3:
                    adminLogin();
                    break;
                case 4:
                    registerUser();
                    break;
                case 5:
                    running = false;
                    System.out.println("\n✓ Thank you for using FlipFit!");
                    break;
                default:
                    System.out.println("✗ Invalid choice!");
            }
        }
        
        scanner.close();
    }
    
    /**
     * Customer login
     */
    private static void customerLogin() {
        System.out.println("\n========== CUSTOMER LOGIN ==========");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        currentUser = userDAO.authenticateUser(email, password);
        
        if (currentUser != null && currentUser.getRole() == Role.CUSTOMER) {
            System.out.println("✓ Login successful! Welcome " + currentUser.getName());
            customerMenu();
        } else {
            System.out.println("✗ Invalid credentials or not a customer account!");
        }
        currentUser = null;
    }
    
    /**
     * Gym Owner login
     */
    private static void gymOwnerLogin() {
        System.out.println("\n========== GYM OWNER LOGIN ==========");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        currentUser = userDAO.authenticateUser(email, password);
        
        if (currentUser != null && currentUser.getRole() == Role.GYM_OWNER) {
            System.out.println("✓ Login successful! Welcome " + currentUser.getName());
            gymOwnerMenu();
        } else {
            System.out.println("✗ Invalid credentials or not a gym owner account!");
        }
        currentUser = null;
    }
    
    /**
     * Admin login
     */
    private static void adminLogin() {
        System.out.println("\n========== ADMIN LOGIN ==========");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        currentUser = userDAO.authenticateUser(email, password);
        
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            System.out.println("✓ Login successful! Welcome Admin " + currentUser.getName());
            adminMenu();
        } else {
            System.out.println("✗ Invalid credentials or not an admin account!");
        }
        currentUser = null;
    }
    
    /**
     * Register new user
     */
    private static void registerUser() {
        System.out.println("\n========== USER REGISTRATION ==========");
        
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        
        System.out.println("\nSelect Role:");
        System.out.println("1. Customer");
        System.out.println("2. Gym Owner");
        System.out.print("Enter choice: ");
        int roleChoice = scanner.nextInt();
        scanner.nextLine();
        
        Role role = (roleChoice == 2) ? Role.GYM_OWNER : Role.CUSTOMER;
        
        // Generate user ID
        String userId = "USR" + System.currentTimeMillis();
        
        GymUser user = new GymUser();
        user.setUserId(userId);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setAddress(address);
        user.setRole(role);
        
        boolean result = userDAO.insertUser(user);
        
        if (result) {
            System.out.println("✓ Registration successful! You can now login.");
        } else {
            System.out.println("✗ Registration failed!");
        }
    }
    
    /**
     * Customer menu after login
     */
    private static void customerMenu() {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("\n========== CUSTOMER MENU ==========");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. View All Gym Centers");
            System.out.println("4. View My Bookings");
            System.out.println("5. Logout");
            System.out.println("===================================");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    displayUser(currentUser);
                    break;
                case 2:
                    updateProfile();
                    break;
                case 3:
                    viewAllGymCenters();
                    break;
                case 4:
                    System.out.println("My Bookings feature - Coming soon!");
                    break;
                case 5:
                    loggedIn = false;
                    System.out.println("✓ Logged out successfully!");
                    break;
                default:
                    System.out.println("✗ Invalid choice!");
            }
        }
    }
    
    /**
     * Gym Owner menu after login
     */
    private static void gymOwnerMenu() {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("\n========== GYM OWNER MENU ==========");
            System.out.println("1. View Profile");
            System.out.println("2. Add Gym Center");
            System.out.println("3. View My Gym Centers");
            System.out.println("4. Add Gym Slots");
            System.out.println("5. View Approval Status");
            System.out.println("6. Logout");
            System.out.println("====================================");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    displayUser(currentUser);
                    break;
                case 2:
                    System.out.println("Add Gym Center feature - Coming soon!");
                    break;
                case 3:
                    System.out.println("My Gym Centers feature - Coming soon!");
                    break;
                case 4:
                    System.out.println("Add Gym Slots feature - Coming soon!");
                    break;
                case 5:
                    System.out.println("Approval Status feature - Coming soon!");
                    break;
                case 6:
                    loggedIn = false;
                    System.out.println("✓ Logged out successfully!");
                    break;
                default:
                    System.out.println("✗ Invalid choice!");
            }
        }
    }
    
    /**
     * Admin menu after login
     */
    private static void adminMenu() {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("\n========== ADMIN MENU ==========");
            System.out.println("1. View System Statistics");
            System.out.println("2. View All Users");
            System.out.println("3. Approve Gym Owners");
            System.out.println("4. Approve Gym Centers");
            System.out.println("5. View All Gym Centers");
            System.out.println("6. Logout");
            System.out.println("================================");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    viewSystemStatistics();
                    break;
                case 2:
                    viewAllUsers();
                    break;
                case 3:
                    approveGymOwnerMenu();
                    break;
                case 4:
                    System.out.println("Approve Gym Centers feature - Coming soon!");
                    break;
                case 5:
                    viewAllGymCenters();
                    break;
                case 6:
                    loggedIn = false;
                    System.out.println("✓ Logged out successfully!");
                    break;
                default:
                    System.out.println("✗ Invalid choice!");
            }
        }
    }
    
    /**
     * Update profile
     */
    private static void updateProfile() {
        System.out.println("\n--- Update Profile ---");
        
        System.out.print("Enter new name (or press Enter to skip): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            currentUser.setName(name);
        }
        
        System.out.print("Enter new address (or press Enter to skip): ");
        String address = scanner.nextLine();
        if (!address.isEmpty()) {
            currentUser.setAddress(address);
        }
        
        boolean result = userDAO.updateUser(currentUser);
        
        if (result) {
            System.out.println("✓ Profile updated successfully!");
        } else {
            System.out.println("✗ Failed to update profile!");
        }
    }
    
    /**
     * Approve gym owner menu
     */
    private static void approveGymOwnerMenu() {
        System.out.println("\n--- Pending Gym Owner Approvals ---");
        var owners = ownerDAO.getPendingGymOwners();
        
        if (owners.isEmpty()) {
            System.out.println("No pending approvals!");
            return;
        }
        
        System.out.println("Total Pending: " + owners.size());
        System.out.print("\nEnter Owner ID to approve (or 0 to cancel): ");
        String ownerId = scanner.nextLine();
        
        if (!ownerId.equals("0")) {
            boolean result = ownerDAO.approveGymOwner(ownerId);
            
            if (result) {
                System.out.println("✓ Gym Owner approved successfully!");
            } else {
                System.out.println("✗ Failed to approve gym owner!");
            }
        }
    }
    
    
    /**
     * View all users (Admin only)
     */
    private static void viewAllUsers() {
        System.out.println("\n--- All Users ---");
        List<GymUser> users = userDAO.getAllUsers();
        
        if (users.isEmpty()) {
            System.out.println("No users found!");
        } else {
            System.out.println("Total Users: " + users.size());
            for (GymUser user : users) {
                System.out.println("ID: " + user.getUserId() + " | Name: " + user.getName() + 
                                 " | Email: " + user.getEmail() + " | Role: " + user.getRole());
            }
        }
    }
    
    /**
     * View system statistics
     */
    private static void viewSystemStatistics() {
        System.out.println("\n========== SYSTEM STATISTICS ==========");
        Map<String, Integer> stats = adminDAO.getSystemStatistics();
        
        System.out.println("Total Users: " + stats.getOrDefault("total_users", 0));
        System.out.println("Total Gym Owners: " + stats.getOrDefault("total_gym_owners", 0));
        System.out.println("Total Customers: " + stats.getOrDefault("total_customers", 0));
        System.out.println("Total Bookings: " + stats.getOrDefault("total_bookings", 0));
        System.out.println("Total Gym Centers: " + stats.getOrDefault("total_gym_centers", 0));
        System.out.println("========================================");
    }
    
    /**
     * View all gym centers
     */
    private static void viewAllGymCenters() {
        System.out.println("\n--- All Gym Centers ---");
        var centers = adminDAO.getAllGymCenters();
        
        if (centers.isEmpty()) {
            System.out.println("No gym centers found!");
        } else {
            System.out.println("Total Gym Centers: " + centers.size());
            for (var center : centers) {
                System.out.println("\nID: " + center.getCenterId());
                System.out.println("City: " + center.getCenterCity());
                System.out.println("Location: " + center.getCenterLocn());
                System.out.println("-------------------");
            }
        }
    }
    
    /**
     * Helper method to display user information
     */
    private static void displayUser(GymUser user) {
        System.out.println("\n--- User Details ---");
        System.out.println("User ID: " + user.getUserId());
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Address: " + user.getAddress());
        System.out.println("Role: " + user.getRole());
    }
}

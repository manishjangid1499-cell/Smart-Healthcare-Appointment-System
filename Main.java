package com.manish.healthcare;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        System.out.println("=== Smart Healthcare Appointment System (Console) ===");
        System.out.println("Make sure you ran schema.sql and set application.properties.
");

        Patient loggedIn = null;
        PatientDAO patientDAO = new PatientDAO();
        DoctorDAO doctorDAO = new DoctorDAO();
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        while (true) {
            if (loggedIn == null) {
                System.out.println("1) Register  2) Login  3) Exit");
                System.out.print("Choose: ");
                String c = sc.nextLine().trim();
                if ("1".equals(c)) register(patientDAO);
                else if ("2".equals(c)) loggedIn = login(patientDAO);
                else if ("3".equals(c)) break;
                else System.out.println("Invalid choice.
");
            } else {
                System.out.println("\nLogged in as: " + loggedIn.getFullName());
                System.out.println("1) List Doctors  2) Add Doctor  3) Book Appointment  4) My Appointments  5) Cancel Appointment  6) Logout");
                System.out.print("Choose: ");
                String c = sc.nextLine().trim();
                switch (c) {
                    case "1" -> listDoctors(doctorDAO);
                    case "2" -> addDoctor(doctorDAO);
                    case "3" -> book(appointmentDAO, doctorDAO, loggedIn);
                    case "4" -> myAppointments(appointmentDAO, loggedIn);
                    case "5" -> cancel(appointmentDAO, loggedIn);
                    case "6" -> loggedIn = null;
                    default -> System.out.println("Invalid choice.");
                }
            }
        }
        System.out.println("Goodbye.");
    }

    static void register(PatientDAO dao) {
        try {
            System.out.print("Full name: "); String name = sc.nextLine();
            System.out.print("Email: "); String email = sc.nextLine();
            System.out.print("Phone: "); String phone = sc.nextLine();
            System.out.print("Password: "); String pass = sc.nextLine();
            String hash = HashUtil.sha256(pass);
            Patient p = new Patient(name, email, phone, hash);
            boolean ok = dao.addPatient(p);
            System.out.println(ok ? "Registered successfully." : "Registration failed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static Patient login(PatientDAO dao) {
        try {
            System.out.print("Email: "); String email = sc.nextLine();
            System.out.print("Password: "); String pass = sc.nextLine();
            Patient p = dao.findByEmail(email);
            if (p != null && p.getPasswordHash().equals(HashUtil.sha256(pass))) {
                System.out.println("Login successful.\n");
                return p;
            } else {
                System.out.println("Invalid credentials.\n");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    static void listDoctors(DoctorDAO dao) {
        try {
            List<Doctor> docs = dao.getAllDoctors();
            if (docs.isEmpty()) {
                System.out.println("No doctors found.");
            } else {
                System.out.println("--- Doctors ---");
                for (Doctor d : docs) {
                    System.out.printf("#%d | %s | %s | %s%n", d.getDoctorId(), d.getFullName(), d.getSpecialty(), d.getPhone());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void addDoctor(DoctorDAO dao) {
        try {
            System.out.print("Doctor full name: "); String name = sc.nextLine();
            System.out.print("Specialty: "); String spec = sc.nextLine();
            System.out.print("Phone: "); String phone = sc.nextLine();
            boolean ok = dao.addDoctor(new Doctor(name, spec, phone));
            System.out.println(ok ? "Doctor added." : "Failed to add doctor.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void book(AppointmentDAO adao, DoctorDAO ddao, Patient user) {
        try {
            listDoctors(ddao);
            System.out.print("Enter doctor ID to book with: ");
            int did = Integer.parseInt(sc.nextLine());
            System.out.print("Enter date & time (yyyy-MM-dd HH:mm): ");
            String s = sc.nextLine();
            LocalDateTime when = LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            System.out.print("Reason: "); String reason = sc.nextLine();
            boolean ok = adao.book(user.getPatientId(), did, when, reason);
            System.out.println(ok ? "Appointment booked." : "Time conflict or failed to book.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void myAppointments(AppointmentDAO adao, Patient user) {
        try {
            List<String> rows = adao.listByPatient(user.getPatientId());
            if (rows.isEmpty()) System.out.println("No appointments.");
            else {
                System.out.println("--- My Appointments ---");
                rows.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void cancel(AppointmentDAO adao, Patient user) {
        try {
            System.out.print("Enter appointment ID to cancel: ");
            int aid = Integer.parseInt(sc.nextLine());
            boolean ok = adao.cancel(aid, user.getPatientId());
            System.out.println(ok ? "Cancelled." : "Unable to cancel (check ID or status).");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

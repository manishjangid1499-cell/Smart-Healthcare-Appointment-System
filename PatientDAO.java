package com.manish.healthcare;

import java.sql.*;

public class PatientDAO {

    public boolean addPatient(Patient p) throws Exception {
        String sql = "INSERT INTO patients (full_name, email, phone, password_hash) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getFullName());
            ps.setString(2, p.getEmail());
            ps.setString(3, p.getPhone());
            ps.setString(4, p.getPasswordHash());
            return ps.executeUpdate() == 1;
        }
    }

    public Patient findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM patients WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Patient p = new Patient();
                    p.setPatientId(rs.getInt("patient_id"));
                    p.setFullName(rs.getString("full_name"));
                    p.setEmail(rs.getString("email"));
                    p.setPhone(rs.getString("phone"));
                    p.setPasswordHash(rs.getString("password_hash"));
                    return p;
                }
            }
        }
        return null;
    }
}

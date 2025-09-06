package com.manish.healthcare;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public boolean book(int patientId, int doctorId, LocalDateTime when, String reason) throws Exception {
        // Simple conflict check: same doctor & exact same datetime
        String conflictSql = "SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appointment_datetime=? AND status='BOOKED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement cps = conn.prepareStatement(conflictSql)) {
            cps.setInt(1, doctorId);
            cps.setTimestamp(2, Timestamp.valueOf(when));
            try (ResultSet rs = cps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // conflict
                }
            }
        }
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_datetime, reason, status) VALUES (?, ?, ?, ?, 'BOOKED')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setTimestamp(3, Timestamp.valueOf(when));
            ps.setString(4, reason);
            return ps.executeUpdate() == 1;
        }
    }

    public List<String> listByPatient(int patientId) throws Exception {
        String sql = "SELECT a.appointment_id, a.appointment_datetime, a.status, a.reason, d.full_name AS doctor_name " +
                     "FROM appointments a JOIN doctors d ON a.doctor_id = d.doctor_id " +
                     "WHERE a.patient_id=? ORDER BY a.appointment_datetime DESC";
        List<String> res = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                while (rs.next()) {
                    LocalDateTime dt = rs.getTimestamp("appointment_datetime").toLocalDateTime();
                    res.add(String.format("#%d | %s | %s | Dr. %s | %s",
                            rs.getInt("appointment_id"),
                            dt.format(fmt),
                            rs.getString("status"),
                            rs.getString("doctor_name"),
                            rs.getString("reason")));
                }
            }
        }
        return res;
    }

    public boolean cancel(int appointmentId, int patientId) throws Exception {
        String sql = "UPDATE appointments SET status='CANCELLED' WHERE appointment_id=? AND patient_id=? AND status='BOOKED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ps.setInt(2, patientId);
            return ps.executeUpdate() == 1;
        }
    }
}

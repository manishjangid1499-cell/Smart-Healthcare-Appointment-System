package com.manish.healthcare;

public class Doctor {
    private int doctorId;
    private String fullName;
    private String specialty;
    private String phone;

    public Doctor() {}

    public Doctor(String fullName, String specialty, String phone) {
        this.fullName = fullName;
        this.specialty = specialty;
        this.phone = phone;
    }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}

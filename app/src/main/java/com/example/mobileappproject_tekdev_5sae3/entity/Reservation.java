package com.example.mobileappproject_tekdev_5sae3.entity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reservation_table")
public class Reservation {


    @PrimaryKey(autoGenerate = true)
    int id_reservation;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @ColumnInfo(name = "first_name")
    String firstName;

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @ColumnInfo(name = "last_name")
    String lastName;

    @ColumnInfo(name = "email")
    String email;

    @ColumnInfo(name = "phone")
    String phone;

    @ColumnInfo(name = "date")
    String date;

    @ColumnInfo(name = "guests")
    int guests;
    @ColumnInfo(name = "duration")
    int duration;

    @ColumnInfo(name = "duration_type")
    String durationType;

    @ColumnInfo(name = "amount")
    double amount;

    @ColumnInfo(name = "payment_method")
    String paymentMethod;

    @ColumnInfo(name = "card_number")
    String cardNumber;

    @ColumnInfo(name = "expiry_date")
    String expiryDate;

    @ColumnInfo(name = "cvv")
    String cvv;

    public Reservation(String firstName, String lastName, String email, String phone, String date, int guests, int duration, String durationType, double amount, String paymentMethod, String cardNumber, String expiryDate, String cvv) {
        this.duration = duration;
        this.durationType = durationType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.date = date;
        this.guests = guests;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public Reservation() {

    }

    public int getId_reservation() {
        return id_reservation;
    }


    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }


    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDate() {
        return date;
    }



    public int getGuests() {
        return guests;
    }
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setId_reservation(int id) {
        this.id_reservation = id;
    }

    public void setName(String name) {
        this.firstName = firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public String getDurationType() {
        return durationType;
    }

    public void setDurationType(String durationType) {
        this.durationType = durationType;
    }
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
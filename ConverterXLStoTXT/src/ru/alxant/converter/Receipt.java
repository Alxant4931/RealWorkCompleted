package ru.alxant.converter;

/**
 * Created by Alxant on 10.03.2018.
 */

/**
 *  PersonalAccount В дневном отчете отсутствует придется брать его из БД по улице Дому и квартире
 *  fullName Берется из документа
 *  10 field
 *
 * */
public class Receipt {
    private String personalAccount;
    private String fullName;
    private Address address;
    private int amount;
    private String date; // DD/MM/YYYY
    private int payNumber;
    private String note;
    private String advancedNote;

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setPersonalAccount(String personalAccount) {
        this.personalAccount = personalAccount;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setAdvancedNote(String advancedNote) {
        this.advancedNote = advancedNote;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "personalAccount='" + personalAccount + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address=" + address +
                ", amount='" + amount + '\'' +
                ", date='" + date + '\'' +
                ", payNumber='" + payNumber + '\'' +
                ", note='" + note + '\'' +
                ", advancedNote='" + advancedNote + '\'' +
                '}';
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPayNumber(int payNumber) {
        this.payNumber = payNumber;
    }


    public String getPersonalAccount() {
        return personalAccount;
    }

    public String getFullName() {
        return fullName;
    }

    public int getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public int getPayNumber() {
        return payNumber;
    }

    public String getNote() {
        return note;
    }

    public String getAdvancedNote() {
        return advancedNote;
    }
}

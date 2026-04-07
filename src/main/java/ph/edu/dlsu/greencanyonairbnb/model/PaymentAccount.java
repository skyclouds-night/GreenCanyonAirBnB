package ph.edu.dlsu.greencanyonairbnb.model;

import java.sql.Blob;

public class PaymentAccount {
    private int id;
    private int adminId;
    private String method;
    private String accountName;
    private String accountNumber;
    private  byte[] qrImage;

    public PaymentAccount(int id, int adminId, String method, String accountName, String accountNumber, byte[] qrimg) {
        this.id = id;
        this.adminId = adminId;
        this.method = method;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.qrImage = qrimg;
    }

    public int getAdminId() { return adminId; }
    public String getMethod() { return method; }
    public String getAccountName() { return accountName; }
    public String getAccountNumber() { return accountNumber; }
    public byte[] getQrImage() { return qrImage; }
}
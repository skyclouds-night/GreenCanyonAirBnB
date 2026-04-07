package ph.edu.dlsu.greencanyonairbnb;
import ph.edu.dlsu.greencanyonairbnb.model.Payment;

public class test{
    Payment payment;
    byte[] image;

    public test() {
        super();
        payment = new Payment();
//        String path = "C:\\Users\\Angel\\Downloads\\scenebuilder-master\\GreenCanyonAirBnB(1)\\src\\main\\resources\\e0c8234c-c201-4778-b390-8a9bcee16264.jpg";
//        image = payment.convertImageToByteArray(path);
//        if (image != null) {
//            payment.addMethod(11606, "Angel Espiritu", "09776250280", "Gcash", image);
//            System.out.println("Image converted and uploaded.");
//        } else {
//            System.out.println("The image could not be found or read at: " + path);
//        }
        java.util.List<ph.edu.dlsu.greencanyonairbnb.model.PaymentAccount>accounts = payment.getMethodsByAdmin(11606);
        if (accounts.isEmpty()) {
            System.out.println("No accounts found for this Admin ID.");
        } else {
            System.out.println("Found " + accounts.size() + " account(s).");

            for (ph.edu.dlsu.greencanyonairbnb.model.PaymentAccount acc : accounts) {
                System.out.println("Account Name: " + acc.getAccountName());
                System.out.println("Method: " + acc.getMethod());

                byte[] retrievedImage = acc.getQrImage();
                if (retrievedImage != null && retrievedImage.length > 0) {
                    System.out.println("Image Data: Found (" + retrievedImage.length + " bytes)");
                } else {
                    System.out.println("Image Data: MISSING OR EMPTY");
                }
            }
        }
    }

    public static void main(String[] args) {
        new test();
    }
}
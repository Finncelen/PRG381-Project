package utils;

public class IDGenerator {

    private static int userID = 1;
    private static int materialID = 1;
    private static int supplierID = 1;
    private static int cleanerID = 1;
    private static int issuanceID = 1;

    public static int nextUserID() {
        return userID++;
    }

    public static int nextMaterialID() {
        return materialID++;
    }

    public static int nextSupplierID() {
        return supplierID++;
    }

    public static int nextCleanerID() {
        return cleanerID++;
    }

    public static int nextIssuanceID() {
        return issuanceID++;
    }

}
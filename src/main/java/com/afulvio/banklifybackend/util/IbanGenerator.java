package com.afulvio.banklifybackend.util;

public class IbanGenerator {

    public static String generateUniqueIban(Long clientId) {
        String countryCode = "IT";
        String cin = "60";
        String abi = "12345";
        String cab = "00001";
        String accountSuffix = String.format("%012d", clientId);
        return countryCode + cin + abi + cab + accountSuffix;
    }

}

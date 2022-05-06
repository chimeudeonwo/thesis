package com.bepastem.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

public class IdsGenerator {

    public String generateUserId() throws NoSuchAlgorithmException {
        /*UID userId = new UID();
        return userId.toString();*/

        //initialization of the application
        SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");

        //generate a random number
        String randomNum = Integer.toString(prng.nextInt(1000000000));

        //get its digest
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] result = sha.digest(randomNum.getBytes());

        return randomNum;
    }

    public long generateLongId() throws NoSuchAlgorithmException {
        /*UID userId = new UID();
        return userId.toString();*/

        //initialization of the application
        SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");

        //generate a random number
        String randomNum = Integer.toString(prng.nextInt(1000000000));

        //get its digest
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] result = sha.digest(randomNum.getBytes());

        return Long.parseLong(randomNum);
    }

    public String generateStringId() {
        UUID uniqueID = UUID.randomUUID();
        return uniqueID.toString();
    }
}
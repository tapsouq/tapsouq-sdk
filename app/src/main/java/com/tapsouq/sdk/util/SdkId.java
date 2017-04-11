package com.tapsouq.sdk.util;


import java.util.Random;

public class SdkId {

    //26 letters codes
    private static int[] codes = {1, 493, 31, 525, 90, 37, 403, 23, 267, 82, 736, 59, 336, 49, 755, 7, 332, 7, 708, 83, 609, 28, 950, 8, 56, 388};

    //second letter group
    private static String[] letterGroups = {"abcde", "fghij", "klmno", "pqrst", "uvwxyz"};

    //indexes not positions
    private static int[][] selectedLetters = {
            {1, 7, 12, 15, 17},
            {2, 8, 14, 15, 16},
            {0, 6, 10, 12, 16},
            {3, 5, 8, 14, 17},
            {4, 6, 12, 13, 15}
    };

    public static void main(String[] args)
    {
        //String reqId = "bjkdeisldkfjeidklowz";

        //String reqId = "sddko";//shift 14, group[4,6,12,13,15]
        //String reqId = "ozkdeisldkfjeidklowz";
        //System.out.println(generateSdkId(reqId));

        String str = generateRandomLetters();
        for(int i=0; i<4; i++){
            String letters = getAllLetters(i, "345435344", str);
            System.out.print(letters + "   ");
            System.out.println(generateSdkId(letters));
        }
    }

    public static String generateRandomLetters(){
        Random random = new Random();

        String str = "";
        for(int i=0; i<19; i++){
            str += String.valueOf((char) ('a' + random.nextInt(26)));
        }

        return  str;
    }

    public static String getAllLetters(int action, String reqId, String str){

        long n = Long.parseLong(reqId.substring(action)) % 26;

        return String.valueOf((char) ('a' + n)) + str;
    }

    public static String generateSdkId(String reqId){
        //first letter for shifting
        int shift = reqId.charAt(0) - 'a';

        //second letter for groupNums
        String group = reqId.charAt(1) +"";

        //remaining 18 letters
        reqId = reqId.substring(2);

        //final code
        String sdkId = "";

        for(int i=0; i<5; i++){
            if(letterGroups[i].contains(group)){
                for(int j=0; j<5; j++){
                    char selectedChar = reqId.charAt(selectedLetters[i][j]);
                    int charIndex = selectedChar - 'a';
                    sdkId = sdkId + codes[(charIndex +shift)%26];
                }
                return sdkId;
            }
        }
        return null;
    }
}

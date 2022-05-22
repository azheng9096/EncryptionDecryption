import java.nio.file.*;
import java.io.*;

class Lesson13_EncryptionProject{

    /*
    A. Zheng
    4 Jan 2018

    Algorithm Selection
    1) List the various algorithms you will use as part of your project.

    2) Your grade will be based on the difficulty of the algorithms you choose.
    For example, a working solution with one simple algorithm will earn a 70. A solution with two algorithms combined could earn an 80. A solution with three algorithms combined could earn an 90. A solution with four algorithms combined could earn an 100.

    Solutions that involves more sophisticated algorithms could earn higher points with few algorithms.

    3) Based on your submission here, you will be told your max point values for your algorithm.

    */

    static void writeFile(String file_path, String msg){
        try {
            //Write the encrypted message to a file
            Files.write(Paths.get(file_path), msg.getBytes());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    static char reverseLetter(char letter){

        int ascii;

        //If - Lower Reversing, Else If - Uppercase Reversing, Else - Leaving It As Is (Anything other than alphabets, ex: . , ; "")
        if ((int)letter>=97 && (int)letter<=122){
            ascii=122-((int)letter-97);
        }else if((int)letter>=65 && (int)letter<=90){
            ascii=90-((int)letter-65);
        }else{
            ascii = (int)letter;
        }

        return (char)ascii;
    }

    static String hexToBinary8(int hex){
        String binary = "";
        int left = hex;

        for (int x=7; x>=0; x--){
            binary += String.valueOf(left/(int)(Math.pow(2,x)));
            left = left%(int)(Math.pow(2,x));
        }

        return binary;
    }

    static String encrypt(String msg){
        char letter = ' ';
        int ascii = 0, rNum = 0;

        boolean secondLoop = false;

        String alteredMsg = "";
        int hexDigit1 = 0, hexDigit0 = 0;
        
        //String build = "", build2 = "";

        //ASCII in messages typically won't exceed 255, or even 127. 2 digits for hexadecimal can suffice

        //Loop reverses string, every 2 letters
        for (int i=msg.length()-1; i>=0; i-=2){
            letter = msg.charAt(i);

            //Reverse letters (alphabet order, a=z; b=y; c=x; etc...)
            ascii = (int)reverseLetter(letter);

            //Adds a random number to ascii value of alphabet
            rNum = (int)(Math.floor(Math.random()*5));
            ascii = ascii + rNum;

            //Hex Value, Second Digit (Leftmost Hex Digit)
            hexDigit1 = ascii/16;
            //If greater than 10, convert to A-F ascii value. A = 10 (Hex) -> 65 (Ascii), B = 11 (Hex) -> 66 (Ascii), C = 12 (Hex) -> 67 (Ascii), ... F = 15 (Hex) -> 70 (Ascii).
            if (hexDigit1>=10 && hexDigit1<=15){
                //ASCII Value of letter
                hexDigit1 = hexDigit1 + 55;
            }
            
            //Convert the Second Digit to 8-bit Binary
            alteredMsg += hexToBinary8(hexDigit1);

            //Hex Value, First Digit
            hexDigit0 = ascii%16;

            if(hexDigit0>=10 && hexDigit0<=15){
                hexDigit0 = hexDigit0 + 55;
            }

            //Convert the First Digit to 8-bit Binary
            alteredMsg += hexToBinary8(hexDigit0);


            //Add Random Number
            alteredMsg += rNum;


            if ((i-2)<0 && !secondLoop){
                i = msg.length(); //Don't subtract 2, first letter when you loop second time will not be registered -- accumulator will subtract it.
                secondLoop = true;
            }
        }

        return alteredMsg;
    }


    
    static int asciiBinaryToHex(String binary){
        int ascii = Integer.parseInt(binary, 2);
        return (ascii>=65 && ascii<=70) ? (ascii-55) : ascii;
    }

    static String reverseBinaryAndLetter(String alteredMsg){
        String notBinary = "";

        //Reverse Binary
        for(int a=17; a<=alteredMsg.length(); a+=17){
            int hex = 0;

            int beg = a-17; //index of first character in binary string
            int mid = (a + beg)/2; //get mid index of binary string

            //Second Hex Digit
            String hexDigit1 = alteredMsg.substring(beg, mid);
            hex += asciiBinaryToHex(hexDigit1) * (int)Math.pow(16, 1);

            //First Hex Digit
            String hexDigit0 = alteredMsg.substring(mid, a-1);
            hex += asciiBinaryToHex(hexDigit0);

            //Random Number - every 17th character
            int hexLetter = hex - Integer.parseInt(alteredMsg.substring(a-1,a));

            //Reverse letter
            notBinary += String.valueOf(reverseLetter((char)hexLetter));
        }
        
        return notBinary;
    }

    static String decrypt(String alteredMsg){
        //Reverse Binary
        String notBinary = reverseBinaryAndLetter(alteredMsg);


        String msgDecrypted = "";

        if (notBinary.length()%2==0){
            msgDecrypted += notBinary.substring(notBinary.length()-1);
            notBinary = notBinary.substring(0, notBinary.length()-1);
        }

        /*//If original message is odd, has to start from middle
        k = notBinary.length()/2;*/

        int accumulator = notBinary.length()/2;
        int k = (int)(Math.ceil(notBinary.length()/2.0));

        for (int b=1; b<=notBinary.length(); b++){
            msgDecrypted += notBinary.substring(k-1, k);
            k += notBinary.length()/2;
            if (k > notBinary.length()){
                k %= notBinary.length();
            }
        }

        return msgDecrypted;
    }

    public static void main(String[] args){
        // Get and check path
        String txtPath = "";
        do {
            System.out.println("Specify txt file path: ");
            txtPath = Input.readString();

            Path path = Paths.get(txtPath);
            if (txtPath.length() != 0 && Files.exists(path)) {
                break;
            }

            System.out.println("Invalid txt file path.");
        } while (true);

        System.out.println("\n\n");

        // Message
        String msg = Input.readFile(txtPath);

        // Start encryption program
        System.out.println("Encrypting..\n");
        String alteredMsg = encrypt(msg);
        System.out.println(alteredMsg);
        writeFile("encryptedText.txt", alteredMsg);

        System.out.println("\n\n");

        // Start decryption program
        System.out.println("Decrypting..\n");
        String decryptedMsg = decrypt(alteredMsg);
        System.out.println(decryptedMsg);
    }
}

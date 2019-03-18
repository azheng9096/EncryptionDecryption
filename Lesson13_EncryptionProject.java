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

    public static void main(String[] args){
        String msg = Input.readFile("EncryptionProject.txt");
        //String msg = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.,";

        char letter = ' ';
        int ascii = 0, newAscii = 0;

        int rNum = 0;
        boolean secondLoop = false;

        String alteredMsg = "";
        int hexDigit1 = 0, hexDigit0 = 0;
        int left = 0;
        
        //String build = "", build2 = "";

        //ASCII in messages typically won't exceed 255, or even 127. 2 digits for hexadecimal can suffice

        //Loop reverses string, every 2 letters
        for (int i=msg.length()-1; i>=0; i-=2){
            letter = msg.charAt(i);
            //System.out.println(letter);
            //ascii = (int)letter;

            //Reverse letters (alphabet order, a=z; b=y; c=x; etc...)

            //If - Lower Reversing, Else If - Uppercase Reversing, Else - Leaving It As Is (Anything other than alphabets, ex: . , ; "")
            if ((int)letter>=97 && (int)letter<=122){
                ascii=122-((int)letter-97);
            }else if((int)letter>=65 && (int)letter<=90){
                ascii=90-((int)letter-65);
            }else{
                ascii = (int)letter;
            }
            //Adds a random number to ascii value of alphabet
            rNum = (int)(Math.floor(Math.random()*5));
            ascii = ascii + rNum;
            //System.out.println(ascii + " " + (char)ascii);


            //Hex Value, Second Digit
            hexDigit1 = ascii/16;
            if (hexDigit1>=10 && hexDigit1<=15){
                //ASCII Value of letter
                hexDigit1 = hexDigit1 + 55;
            }
            
            left = hexDigit1;
            for (int x=7; x>=0; x--){
                alteredMsg += String.valueOf(left/(int)(Math.pow(2,x)));
                left = left%(int)(Math.pow(2,x));
            }

            //Hex Value, First Digit
            hexDigit0 = ascii%16;
            //System.out.println(ascii%16);
            //If greater than 10, convert to A-F ascii value. A = 10 (Hex) -> 65 (Ascii), B = 11 (Hex) -> 66 (Ascii), C = 12 (Hex) -> 67 (Ascii), ... F = 15 (Hex) -> 70 (Ascii). 
            if(hexDigit0>=10 && hexDigit0<=15){
                hexDigit0 = hexDigit0 + 55;
            }

            //Convert the First Digit to Binary
            left = hexDigit0;
            for (int x=7; x>=0; x--){
                alteredMsg += String.valueOf(left/(int)(Math.pow(2,x)));
                left = left%(int)(Math.pow(2,x));
            }


            alteredMsg += rNum;


            //letter = (char)ascii;

            if ((i-2)<0 && secondLoop == false){
                i = msg.length(); //Don't subtract 2, first letter when you loop second time will not be registered -- accumulator will subtract it.
                secondLoop = true;
            }

        }

        System.out.println(alteredMsg);
        try {
            //Write the encrypted message to a file
            Files.write(Paths.get("encryptedTest.txt"), alteredMsg.getBytes());
        }
        catch(IOException e) {
            e.printStackTrace();
        }


        // Start decryption program
        int counter = 7;
        int asciiBinary = 0;

        String notBinary = "";

        int powerHex = 0;
        int NegPos1 = -1;
        int hex = 0;

        int hexLetter = 0;

        //Reverse Binary
        for(int a=0; a<alteredMsg.length(); a++){
            if ((a+1)%17!=0 || a==0){
                //System.out.println(Integer.parseInt(alteredMsg.substring(a,a+1))*Math.pow(2,counter) + " " + counter);
                asciiBinary += Integer.parseInt(alteredMsg.substring(a,a+1))*Math.pow(2,counter);

                if (counter==0){
                    counter=8;
                    NegPos1 *= -1;

                    //notBinary += String.valueOf(asciiBinary);
                    if (asciiBinary>=65 && asciiBinary<=70){
                        //notBinary += String.valueOf((char)asciiBinary);
                        hex += (asciiBinary-55)*(int)Math.pow(16,powerHex+NegPos1);
                        
                    }else{
                        //notBinary += String.valueOf(asciiBinary);
                        hex += asciiBinary*(int)Math.pow(16,powerHex+NegPos1);
                    }
                    asciiBinary = 0;
                    //System.out.println(hex);

                    //Will actually change value of powerHex
                    
                    powerHex = powerHex + NegPos1;
                    //System.out.println("Power to: " + powerHex);

                }

                counter--;

            }else{
                //notBinary+=String.valueOf(hex);

                //Random Number
                //notBinary+=alteredMsg.substring(a,a+1);

                //notBinary+=String.valueOf(hex-Integer.parseInt(alteredMsg.substring(a,a+1)));

                hexLetter = hex-Integer.parseInt(alteredMsg.substring(a,a+1));

                if (hexLetter>=97 && hexLetter<=122){
                    hexLetter=122-(hexLetter-97);
                }else if(hexLetter>=65 && hexLetter<=90){
                    hexLetter=90-(hexLetter-65);
                }else{
                    hexLetter = hexLetter;
                }

                notBinary+=String.valueOf((char)hexLetter);

                hex = 0;
            }
            //System.out.println(a);
        }
        //System.out.println(notBinary);

        

        String msgDecrypted = "";

        if (notBinary.length()%2==0){
            msgDecrypted += notBinary.substring(msg.length()-1);
            notBinary = notBinary.substring(0,msg.length()-1);
        }

        /*//If original message is odd, has to start from middle
        k= notBinary.length()/2;*/

        int accumulator = notBinary.length()/2;
        int k = (int)(Math.ceil(notBinary.length()/2.0));

        for (int b=1; b<=notBinary.length(); b++){
            msgDecrypted += notBinary.substring(k-1,k);
            k+=notBinary.length()/2;
            if (k>notBinary.length()){
                k%=notBinary.length();
            }
        }
        System.out.println(msgDecrypted);
        
    }
}
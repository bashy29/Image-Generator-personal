import java.io.Console;
import java.util.Scanner;
public class RleProgram
{
    public static void main(String[] args)
    {
        int choice = 0;
        byte[] imageData = null;
        boolean ifTrue = true;
        Scanner scnr = new Scanner(System.in);
        System.out.println("Welcome to the RLE image encoder!");
        //outprints welcome message
        System.out.println("Displaying Spectrum Image:");
        ConsoleGfx.displayImage(ConsoleGfx.testRainbow);

        while (ifTrue)
        {

            System.out.println("RLE Menu \n -------- \n 0. Exit\n 1. Load File\n 2. Load Test Image\n 3. Read RLE String\n 4. Read RLE Hex String \n 5. Read Data Hex String \n 6. Display Image\n 7. Display RLE String \n 8. Display Hex RLE Data \n 9. Display Hex Flat Data");
            System.out.println(" ");
            System.out.println("Select a Menu Option:");

            choice = scnr.nextInt();

            if (choice == 0)
            {
                break;
            }
            else if (choice == 1)
            {
                System.out.println("Enter name of file to load:");
                String userInput = scnr.next();                      //enter file to load
                imageData = ConsoleGfx.loadFile(userInput);
            }
            else if (choice == 2)
            {
                System.out.println("Test image data loaded.");      //loads test image
                imageData = ConsoleGfx.testImage;
            }
            else if (choice == 3)
            {
                System.out.println("Enter an RLE string to be decoded:");
                String dataString = scnr.next();
                imageData = decodeRle(stringToRle(dataString));         //decodes string
            }
            else if (choice == 4)
            {
                System.out.println("Enter the hex string holding RLE data: ");
                String dataString = scnr.next();
                imageData = decodeRle(stringToData(dataString));

            }
            else if (choice == 5)
            {
                System.out.println("Enter the hex string holding flat data");
                String dataString = scnr.next();
                imageData = stringToData(dataString);               //converts hex string to data

            }
            else if (choice == 6)
            {
                System.out.println("Displaying image...");
                ConsoleGfx.displayImage(imageData);
            }

            else if (choice == 7)
            {
                String dataString = toRleString(encodeRle(imageData));
                System.out.println("RLE representation: " + dataString);            //shows converted to RLE rep
            }
            else if (choice == 8)
            {
                String dataString = toHexString(encodeRle(imageData));
                System.out.println("RLE hex values: " + dataString);
            }
            else if (choice == 9)
            {
                String dataString = toHexString(imageData);
                System.out.println("Flat hex values: " + dataString);
            }
            else
            {
                System.out.println("Error! Invalid input.");  //runs for invalid input
            }
        }
    }
    public static String toHexString(byte[] data)
    {
        String resolution = "";
        for (int i = 0; i < data.length; i++)
        {
            if (data[i] > 9)
            {
                if (data[i] == 10)
                {
                    resolution += "a";                     //method converting to hex string
                }
                else if (data[i] == 11)
                {
                    resolution += "b";
                }
                else if (data[i] == 12)
                {
                    resolution += "c";
                }
                else if (data[i] == 13)
                {
                    resolution += "d";
                }
                else if (data[i] == 14)
                {
                    resolution += "e";
                }
                else if (data[i] == 15)
                {
                    resolution += "f";
                }
            }
            else
            {
                resolution += data[i];
            }
        }
        return resolution;
    }
    public static int countRuns(byte[] flatData)
    {
        int count = 1;
        int count1 = 1;
        for (int i = 0; i < flatData.length - 1; i++)
        {
            if (flatData[i] == flatData[i + 1])
            {
                count1++;                               //counts the amount of different entries
                if (count1 == 15)
                {
                    count+=1;
                    count1 = 1;
                }
            }
            else
            {
                count+=1;
            }
        }
        return count;
    }
    public static byte[] encodeRle(byte[] flatData)
    {
        int count = 1;
        int count1 = 1;
        int count2 = 1;
        int count3 = 0;
        int a = 1;
        int b = 0;
        for (int i = 0; i < flatData.length - 1; i++)
        {
            if (flatData[i] != flatData[i + 1])
            {
                count++;
            }
            else
            {
                count2++;
                if (count2 == 15)
                {
                    count3 = count3 + 1;               //encodes RLE data
                    count2 = 1;
                }
            }
        }
        byte[] resolution = new byte[count * 2 + (count3 * 2)];
        resolution[a] = flatData[0];
        for (int i = 0; i < flatData.length - 1; i++)
        {
            if (count1 == 15)
            {
                a = a + 2;
                resolution[b] = (byte) count1;
                resolution[a] = flatData[i + 1];
                count1 = 1;
                b = b + 2;
            }
            else if (flatData[i] == flatData[i + 1])
            {
                count1++;
            }
            else
            {
                a = a + 2;
                resolution[b] = (byte) count1;
                resolution[a] = flatData[i + 1];
                count1 = 1;
                b = b + 2;
            }

        }
        resolution[b] = (byte) count1;
        resolution[a] = flatData[flatData.length - 1];
        return resolution;
    }
    public static int getDecodedLength(byte[] rleData)
    {
        int length = 0;
        for (int i = 0; i < rleData.length; i++)
        {
            if(i % 2 == 0)                      //gets length of decoded data
            {
                length += rleData[i];
            }
        }
        return length;
    }
    public static byte[] decodeRle(byte[] rleData)
    {
        int length = 0;
        int index = 0;
        for (int i = 0; i < rleData.length; i++)
        {
            if (i % 2 == 0)
            {
                length += rleData[i];
            }
        }
        byte[] resolution = new byte[length];
        for (int i = 0; i < rleData.length; i = i + 2)
        {
            int value = rleData[i + 1];
            int repeats = rleData[i];           //decodes RLE data
            for (int j = 0; j < repeats; j++)
            {
                resolution[index] = (byte) value;
                index++;
            }
        }
        return resolution;
    }
    public static byte[] stringToData(String dataString)
    {
        byte[] resolution = new byte[dataString.length()];
        for (int i = 0; i < dataString.length(); i++)
        {
            if (Character.isDigit(dataString.charAt(i)))
            {
                resolution[i] = (byte) Character.getNumericValue(dataString.charAt(i));
            }
            else
            {
                if (dataString.charAt(i) == 'a')
                {
                    resolution[i] = 10;
                }
                else if (dataString.charAt(i) == 'b')
                {
                    resolution[i] = 11;
                }
                else if (dataString.charAt(i) == 'c')
                {
                    resolution[i] = 12;
                }                                                   //coverts string to rle data
                else if (dataString.charAt(i) == 'd')
                {
                    resolution[i] = 13;
                }
                else if (dataString.charAt(i) == 'e')
                {
                    resolution[i] = 14;
                }
                else if (dataString.charAt(i) == 'f')
                {
                    resolution[i] = 15;
                }
            }
        }
        return resolution;
    }
    public static String toRleString(byte[] rleData)
    {
        int a = 0;
        int b = 1;
        int count = rleData.length / 2;
        String resolution = "";

        for (int i = 0; i < rleData.length - count; i++)
        {
            resolution += rleData[a];
            if (rleData[b] == 10)
            {
                resolution += "a";
            }
            else if (rleData[b] == 11)
            {
                resolution += "b";
            }
            else if (rleData[b] == 12)
            {
                resolution += "c";                             //converts data to rle string
            }
            else if (rleData[b] == 13)
            {
                resolution += "d";
            }
            else if (rleData[b] == 14)
            {
                resolution += "e";
            }
            else if (rleData[b] == 15)
            {
                resolution += "f";
            }
            else
            {
                resolution += rleData[b];
            }
            a = a + 2;
            b = b + 2;
            if (b < rleData.length)
            {
                resolution += ":";
            }
        }
        return resolution;
    }
    public static byte[] stringToRle(String rleString)
    {
        String[] data = rleString.split(":");
        byte[] resolution = new byte[data.length * 2];
        int counter = 0;
        for (int i = 0; i < data.length; i++)
        {
            if (data[i].length() == 3)
            {
                String firstNum = data[i].substring(0, 2);
                resolution[counter++] = Byte.parseByte(firstNum, 10);              //converts string to rle
                String secondNum = data[i].substring(2);
                resolution[counter++] = Byte.parseByte(secondNum, 16);
            }
            else if (data[i].length() == 2)
            {
                String firstNum1 = data[i].substring(0, 1);
                resolution[counter++] = Byte.parseByte(firstNum1, 10);
                String secondNum1 = data[i].substring(1);
                resolution[counter++] = Byte.parseByte(secondNum1, 16);
            }
        }
        return resolution;
    }
}
/*
 * Code that checks if a credit card is valid credit card
 */
public class CardValidator 
{
    private static void CardValidator()
    {
        // Constructor
    }
    
    public static boolean isValid(String cardNumber)
    {
        boolean isValid = false;
        if(cardNumber.length() >= 13 && cardNumber.length() <= 16)
        {
            // 4-Visa  5-MC  37-AMEX  6-Discover
            //if(cardNumber.startsWith("4") || cardNumber.startsWith("5") || cardNumber.startsWith("37") || cardNumber.startsWith("6"))
            if(cardNumber.substring(0,1).equals("4") || cardNumber.substring(0,1).equals("5") || cardNumber.substring(0,2).equals("37") || cardNumber.substring(0,1).equals("6"))
            {
                int sumEven = sumOfDoubleEvenPlace(cardNumber);
                int sumOdd = sumOfOddPlace(cardNumber);                
                double result = (sumEven + sumOdd)%10;
                if(result == 0)
                {
                    isValid = true;
                }
                else
                {
                    // Nothing
                }
            }
            else
            {
                // Nothing
            }
        }
        else
        {
            // Nothing
        }
        return isValid;
    }
        
    private static int sumOfDoubleEvenPlace(String cardNumber)
    {
        int sum = 0;
        char[] number = cardNumber.toCharArray();
        int index = number.length - 2;
        while(index > -1)
        {
            // Passes the numerical value of number instead of ASCII value
            sum = sum + getDigit((number[index]-'0')*2);
            index = index - 2;
        }
        return sum;
    }
    
    private static int sumOfOddPlace(String cardNumber)
    {
        int sum = 0;
        char[] number = cardNumber.toCharArray();
        int index = number.length - 1;
        while(index > -1)
        {
            // Passes the numerical value of number instead of ASCII value
            sum = sum + (number[index] - '0');
            index = index - 2;
        }
        return sum;
    }

    private static int getDigit(int number)
    {
        if(number > 9)
        {
            number = 1 + (number - 10);
        }
        return number;
    }
    
}
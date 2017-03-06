import java.util.Scanner;


public class MostContinuousDigit {

    public static short getMostContinuousDigit(long value) {
        short result = 0, maxResult = 0;
        int count = 0, maxCount = 0;
        while(value > 0) {
            short digit = (short)(value % 10);
            if (digit == result) {
                count++;
            } else {
                count = 1;
                result = digit;
            }
            if (count >= maxCount) {
                maxCount = count;
                maxResult = result;
            }
            value /= 10;
        }
        return maxResult;
    }

    public static void main(String[] argv) {
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            if (scanner.hasNextLong()) {
                long value = scanner.nextLong();
                System.out.println(getMostContinuousDigit(value));
            } else {
                String token = scanner.next();
                if (token.equals("end")) {
                    return;
                } else {
                    System.err.println("invalid: " + token);
                }
            }
        }

    }

}

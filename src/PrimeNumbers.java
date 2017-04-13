
public class PrimeNumbers {

    public static void main(String argv[]) {
        for (int n = 2; n <= 100; n++) {
            if (isPrime(n))
                System.out.print(n+" ");
        }
        System.out.println();
    }

    public static boolean isPrime(int n) {
        int sqrt = (int)Math.sqrt(n);
        for (int i = 2; i <= sqrt; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

}

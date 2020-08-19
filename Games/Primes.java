package Games;

public class Primes {

    public static boolean isPrime(int n) {
        if (n % 2 == 0)
            return false;
        for (int i = 3; i <= n/2; i += 2) {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    public static boolean isPrime2(int n) {
        if (n == 2)
            return true;
        if (n == 3)
            return true;
        if (n % 2 == 0)
            return false;
        if (n % 3 == 0)
            return false;

        int i = 5, w = 2;

        while (i * i <= n) {
            if (n % i == 0)
                return false;

            i += w;
            w = 6 - w;
        }

        return true;
    }

    public static int factorial(int n) {
        int f = 1;
        for (int i = 2; i <= n; i++) {
            f *= i;
        }
        return f;
    }

    public static void main(String[] args) {
        int num1, num2;
        for (int i  = 2; i <= 6; i++) {
            num1 = factorial(i);
            num2 = factorial(i + 1);
            for (int n = 1; n < num2 - num1; n += 2) {
                if (isPrime(num1 + n))
                    System.out.println(i + " " + n / 2);
            }
            System.out.println();
        }
    }

}

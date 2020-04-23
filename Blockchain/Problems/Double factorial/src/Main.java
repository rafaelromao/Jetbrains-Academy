//import java.math.BigInteger;
//
//class Main {
//    public static void main(String[] args) {
//        calcDoubleFactorial(10);
//    }

    public static BigInteger calcDoubleFactorial(int n) {
        switch (n) {
            case 0:
            case 1:
                return BigInteger.ONE;
            default:
                var result = BigInteger.valueOf(n);
                while (true) {
                    n -= 2;
                    if (n <= 0) {
                        break;
                    }
                    result = result.multiply(BigInteger.valueOf(n));
                }
                return result;
        }
    }
//}
import java.util.*;
import java.math.BigInteger;

public class PolynomialSolver {

    // Function to decode the value from a given base
    public static long decodeValue(int base, String value) {
        long decodedValue = 0;
        for (char c : value.toCharArray()) {
            int digit = (c >= '0' && c <= '9') ? c - '0' :
                        (c >= 'A' && c <= 'Z') ? c - 'A' + 10 : 0;  // Supports uppercase letters for bases above 10
            decodedValue = decodedValue * base + digit;
        }
        return decodedValue;
    }

    // Lagrange interpolation to find the constant term c
    public static double lagrangeInterpolation(List<Map.Entry<Integer, Long>> points) {
        double c = 0.0;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            int xi = points.get(i).getKey();
            long yi = points.get(i).getValue();
            double term = yi;

            for (int j = 0; j < n; j++) {
                if (i != j) {
                    int xj = points.get(j).getKey();
                    term *= (0.0 - xj) / (xi - xj);  // Lagrange polynomial multiplication
                }
            }

            c += term;
        }

        return Math.round(c);  // Rounding to nearest integer
    }

    // Function to process the test cases
    public static void processTestCases(List<Map<String, Map<String, String>>> testCases) {
        for (int index = 0; index < testCases.size(); index++) {
            Map<String, Map<String, String>> testCase = testCases.get(index);
            int n = Integer.parseInt(testCase.get("keys").get("n"));
            int k = Integer.parseInt(testCase.get("keys").get("k"));

            List<Map.Entry<Integer, Long>> points = new ArrayList<>();

            // Decode the points (x, y)
            for (Map.Entry<String, Map<String, String>> entry : testCase.entrySet()) {
                if (entry.getKey().equals("keys")) continue;
                int x = Integer.parseInt(entry.getKey());  // x is the key
                int base = Integer.parseInt(entry.getValue().get("base"));
                long y = decodeValue(base, entry.getValue().get("value"));  // Decode y
                points.add(new AbstractMap.SimpleEntry<>(x, y));
            }

            // We need at least k points to solve for the polynomial
            List<Map.Entry<Integer, Long>> selectedPoints = points.subList(0, k);

            // Find the secret constant 'c' using Lagrange Interpolation
            double secret = lagrangeInterpolation(selectedPoints);

            System.out.println("Test Case " + (index + 1) + " Secret: " + secret);

            // For the second test case, check for wrong points
            if (index == 1) {
                List<Map.Entry<Integer, Long>> wrongPoints = new ArrayList<>();
                for (Map.Entry<Integer, Long> point : points) {
                    if (Math.abs(point.getValue() - secret) > 1000) {  // Simple heuristic
                        wrongPoints.add(point);
                    }
                }
                System.out.print("Test Case " + (index + 1) + " Wrong Points: ");
                for (Map.Entry<Integer, Long> wp : wrongPoints) {
                    System.out.print("(" + wp.getKey() + ", " + wp.getValue() + ") ");
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        List<Map<String, Map<String, String>>> testCases = new ArrayList<>();

        // Test Case 1
        Map<String, Map<String, String>> testCase1 = new HashMap<>();
        testCase1.put("keys", Map.of("n", "4", "k", "3"));
        testCase1.put("1", Map.of("base", "10", "value", "4"));
        testCase1.put("2", Map.of("base", "2", "value", "111"));
        testCase1.put("3", Map.of("base", "10", "value", "12"));
        testCase1.put("6", Map.of("base", "4", "value", "213"));
        testCases.add(testCase1);

        // Test Case 2
        Map<String, Map<String, String>> testCase2 = new HashMap<>();
        testCase2.put("keys", Map.of("n", "9", "k", "6"));
        testCase2.put("1", Map.of("base", "10", "value", "28735619723837"));
        testCase2.put("2", Map.of("base", "16", "value", "1A228867F0CA"));
        testCase2.put("3", Map.of("base", "12", "value", "32811A4AA0B7B"));
        testCase2.put("4", Map.of("base", "11", "value", "917978721331A"));
        testCase2.put("5", Map.of("base", "16", "value", "1A22886782E1"));
        testCase2.put("6", Map.of("base", "10", "value", "28735619654702"));
        testCase2.put("7", Map.of("base", "14", "value", "71AB5070CC4B"));
        testCase2.put("8", Map.of("base", "9", "value", "122662581541670"));
        testCase2.put("9", Map.of("base", "8", "value", "642121030037605"));
        testCases.add(testCase2);

        // Process all test cases
        processTestCases(testCases);
    }
}
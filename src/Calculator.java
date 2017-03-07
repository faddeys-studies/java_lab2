import java.lang.IllegalArgumentException;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.Math;

public class Calculator {

    private static Pattern expressionPattern = Pattern.compile(
            "\\s*" +
            "([\\da-fA-F]+(?:\\.[\\da-fA-F]+)?)?" +
            "\\s*" +
            "([+\\-*/\\^])" +
            "\\s*" +
            "([\\da-fA-F]+(?:\\.[\\da-fA-F]+)?)" +
            "\\s*"
    );
    private static Pattern setRadixPattern = Pattern.compile("\\s*@\\s*(\\d+)\\s*");

    private Float lastValue = null;
    private int radix;

    public static class InputError extends IllegalArgumentException {
        public InputError(String message) {
            super(message);
        }
    }

    public Calculator(int radix) {
        setRadix(radix);
    }
    public Calculator() {
        this(10);
    }

    public float evaluate(String expression) throws InputError {

        Matcher setRadixMatcher = setRadixPattern.matcher(expression);
        if (setRadixMatcher.matches()) {
            setRadix(Integer.parseInt(setRadixMatcher.group(1)));
            return lastValue == null ? 0.0f : lastValue;
        }

        Matcher exprMatcher = expressionPattern.matcher(expression);
        if (!exprMatcher.matches()) {
            throw new InputError("invalid input");
        }
        String firstOperandStr = exprMatcher.group(1);
        String operatorStr = exprMatcher.group(2);
        String secondOperandStr = exprMatcher.group(3);

        float firstValue = 0.0f;
        float secondValue = parseValue(secondOperandStr);
        if (firstOperandStr == null) {
            if (lastValue != null) {
                firstValue = lastValue;
            } else if (!operatorStr.equals("+") && !operatorStr.equals("-")) {
                throw new InputError("first operand is required");
            }
        } else {
            firstValue = parseValue(firstOperandStr);
        }

        float result = 0.0f;
        switch (operatorStr) {
            case "+": result = firstValue+secondValue; break;
            case "-": result = firstValue-secondValue; break;
            case "*": result = firstValue*secondValue; break;
            case "/": result = firstValue/secondValue; break;
            case "^": result = (float)Math.pow(firstValue, secondValue); break;
        }
        lastValue = result;

        return result;
    }

    private void setRadix(int newRadix) {
        if (newRadix < 2 || 16 < newRadix) {
            throw new InputError("Radix must be in 2..16");
        }
        radix = newRadix;
    }

    public String formatValue(float value) {
        String sign = value > 0 ? "" : "-";
        if (value < 0) value = -value;
        long integerPart = (long)value;
        double fractPartFloat = (value - integerPart) * Math.pow(radix, 6);
        long fractionalPart = (long)fractPartFloat;
        if (fractPartFloat > fractionalPart+0.5) {
            fractionalPart += 1;
        }
        while (fractionalPart > radix && fractionalPart%radix == 0) {
            fractionalPart /= radix;
        }
        return sign + Long.toString(integerPart, radix) + "." + Long.toString(fractionalPart, radix);
    }

    private float parseValue(String str) {
        int dotPos = str.indexOf('.');
        float result;
        try {
            if (dotPos == -1) {
                result = Integer.parseInt(str, radix);
            } else {
                String beforeDot = str.substring(0, dotPos);
                String afterDot = str.substring(dotPos + 1);
                result = Integer.parseInt(beforeDot, radix);
                result += Integer.parseInt(afterDot, radix) / Math.pow(radix, afterDot.length());
            }
        } catch (NumberFormatException exc) {
            throw new InputError("wrong number for radix " + Integer.toString(radix));
        }
        return result;
    }

    public static void main(String[] argv) {

        Calculator calc;
        if (argv.length > 0) {
            try {
                calc = new Calculator(Integer.parseUnsignedInt(argv[0]));
            } catch (IllegalArgumentException exc) {
                // also catches NumberFormatException and Calculator.InputError
                System.out.println("Invalid radix value");
                return;
            }
        } else {
            calc = new Calculator();
        }

        Scanner scanner = new Scanner(System.in);
        Pattern exitPattern = Pattern.compile(
                "\\s*(quit|exit|bye)\\s*"
        );
        while (true) {
            String line = scanner.nextLine();
            if (exitPattern.matcher(line).matches()) {
                return;
            }

            try {
                float result = calc.evaluate(line);
                System.out.println("= " + calc.formatValue(result));
            } catch (IllegalArgumentException exc) {
                System.out.println(">> " + exc.getMessage());
            }
        }
    }

}

package util;

import enums.SpecialValue;

import java.util.Objects;
import java.util.regex.Pattern;

public class IEEE754Converter {

    private static final Pattern one = Pattern.compile("[1]+");
    private static final Pattern zero = Pattern.compile("[0]+");
    private static final String DELIMITER = " ";

    private IEEE754Converter() {
    }

    public static String encode(double value, int characteristicsLength, int mantissaLength) {
        String presentation = tryToEncodeSpecial(String.valueOf(value), characteristicsLength, mantissaLength);
        if (presentation != null) {
            return presentation;
        } else {
            presentation = "";
        }
        if (Double.compare(value, 0.0) == 0) {
            return getForZero(characteristicsLength, mantissaLength, 0);
        } else if (Double.compare(value, -0.0) == 0) {
            return getForZero(characteristicsLength, mantissaLength, 1);
        }
        presentation += (Double.compare(value, 0) < 0) ? "1" : "0";
        presentation += DELIMITER;
        value = Math.abs(value);
        int whole = (int) value;
        double fractional = value - whole;
        String binary = Integer.toBinaryString(whole) + "." + fractionalToBinary(fractional, 32);
        StringBuilder mantissaBuilder = new StringBuilder(binary);
        int firstOneIndex = binary.indexOf('1');
        int pointIndex = binary.indexOf('.');
        int k = (pointIndex < firstOneIndex) ? 1 - firstOneIndex : pointIndex - firstOneIndex - 1;
        mantissaBuilder.deleteCharAt(pointIndex);
        if(pointIndex > firstOneIndex) {
            mantissaBuilder.insert(firstOneIndex + 1, '.');
        }else{
            mantissaBuilder.insert(firstOneIndex, '.');
        }
        if(firstOneIndex != 0) {
            mantissaBuilder.delete(0, firstOneIndex - 1);
        }
        if(mantissaBuilder.charAt(mantissaBuilder.length() - 1) == '.'){
            mantissaBuilder.append("0");
        }
        int BIAS = (int) Math.pow(2, characteristicsLength - 1) - 1;
        String characteristics = Integer.toBinaryString(BIAS + k);
        characteristics = fitCharacteristics(characteristics, characteristicsLength);
        presentation += characteristics;
        presentation += DELIMITER;
        String mantissa = mantissaBuilder.toString();
        presentation += mantissa.charAt(0);
        presentation += DELIMITER;
        if(! one.matcher(characteristics).matches()) {
            presentation += fitMantissa(mantissa.substring(2), mantissaLength);
        }else{
            StringBuilder alternative = new StringBuilder(presentation);
            fillMantissa(alternative, '0', mantissaLength);
            presentation = alternative.toString();
        }
        return presentation;
    }

    public static String tryToEncodeSpecial(String value, int characteristicsLength, int mantissaLength) {
        if (Objects.equals(value, SpecialValue.NAN.getRepresentation())) {
            StringBuilder out = new StringBuilder();
            out.append("0").append(DELIMITER);
            fillCharacteristic(out, '1', characteristicsLength);
            out.append(DELIMITER);
            out.append("1").append(DELIMITER);
            for (int i = 0; i < mantissaLength; i++) {
                int rem = i % 3;
                String nextMantissaBit = (rem == 1) ? "1" : "0";
                out.append(nextMantissaBit);
            }
            return out.toString();
        } else if (Objects.equals(value, SpecialValue.MINUS_INF.getRepresentation())
                || Objects.equals(value, SpecialValue.PLUS_INF.getRepresentation())) {
            StringBuilder out = new StringBuilder();
            String sign = (Objects.equals(value, SpecialValue.MINUS_INF.getRepresentation())) ? "1" : "0";
            out.append(sign);
            out.append(DELIMITER);
            fillCharacteristic(out, '1', characteristicsLength);
            out.append(DELIMITER);
            out.append("1").append(DELIMITER);
            fillMantissa(out, '0', mantissaLength);
            return out.toString();
        }
        return null;
    }

    public static double decode(String ieee754String, int characteristicsLength) {
        String[] parts = ieee754String.split(DELIMITER);
        Double special = tryToDecodeSpecial(ieee754String);
        if (special != null) {
            return special;
        }
        int signNumber = (parts[0].equals("0")) ? 1 : -1;
        int BIAS = (int) Math.pow(2, characteristicsLength - 1) - 1;
        double offset = Math.pow(2, Integer.parseInt(parts[1], 2) - BIAS);
        double value = of(parts[2] + "." + parts[3]);
        return signNumber * value * offset;
    }

    private static Double tryToDecodeSpecial(String ieee754String) {
        String[] parts = ieee754String.split(DELIMITER);
        if (zero.matcher(parts[1]).matches() && !zero.matcher(parts[3]).matches()) {
            return Double.NaN;
        } else if (zero.matcher(parts[3]).matches() && one.matcher(parts[1]).matches()) {
            return (parts[0].equals("0")) ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        return null;
    }

    private static double of(String binary) {
        String[] parts = binary.split("\\.");
        double out = 0;
        for (int i = 0; i < parts[0].length(); i++) {
            if (parts[0].charAt(i) == '1') {
                out += Math.pow(2, parts[0].length() - i - 1);
            }
        }
        for (int i = 0; i < parts[1].length(); i++) {
            if (parts[1].charAt(i) == '1') {
                out += Math.pow(2, -1 * (i + 1));
            }
        }
        return out;
    }

    private static String fitMantissa(String mantissa, int mantissaLength) {
        if (mantissa.length() == mantissaLength) {
            return mantissa;
        }
        if (mantissa.length() < mantissaLength) {
            StringBuilder sb = new StringBuilder(mantissa);
            do {
                sb.append("0");
            } while (sb.length() < mantissaLength);
            return sb.toString();
        } else {
            return mantissa.substring(0, mantissaLength);
        }
    }

    private static String fitCharacteristics(String characteristics, int characteristicLength) {
        if (characteristics.length() == characteristicLength) {
            return characteristics;
        }
        if (characteristics.length() < characteristicLength) {
            StringBuilder sb = new StringBuilder(characteristics);
            do {
                sb.insert(0, "0");
            } while (sb.length() < characteristicLength);
            return sb.toString();
        } else {
            String inf = tryToEncodeSpecial(SpecialValue.PLUS_INF.getRepresentation(), characteristicLength, 0);
            return inf.split(DELIMITER)[1];
        }
    }

    private static String toBinary(double d) {
        long lValue = (long) d;
        return Long.toBinaryString(lValue) + "." + fractionalToBinary(d - lValue, 64);
    }

    private static String fractionalToBinary(double num, int precision) {
        StringBuilder binary = new StringBuilder();
        while (num > 0 && binary.length() < precision) {
            double r = num * 2;
            if (r >= 1) {
                binary.append(1);
                num = r - 1;
            } else {
                binary.append(0);
                num = r;
            }
        }
        return binary.toString();
    }

    private static String getForZero(int characteristicsLength, int mantissaLength, int sign) {
        StringBuilder out = new StringBuilder();
        out.append(sign).append(DELIMITER);
        fillCharacteristic(out, '0', characteristicsLength);
        out.append(DELIMITER);
        out.append("0").append(DELIMITER);
        fillMantissa(out, '0', mantissaLength);
        return out.toString();
    }

    private static void fillCharacteristic(StringBuilder destination, char content, int characteristicLength) {
        for (int i = 0; i < characteristicLength; i++) {
            destination.append(content);
        }
    }

    private static void fillMantissa(StringBuilder destination, char content, int mantissaLength) {
        for (int i = 0; i < mantissaLength; i++) {
            destination.append(content);
        }
    }
}

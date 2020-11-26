package entity;

import util.IEEE754Converter;

import java.util.Objects;
import java.util.regex.Pattern;

public class Register {

    private final String name;

    private final int characteristicLength;
    private final int mantissaLength;

    private String valuePresentation;

    private static final Pattern ieeeString = Pattern.compile("[01] [01]+ [01] [01]+");

    public Register(String name, int characteristicLength, int mantissaLength) {
        this.name = name;
        this.characteristicLength = characteristicLength;
        this.mantissaLength = mantissaLength;
        this.valuePresentation = IEEE754Converter.encode(0.0, characteristicLength, mantissaLength);
    }

    public void setValue(String value) {
        String presentation = IEEE754Converter
                .tryToEncodeSpecial(value, characteristicLength, mantissaLength);
        if(ieeeString.matcher(value).matches() && value.length() == 5 + characteristicLength + mantissaLength){
            this.valuePresentation = value;
            return;
        }
        this.valuePresentation = (presentation == null)
                ? IEEE754Converter.encode(Double.parseDouble(value), characteristicLength, mantissaLength)
                : presentation;
    }

    public String getName() {
        return name;
    }

    public int getCharacteristicLength() {
        return characteristicLength;
    }

    public int getMantissaLength() {
        return mantissaLength;
    }

    public String getValuePresentation() {
        return valuePresentation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Register register = (Register) o;
        return characteristicLength == register.characteristicLength &&
                mantissaLength == register.mantissaLength &&
                Objects.equals(name, register.name) &&
                Objects.equals(valuePresentation, register.valuePresentation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, characteristicLength, mantissaLength, valuePresentation);
    }

    @Override
    public String toString() {


        return "Register{" +
                "name='" + name + '\'' +
                ", valuePresentation='" + valuePresentation + '\'' +
                ", value = " + IEEE754Converter.decode(valuePresentation, characteristicLength) +"}";
    }
}

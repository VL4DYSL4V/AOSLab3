package enums;

public enum SpecialValue {
    NAN("NaN"), PLUS_INF("+inf"), MINUS_INF("-inf");

    private final String representation;

    SpecialValue(String representation) {
        this.representation = representation;
    }

    public String getRepresentation() {
        return representation;
    }
}

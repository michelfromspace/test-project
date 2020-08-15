package data.triangle.model;

import java.io.Serializable;

public class CreateTriangle {

    private String separator;
    private String input;

    public CreateTriangle() {
    }

    public CreateTriangle(String separator, String input) {
        this.separator = separator;
        this.input = input;
    }

    public String getSeparator() {
        return separator;
    }

    public String getInput() {
        return input;
    }

    @Override
    public String toString() {
        return "CreateTriangle{" +
                "separator='" + separator + '\'' +
                ", input='" + input + '\'' +
                '}';
    }
}

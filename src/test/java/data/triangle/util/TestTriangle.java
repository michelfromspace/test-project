package data.triangle.util;


import java.util.Locale;

public class TestTriangle {

    private double sideA;
    private double sideB;
    private double sideC;


    public TestTriangle(double sideA, double sideB, double sideC) {
        this.sideA = sideA;
        this.sideB = sideB;
        this.sideC = sideC;
    }

    public double getSideA() {
        return sideA;
    }

    public double getSideB() {
        return sideB;
    }

    public double getSideC() {
        return sideC;
    }

    public boolean isValid() {
        return (sideA + sideB <= sideC || sideA + sideC <= sideB || sideB + sideC <= sideA);
    }

    public double getArea() {
        double p = (sideA + sideB + sideC) / 2;
        return Math.sqrt(p * (p - sideA) * (p - sideB) * (p - sideC));
    }

    public double getPerimeter() {
        return sideA + sideB + sideC;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%.10f;%.10f;%.10f", sideA, sideB, sideC);
    }

    public String toString(String separator) {
        return String.format(Locale.US, "%.10f%s%.10f%s%.10f", sideA, separator, sideB, separator, sideC);
    }



}

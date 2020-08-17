package data.triangle.util;


import java.util.Locale;

public class TestTriangle {

    private double sideA;
    private double sideB;
    private double sideC;

    /**
     * TestTriangle can help create triangle
     * and convert data to input format for few ways
     * also it helps calculate perimeter and area
     * @param sideA first triangle side
     * @param sideB second triangle side
     * @param sideC third triangle side
     */
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

    /**
     * method checks that triangle is valid
     * @return
     */
    public boolean isValid() {
        return (sideA + sideB > sideC || sideA + sideC > sideB || sideB + sideC > sideA);
    }

    /**
     * method returns triangle area
     * @return area
     */
    public double getArea() {
        double p = (sideA + sideB + sideC) / 2;
        return Math.sqrt(p * (p - sideA) * (p - sideB) * (p - sideC));
    }

    /**
     * method returns triangle perimeter
     * @return perimeter
     */
    public double getPerimeter() {
        return sideA + sideB + sideC;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%.10f;%.10f;%.10f", sideA, sideB, sideC);
    }

    /**
     * represent triangle side values as string with separation
     * @param separator symbol for sides separation
     * @return String, example: 2.0000000000,3.0000330000,2.2567000000
     */
    public String toString(String separator) {
        return String.format(Locale.US, "%.10f%s%.10f%s%.10f", sideA, separator, sideB, separator, sideC);
    }

    /**
     * represent triangle side values as integer as string with separation
     * @param separator symbol for sides separation
     * @return String, example: 2;3;2
     */
    public String toStringAsInt(String separator) {
        return String.join(separator, String.valueOf((int)sideA), String.valueOf((int)sideB), String.valueOf((int)sideC));
    }



}

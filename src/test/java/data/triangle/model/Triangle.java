package data.triangle.model;

public class Triangle {
    private String id;
    private double firstSide;
    private double secondSide;
    private double thirdSide;

    public String getId() {
        return id;
    }

    public double getFirstSide() {
        return firstSide;
    }

    public double getSecondSide() {
        return secondSide;
    }

    public double getThirdSide() {
        return thirdSide;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstSide(double firstSide) {
        this.firstSide = firstSide;
    }

    public void setSecondSide(double secondSide) {
        this.secondSide = secondSide;
    }

    public void setThirdSide(double thirdSide) {
        this.thirdSide = thirdSide;
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "id='" + id + '\'' +
                ", firstSide=" + firstSide +
                ", secondSide=" + secondSide +
                ", thirdSide=" + thirdSide +
                '}';
    }
}

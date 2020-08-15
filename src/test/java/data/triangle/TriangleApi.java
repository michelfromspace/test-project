package data.triangle;

public final class TriangleApi {

    private static final String POST_TRIANGLE = "/triangle";
    private static final String GET_TRIANGLE = "/triangle/{triangleId}";
    private static final String DELETE_TRIANGLE = "/triangle/{triangleId}";
    private static final String GET_TRIANGLES = "/triangle/all";
    private static final String GET_TRIANGLE_PERIMETER = "/triangle/{triangleId}/perimeter";
    private static final String GET_TRIANGLE_AREA = "/triangle/{triangleId}/area";

    public static String getPostTriangle() {
        return POST_TRIANGLE;
    }

    public static String getGetTriangle() {
        return GET_TRIANGLE;
    }

    public static String getDeleteTriangle() {
        return DELETE_TRIANGLE;
    }

    public static String getGetTriangles() {
        return GET_TRIANGLES;
    }

    public static String getGetTrianglePerimeter() {
        return GET_TRIANGLE_PERIMETER;
    }

    public static String getGetTriangleArea() {
        return GET_TRIANGLE_AREA;
    }
}

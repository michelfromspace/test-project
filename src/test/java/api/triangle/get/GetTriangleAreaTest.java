package api.triangle.get;

import common.steps.TriangleApiSteps;
import data.triangle.TriangleApi;
import data.triangle.TriangleApiSpec;
import data.triangle.model.CreateTriangle;
import data.triangle.model.Result;
import data.triangle.model.Triangle;
import data.triangle.util.TestTriangle;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class GetTriangleAreaTest {

    private RequestSpecification triangleRequest = RestAssured.given().spec(TriangleApiSpec.getRequestSpecification());

    @ParameterizedTest(name = "{displayName} со сторонами {arguments}")
    @CsvSource({"6,25,29", "2,3,2.01", "3.1,2.00233,2", "11.11,11.11,11.11"})
    @DisplayName("Получаем площадь треугольника")
    @Description("Выполняем запрос GET /triangle/{id}/area и проверяем корректность результата")
    public void get_triangleArea(double sideA, double sideB, double sideC) {
        TestTriangle testTriangle = new TestTriangle(sideA,sideB,sideC);
        CreateTriangle createTriangle = new CreateTriangle("x", testTriangle.toString("x"));

        Response response = triangleRequest
                .when()
                .body(createTriangle)
                .post(TriangleApi.getPostTriangle());

        response.then().specification(TriangleApiSpec.getOkResponseSpecification());

        Triangle createTriangleBody = response.then().extract().body().as(Triangle.class);

        Response responseArea = triangleRequest
                .when()
                .pathParam("triangleId", createTriangleBody.getId())
                .get(TriangleApi.getGetTriangleArea());
        responseArea.then().specification(TriangleApiSpec.getOkResponseSpecification());

        Result result = responseArea.then().extract().body().as(Result.class);

        TriangleApiSteps.checkTrianglePerimeter(result, testTriangle.getArea());
    }
}

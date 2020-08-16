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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class GetTrianglePerimeter {

    private RequestSpecification triangleRequest = RestAssured.given().spec(TriangleApiSpec.getRequestSpecification());

    @AfterAll
    public static void afterClass() {
        TriangleApiSteps.deleteTriangles();
    }

    @ParameterizedTest(name = "{displayName} со сторонами {arguments}")
    @CsvSource({"2,2,3", "2,3,2", "3,2,2", "11.11,11.11,11.11"})
    @DisplayName("Получаем периметр треугольника")
    @Description("Выполняем запрос GET /triangle/{id}/perimeter и проверяем корректность результата")
    public void get_trianglePerimeter(double sideA, double sideB, double sideC) {
        TestTriangle testTriangle = new TestTriangle(sideA,sideB,sideC);
        CreateTriangle createTriangle = new CreateTriangle("x", testTriangle.toString("x"));

        Response response = triangleRequest
                .when()
                .body(createTriangle)
                .post(TriangleApi.getPostTriangle());

        response.then().specification(TriangleApiSpec.getOkResponseSpecification());

        Triangle createTriangleBody = response.then().extract().body().as(Triangle.class);

        Response responsePerimeter = triangleRequest
                .when()
                .pathParam("triangleId", createTriangleBody.getId())
                .get(TriangleApi.getGetTrianglePerimeter());
        responsePerimeter.then().specification(TriangleApiSpec.getOkResponseSpecification());

        Result result = responsePerimeter.then().extract().body().as(Result.class);

        TriangleApiSteps.checkTrianglePerimeter(result, testTriangle.getPerimeter());
    }
}

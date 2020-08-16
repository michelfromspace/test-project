package api.triangle.get;

import common.steps.TriangleApiSteps;
import data.triangle.TriangleApi;
import data.triangle.TriangleApiSpec;
import data.triangle.model.CreateTriangle;
import data.triangle.model.Error;
import data.triangle.model.GetTriangle;
import data.triangle.model.Triangle;
import data.triangle.util.TestTriangle;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GetTriangleTest {

    private RequestSpecification triangleRequest = RestAssured.given().spec(TriangleApiSpec.getRequestSpecification());
    private RequestSpecification triangleGetRequest = RestAssured.given().spec(TriangleApiSpec.getRequestSpecification());
    private RequestSpecification triangleRequestNoAuth = RestAssured.given().spec(TriangleApiSpec.getRequestSpecificationNoAuth());

    @AfterAll
    public static void afterClass() {
        TriangleApiSteps.deleteTriangles();
    }

    @Test
    @DisplayName("Получаем треугольник по его ID")
    @Description("Выполняем запрос GET /triangle{id}")
    public void get_getTriangle() {
        TestTriangle testTriangle = new TestTriangle(10,10,10);
        CreateTriangle createTriangle = new CreateTriangle(",", testTriangle.toString(","));

        Response createResponse = triangleRequest
                .body(createTriangle)
                .when()
                .post(TriangleApi.getPostTriangle());
        Triangle createTriangleResponse = createResponse.then().extract().body().as(Triangle.class);

        Response getResponse = triangleGetRequest
                .when()
                .pathParam("triangleId", createTriangleResponse.getId())
                .get(TriangleApi.getGetTriangle());

        getResponse.then().specification(TriangleApiSpec.getOkResponseSpecification());

        GetTriangle getTriangle = getResponse.then().extract().body().as(GetTriangle.class);
        TriangleApiSteps.checkGetTriangleRequest(getTriangle, testTriangle, createTriangleResponse.getId());
    }

    @Test
    @DisplayName("Пытаемся поулчить треугольник по несуществующему id")
    @Description("Выполняем запрос GET /triangle{id} c несуществующим id")
    public void get_getTriangle_notFound() {
        Response getResponse = triangleGetRequest
                .when()
                .pathParam("triangleId", "someId")
                .get(TriangleApi.getGetTriangle());

        getResponse.then().specification(TriangleApiSpec.getNotFoundResponseSpecification());

        Error getTriangle = getResponse.then().extract().body().as(Error.class);
        TriangleApiSteps.checkTriangleApiNotFoundError(getTriangle, "/triangle/someId");
    }

    @Test
    @DisplayName("Получаем существующий треугольник по ID без авторизации")
    @Description("Выполняем запрос GET /triangle{id}, не передан корректный заголовок X-User")
    public void post_createTriangle_incorrectToken() {
        TestTriangle testTriangle = new TestTriangle(1000,1000,1000);
        CreateTriangle createTriangle = new CreateTriangle("-", testTriangle.toString("-"));

        Response createResponse = triangleRequest
                .body(createTriangle)
                .when()
                .post(TriangleApi.getPostTriangle());
        Triangle createTriangleResponse = createResponse.then().extract().body().as(Triangle.class);

        Response getResponse = triangleRequestNoAuth
                .when()
                .header("X-User", "")
                .pathParam("triangleId", createTriangleResponse.getId())
                .get(TriangleApi.getGetTriangle());

        getResponse.then().specification(TriangleApiSpec.getUnauthorizedResponseSpecification());

        Error createTriangleBody = getResponse.then().extract().body().as(Error.class);

        TriangleApiSteps.checkTriangleApiUnauthorizedError(createTriangleBody, String.format("/triangle/%s", createTriangleResponse.getId()));
    }
}

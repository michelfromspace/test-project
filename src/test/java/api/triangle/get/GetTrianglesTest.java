package api.triangle.get;

import common.steps.TriangleApiSteps;
import data.triangle.TriangleApi;
import data.triangle.TriangleApiSpec;
import data.triangle.model.CreateTriangle;
import data.triangle.model.Error;
import data.triangle.model.GetTriangle;
import data.triangle.model.Triangle;
import data.triangle.util.TestTriangle;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

public class GetTrianglesTest {

    private RequestSpecification triangleRequest = RestAssured.given().spec(TriangleApiSpec.getRequestSpecification());
    private RequestSpecification triangleGetRequest = RestAssured.given().spec(TriangleApiSpec.getRequestSpecification());
    private RequestSpecification triangleRequestNoAuth = RestAssured.given().spec(TriangleApiSpec.getRequestSpecificationNoAuth());

    @BeforeEach
    public void beforeEach() {
        TriangleApiSteps.deleteTriangles();
    }

    @AfterAll
    public static void afterClass() {
        TriangleApiSteps.deleteTriangles();
    }

    @Test
    @DisplayName("Делаем запрос на получение всех треугольников, проверка пустого массива в ответе")
    @Description("Выполняем запрос GET /triangle/all")
    public void get_getTriangles_empty() {

        Response getResponse = triangleGetRequest
                .when()
                .get(TriangleApi.getGetTriangles());
        getResponse.then().specification(TriangleApiSpec.getOkResponseSpecification());

        GetTriangle[] getTriangles = getResponse.then().extract().body().as(GetTriangle[].class);
        Allure.step("Проверяем, что запрос вернул пустой массив, так как еще ни одного треугольника не было создано", () -> {
            Assertions.assertEquals(0, getTriangles.length);
        });
    }

    @Test
    @DisplayName("Получаем все созданные треугольники")
    @Description("Выполняем запрос GET /triangle/all")
    public void get_getTriangles() {
        TestTriangle firstTestTriangle = new TestTriangle(10,10,10);
        TestTriangle secondTestTriangle = new TestTriangle(13,20,21);
        CreateTriangle createFirstTriangle = new CreateTriangle("=", firstTestTriangle.toString("="));
        CreateTriangle createSecondTriangle = new CreateTriangle(";", secondTestTriangle.toString(";"));

        Response createFirstResponse = triangleRequest
                .when()
                .body(createFirstTriangle)
                .post(TriangleApi.getPostTriangle());
        Triangle createFirstTriangleResponse = createFirstResponse.then().extract().body().as(Triangle.class);

        Response createSecondResponse = triangleRequest
                .when()
                .body(createSecondTriangle)
                .post(TriangleApi.getPostTriangle());
        Triangle createSecondTriangleResponse = createSecondResponse.then().extract().body().as(Triangle.class);

        Response getResponse = triangleGetRequest
                .when()
                .get(TriangleApi.getGetTriangles());
        getResponse.then().specification(TriangleApiSpec.getOkResponseSpecification());

        GetTriangle[] getTriangles = getResponse.then().extract().body().as(GetTriangle[].class);
        TriangleApiSteps.checkGetTriangleRequest(getTriangles[0], secondTestTriangle, createSecondTriangleResponse.getId());
        TriangleApiSteps.checkGetTriangleRequest(getTriangles[1], firstTestTriangle, createFirstTriangleResponse.getId());
    }

    @Test
    @DisplayName("Делаем запрос на получение всех треугольников, без авторизации")
    @Description("Выполняем запрос GET /triangle/all без заголовка X-User")
    public void get_getTriangles_noAuth() {

        Response getResponse = triangleRequestNoAuth
                .when()
                .get(TriangleApi.getGetTriangles());

        getResponse.then().specification(TriangleApiSpec.getUnauthorizedResponseSpecification());

        Error createTriangleBody = getResponse.then().extract().body().as(Error.class);

        TriangleApiSteps.checkTriangleApiUnauthorizedError(createTriangleBody, TriangleApi.getGetTriangles());
    }
}

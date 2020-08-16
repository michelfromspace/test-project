package api.triangle.create;

import common.steps.TriangleApiSteps;
import data.triangle.TriangleApi;
import data.triangle.TriangleApiSpec;
import data.triangle.model.CreateTriangle;
import data.triangle.model.Triangle;
import data.triangle.model.Error;
import data.triangle.util.TestTriangle;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CreateTriangleTest {

    private RequestSpecification triangleRequest = RestAssured.given().spec(TriangleApiSpec.getRequestSpecification());
    private RequestSpecification triangleRequestNoAuth = RestAssured.given().spec(TriangleApiSpec.getRequestSpecificationNoAuth());


    @AfterAll
    public static void afterClass() {
        TriangleApiSteps.deleteTriangles();
    }

    @ParameterizedTest(name = "{displayName} со сторонами {arguments}")
    @CsvSource({"2.2244,2.2244,3.0", "2,3.0001,2", "3,2,2"})
    @DisplayName("Создаем валидный треугольник")
    @Description("Выполняем запрос POST /triangle и проверяем корректность созданного объекта")
    public void post_createTriangle_valid(double sideA, double sideB, double sideC) {
        TestTriangle testTriangle = new TestTriangle(sideA,sideB,sideC);
        CreateTriangle createTriangle = new CreateTriangle("!", testTriangle.toString("!"));

        Response response = triangleRequest
                .body(createTriangle)
                .when()
                .post(TriangleApi.getPostTriangle());

        response.then().specification(TriangleApiSpec.getOkResponseSpecification());

        Triangle createTriangleBody = response.then().extract().body().as(Triangle.class);

        TriangleApiSteps.checkCreateTriangleRequest(createTriangleBody, testTriangle);
    }

    @Test
    @DisplayName("Проверяем, что можно сохранить только 10 треугольников")
    @Description("Выполняем запрос POST /triangle 10 раз и провреяем, что в 11-й раз система не даст сохранить треугольник")
    public void post_createTriangle_checkLimit() {
        TestTriangle testTriangle = new TestTriangle(3,4, 5);
        CreateTriangle createTriangle = new CreateTriangle("!", testTriangle.toString("!"));

        TriangleApiSteps.deleteTriangles();

        for (int i=0; i<10; i++) {
            triangleRequest
                    .when()
                    .body(createTriangle)
                    .post(TriangleApi.getPostTriangle());
        }
        Response response = triangleRequest
                .body(createTriangle)
                .when()
                .post(TriangleApi.getPostTriangle());

        response.then().specification(TriangleApiSpec.getErrorResponseSpecification());

        Error createTriangleBody = response.then().extract().body().as(Error.class);

        TriangleApiSteps.checkTriangleApiLimitExceededError(createTriangleBody);
    }

    @ParameterizedTest(name = "{displayName} со сторонами {arguments}")
    @CsvSource({"1,1,2", "1,2,1", "2,1,1"})
    @DisplayName("Создаем невалидный треугольник")
    @Description("Выполняем запрос POST /triangle и проверяем, что создание треугольника не удалось, так как не выполняется условие валидности треугольника")
    public void post_createTriangle_notValid(double sideA, double sideB, double sideC) {
        TestTriangle testTriangle = new TestTriangle(sideA,sideB,sideC);
        CreateTriangle createTriangle = new CreateTriangle(";", testTriangle.toString(";"));

        Response response = triangleRequest
                .body(createTriangle)
                .when()
                .post(TriangleApi.getPostTriangle());

        response.then().specification(TriangleApiSpec.getErrorResponseSpecification());

        Error createTriangleBody = response.then().extract().body().as(Error.class);

        TriangleApiSteps.checkTriangleApiUnprocessableError(createTriangleBody, TriangleApi.getPostTriangle());
    }

    @Test
    @DisplayName("Создаем треугольник со сторонами 0,0,0")
    @Description("Выполняем запрос POST /triangle и проверяем, что объект не будет создан, так как треугольник со сторанами 0,0,0 не может существовать")
    public void post_createTriangle_zeroSides() {
        Allure.step("Подготавливаем треугольник со сторонами 0, 0, 0");
        TestTriangle testTriangle = new TestTriangle(0,0,0);
        CreateTriangle createTriangle = new CreateTriangle(";", testTriangle.toString(";"));

        Response response = triangleRequest
                .body(createTriangle)
                .when()
                .post(TriangleApi.getPostTriangle());

        response.then().specification(TriangleApiSpec.getErrorResponseSpecification());

        Error createTriangleBody = response.then().extract().body().as(Error.class);

        TriangleApiSteps.checkTriangleApiUnprocessableError(createTriangleBody, TriangleApi.getPostTriangle());
    }

    @Test
    @DisplayName("Пытаемся вызвать запрос на создание треугольника с некорректным токеном в X-User")
    @Description("Проверяем, что не получится выполнить операцию создания, если не передан корректный заголовок X-User")
    public void post_createTriangle_incorrectToken() {
        CreateTriangle createTriangle = new CreateTriangle(";", "1,1,1");
        Response response = triangleRequestNoAuth
                .header("X-User", "incorrect_value")
                .body(createTriangle)
                .when()
                .post(TriangleApi.getPostTriangle());

        response.then().specification(TriangleApiSpec.getUnauthorizedResponseSpecification());

        Error createTriangleBody = response.then().extract().body().as(Error.class);

        TriangleApiSteps.checkTriangleApiUnauthorizedError(createTriangleBody, TriangleApi.getPostTriangle());
    }
}

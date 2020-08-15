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

public class CreateTestTriangleApiTest {

    RequestSpecification REQUEST = RestAssured.given().spec(TriangleApiSpec.getRequestSpecification());
    RequestSpecification REQUEST_NO_AUTH = RestAssured.given().spec(TriangleApiSpec.getRequestSpecificationNoAuth());


    @AfterAll
    public static void afterClass() {
        TriangleApiSteps.deleteTriangles();
    }

    @ParameterizedTest(name = "{displayName} со сторонами {arguments}")
    @CsvSource({"2,2,3", "2,3,2", "3,2,2"})
    @DisplayName("Создаем валидный треугольник")
    @Description("Выполняем запрос POST /triangle и проверяем корректность созданного объекта")
    public void post_createTriangle_valid(double sideA, double sideB, double sideC) {
        TestTriangle testTriangle = new TestTriangle(sideA,sideB,sideC);
        CreateTriangle createTriangle = new CreateTriangle(";", testTriangle.toString(";"));

        Response response = REQUEST
                .body(createTriangle)
                .when()
                .post(TriangleApi.getPostTriangle());

        response.then().specification(TriangleApiSpec.getOkResponseSpecification());

        Triangle createTriangleBody = response.then().extract().body().as(Triangle.class);

        TriangleApiSteps.checkCreateTriangleRequest(createTriangleBody, testTriangle);
    }

    @ParameterizedTest(name = "{displayName} со сторонами {arguments}")
    @CsvSource({"1,1,2", "1,2,1", "2,1,1"})
    @DisplayName("Создаем невалидный треугольник")
    @Description("Выполняем запрос POST /triangle и проверяем, что создание треугольника не удалось, так как не выполняется условие валидности треугольника")
    public void post_createTriangle_notValid(double sideA, double sideB, double sideC) {
        TestTriangle testTriangle = new TestTriangle(sideA,sideB,sideC);
        CreateTriangle createTriangle = new CreateTriangle(";", testTriangle.toString(";"));

        Response response = REQUEST
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

        Response response = REQUEST
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
        Response response = REQUEST_NO_AUTH
                .header("X-User", "incorrect_value")
                .body(createTriangle)
                .when()
                .post(TriangleApi.getPostTriangle());

        response.then().specification(TriangleApiSpec.getUnauthorizedResponseSpecification());

        Error createTriangleBody = response.then().extract().body().as(Error.class);

        TriangleApiSteps.checkTriangleApiUnauthorizedError(createTriangleBody, TriangleApi.getPostTriangle());
    }
}

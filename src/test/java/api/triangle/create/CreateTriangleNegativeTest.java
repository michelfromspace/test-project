package api.triangle.create;

import common.steps.TriangleApiSteps;
import data.triangle.TriangleApi;
import data.triangle.TriangleApiSpec;
import data.triangle.model.CreateTriangle;
import data.triangle.model.Error;
import data.triangle.model.Triangle;
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

public class CreateTriangleNegativeTest {

    private RequestSpecification triangleRequest = RestAssured.given().spec(TriangleApiSpec.getRequestSpecification());


    @AfterAll
    public static void afterClass() {
        TriangleApiSteps.deleteTriangles();
    }

    @ParameterizedTest(name = "{displayName} с разделителем {arguments}")
    @CsvSource({"+", "\\", "*"})
    @DisplayName("Создаем валидный треугольник")
    @Description("Выполняем запрос POST /triangle и проверяем корректность созданного объекта")
    public void post_createTriangle_separators(String separator) {
        TestTriangle testTriangle = new TestTriangle(4,13,15);
        CreateTriangle createTriangle = new CreateTriangle(separator, testTriangle.toString(";"));

        Response response = triangleRequest
                .body(createTriangle)
                .when()
                .post(TriangleApi.getPostTriangle());

        response.then().specification(TriangleApiSpec.getOkResponseSpecification());

        Triangle createTriangleBody = response.then().extract().body().as(Triangle.class);

        TriangleApiSteps.checkCreateTriangleRequest(createTriangleBody, testTriangle);
    }

    @ParameterizedTest(name = "{displayName} со сторонами {arguments}")
    @CsvSource({"-4,13,15", "3,-25,26", "7,15,-20", "-6,25,-29"})
    @DisplayName("Создаем не валидный треугольник с отрицательными длинами сторон")
    @Description("Выполняем запрос POST /triangle и проверяем, что треугольник не будет создан и вернется ошибка")
    public void post_createTriangle_negativeSides(double sideA, double sideB, double sideC) {
        TestTriangle testTriangle = new TestTriangle(sideA,sideB,sideC);
        CreateTriangle createTriangle = new CreateTriangle(";", testTriangle.toString(";"));

        Response response = triangleRequest
                .body(createTriangle)
                .when()
                .post(TriangleApi.getPostTriangle());

        response.then().specification(TriangleApiSpec.getErrorResponseSpecification());

        Error createTriangleBody = response.then().extract().body().as(Error.class);

        TriangleApiSteps.checkTriangleApiUnauthorizedError(createTriangleBody, TriangleApi.getPostTriangle());
        Allure.step("Треугольник не должен быть создан, т.к. одна из длин сторон отрицательная");
    }

    @ParameterizedTest(name = "{displayName} со сторонами {arguments}")
    @CsvSource({"a,1,2", "2,b,2", "2,2,c", "a,b,c", "-,1,2","2,.,2"})
    @DisplayName("Создаем не валидный треугольник с некорректными значениями для длин сторон")
    @Description("Выполняем запрос POST /triangle и проверяем, что треугольник не будет создан и вернется ошибка")
    public void post_createTriangle_negativeSideValues(String sideA, String sideB, String sideC) {
        CreateTriangle createTriangle = new CreateTriangle(";", String.join(";", sideA, sideB, sideC));

        Response response = triangleRequest
                .body(createTriangle)
                .when()
                .post(TriangleApi.getPostTriangle());

        response.then().specification(TriangleApiSpec.getErrorResponseSpecification());

        Error createTriangleBody = response.then().extract().body().as(Error.class);

        TriangleApiSteps.checkTriangleApiUnprocessableError(createTriangleBody, TriangleApi.getPostTriangle());
        Allure.step("Треугольник не должен быть создан, т.к. одна из длин сторон отрицательная");
    }

    @ParameterizedTest(name = "{displayName}, body = {arguments}")
    @CsvSource({",3:4:5", ";,"})
    @DisplayName("Выполняем запрос без обязательных полей в body")
    @Description("Выполняем запрос POST /triangle и проверяем, что треугольник не будет создан и вернется ошибка")
    public void post_createTriangle_nullData(String separator, String input) {
        CreateTriangle createTriangle = new CreateTriangle(separator, input);

        Response response = triangleRequest
                .body(createTriangle)
                .when()
                .post(TriangleApi.getPostTriangle());

        response.then().specification(TriangleApiSpec.getErrorResponseSpecification());

        Error createTriangleBody = response.then().extract().body().as(Error.class);
        TriangleApiSteps.checkTriangleApiUnprocessableError(createTriangleBody, TriangleApi.getPostTriangle());
        Allure.step("Треугольник не должен быть создан, т.к. одна из длин сторон отрицательная");
    }
}

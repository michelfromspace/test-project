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

public class CreateTestTriangleApiNegativeTest {

    RequestSpecification REQUEST = RestAssured.given().spec(TriangleApiSpec.getRequestSpecification());


    @AfterAll
    public static void afterClass() {
        TriangleApiSteps.deleteTriangles();
    }

    @ParameterizedTest(name = "{displayName} с разделителем {arguments}")
    @CsvSource({"+", "\\", "*"})
    @DisplayName("Создаем валидный треугольник")
    @Description("Выполняем запрос POST /triangle и проверяем корректность созданного объекта")
    public void post_createTriangle_valid(String separator) {
        TestTriangle testTriangle = new TestTriangle(4,13,15);
        CreateTriangle createTriangle = new CreateTriangle(separator, testTriangle.toString(";"));

        Response response = REQUEST
                .body(createTriangle)
                .when()
                .post(TriangleApi.getPostTriangle());

        response.then().specification(TriangleApiSpec.getOkResponseSpecification());

        Triangle createTriangleBody = response.then().extract().body().as(Triangle.class);

        TriangleApiSteps.checkCreateTriangleRequest(createTriangleBody, testTriangle);
    }

    @ParameterizedTest(name = "{displayName} со сторонами {arguments}")
    @CsvSource({"-4,13,15", "3,-25,26", "7,15,-20", "-6,25,-29"})
    @DisplayName("Создаем валидный треугольник")
    @Description("Выполняем запрос POST /triangle и проверяем корректность созданного объекта")
    public void post_createTriangle_valid(double sideA, double sideB, double sideC) {
        TestTriangle testTriangle = new TestTriangle(sideA,sideB,sideC);
        CreateTriangle createTriangle = new CreateTriangle(";", testTriangle.toString(";"));

        Response response = REQUEST
                .body(createTriangle)
                .when()
                .post(TriangleApi.getPostTriangle());

        response.then().specification(TriangleApiSpec.getErrorResponseSpecification());

        Triangle createTriangleBody = response.then().extract().body().as(Triangle.class);

        TriangleApiSteps.checkCreateTriangleRequest(createTriangleBody, testTriangle);
    }
}

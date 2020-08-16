package common.steps;

import data.triangle.TriangleApi;
import data.triangle.TriangleApiSpec;
import data.triangle.model.GetTriangle;
import data.triangle.model.Result;
import data.triangle.model.Triangle;
import data.triangle.model.Error;
import data.triangle.util.TestTriangle;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class TriangleApiSteps {

    @Step("Удаляем созданный треугольник c id={id}")
    public static void deleteTriangle(String id) {
        RestAssured.given()
                .spec(TriangleApiSpec.getRequestSpecification())
                .pathParam("triangleId", id)
                .delete(TriangleApi.getDeleteTriangle());
    }

    @Step("Удаляем все созданные треугольники")
    public static void deleteTriangles() {
        GetTriangle[] response = RestAssured.given()
                .spec(TriangleApiSpec.getRequestSpecification())
                .get(TriangleApi.getGetTriangles())
                .then().extract().body().as(GetTriangle[].class);
        for (GetTriangle triangle : List.of(response)) {
            deleteTriangle(triangle.getId());
        }
    }

    @Step("Проверяем ответ из запроса на создание треугольника с исходными данными, стороны треугольника должны совпдать, id не должен быть пустым")
    public static void checkCreateTriangleRequest(Triangle createTriangleBody, TestTriangle testTriangle) {
        Assertions.assertNotNull(createTriangleBody.getId());
        Assertions.assertEquals(testTriangle.getSideA(), createTriangleBody.getFirstSide(),0);
        Assertions.assertEquals(testTriangle.getSideB(), createTriangleBody.getSecondSide(),0);
        Assertions.assertEquals(testTriangle.getSideC(), createTriangleBody.getThirdSide(),0);
    }

    @Step("Проверяем ответ из запроса на получение")
    public static void checkGetTriangleRequest(GetTriangle getTriangleBody, TestTriangle testTriangle, String id) {
        Assertions.assertEquals(getTriangleBody.getId(), id);
        Assertions.assertEquals(testTriangle.getSideA(), getTriangleBody.getFirstSide(), 0);
        Assertions.assertEquals(testTriangle.getSideB(),getTriangleBody.getSecondSide(), 0);
        Assertions.assertEquals(testTriangle.getSideC(),getTriangleBody.getThirdSide(), 0);
    }

    @Step("Проверяем ответ на запрос {path} со статусом 422")
    public static void checkTriangleApiUnprocessableError(Error error, String path) {
        Assertions.assertNotNull(error.getTimestamp());
        Assertions.assertEquals("Unprocessable Entity", error.getError());
        Assertions.assertEquals("Cannot process input", error.getMessage());
        Assertions.assertEquals(path, error.getPath());
        Assertions.assertEquals(422, error.getStatus());
    }

    @Step("Проверяем ответ на запрос {path} со статусом 404")
    public static void checkTriangleApiNotFoundError(Error error, String path) {
        Assertions.assertNotNull(error.getTimestamp());
        Assertions.assertEquals("Not Found", error.getError());
        Assertions.assertEquals("Not Found", error.getMessage());
        Assertions.assertEquals(path, error.getPath());
        Assertions.assertEquals(404, error.getStatus());
    }

    @Step("Проверяем ответ на запрос {path} со статусом 401")
    public static void checkTriangleApiUnauthorizedError(Error error, String path) {
        Assertions.assertNotNull(error.getTimestamp());
        Assertions.assertEquals("Unauthorized", error.getError());
        Assertions.assertEquals("No message available", error.getMessage());
        Assertions.assertEquals(path, error.getPath());
        Assertions.assertEquals(401, error.getStatus());
    }

    @Step("Проверяем ответ на запрос периметра треугольника, ожидаемый результат вычислений - {expectedResult}")
    public static void checkTrianglePerimeter(Result actualResult, double expectedResult) {
        Assertions.assertEquals(expectedResult, actualResult.getResult());
    }
}

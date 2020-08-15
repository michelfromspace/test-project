package common.steps;

import data.triangle.TriangleApi;
import data.triangle.TriangleApiSpec;
import data.triangle.model.GetTriangle;
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
        Assertions.assertEquals(createTriangleBody.getFirstSide(), testTriangle.getSideA(), 0);
        Assertions.assertEquals(createTriangleBody.getSecondSide(), testTriangle.getSideB(), 0);
        Assertions.assertEquals(createTriangleBody.getThirdSide(), testTriangle.getSideC(), 0);
    }

    @Step("Проверяем ответ на запрос {path} со статусом 422")
    public static void checkTriangleApiUnprocessableError(Error error, String path) {
        Assertions.assertNotNull(error.getTimestamp());
        Assertions.assertEquals(error.getError(), "Unprocessable Entity");
        Assertions.assertEquals(error.getMessage(), "Cannot process input");
        Assertions.assertEquals(error.getPath(), path);
        Assertions.assertEquals(error.getStatus(), 422);
    }

    @Step("Проверяем ответ на запрос {path} со статусом 404")
    public static void checkTriangleApiNotFoundError(Error error, String path) {
        Assertions.assertNotNull(error.getTimestamp());
        Assertions.assertEquals(error.getError(), "Not Found");
        Assertions.assertEquals(error.getMessage(), "Not Found");
        Assertions.assertEquals(error.getPath(), path);
        Assertions.assertEquals(error.getStatus(), 404);
    }

    @Step("Проверяем ответ на запрос {path} со статусом 401")
    public static void checkTriangleApiUnauthorizedError(Error error, String path) {
        Assertions.assertNotNull(error.getTimestamp());
        Assertions.assertEquals(error.getError(), "Unauthorized");
        Assertions.assertEquals(error.getMessage(), "No message available");
        Assertions.assertEquals(error.getPath(), path);
        Assertions.assertEquals(error.getStatus(), 401);
    }
}

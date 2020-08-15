package data.triangle;

import environment.Configuration;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import util.AllureRestAssuredFilter;

public final class TriangleApiSpec {
    private static final RequestSpecification REQUEST_SPECIFICATION = new RequestSpecBuilder()
            .setBaseUri(Configuration.getHostname())
            .addHeader("X-User", Configuration.getToken())
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .addFilter(new AllureRestAssuredFilter())
            .build();

    private static final RequestSpecification REQUEST_SPECIFICATION_NO_AUTH = new RequestSpecBuilder()
            .setBaseUri(Configuration.getHostname())
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .addFilter(new AllureRestAssuredFilter())
            .build();

    private static final ResponseSpecification OK_RESPONSE_SPECIFICATION = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .expectStatusCode(200)
            .expectContentType(ContentType.JSON)
            .build();

    private static final ResponseSpecification ERROR_RESPONSE_SPECIFICATION = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .expectStatusCode(422)
            .expectContentType(ContentType.JSON)
            .build();

    private static final ResponseSpecification NOT_FOUND_RESPONSE_SPECIFICATION = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .expectStatusCode(404)
            .expectContentType(ContentType.JSON)
            .build();

    private static final ResponseSpecification UNAUTHORIZED_RESPONSE_SPECIFICATION = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .expectStatusCode(401)
            .expectContentType(ContentType.JSON)
            .build();


    public static RequestSpecification getRequestSpecification() {
        return REQUEST_SPECIFICATION;
    }

    public static RequestSpecification getRequestSpecificationNoAuth() {
        return REQUEST_SPECIFICATION_NO_AUTH;
    }

    public static ResponseSpecification getOkResponseSpecification() {
        return OK_RESPONSE_SPECIFICATION;
    }

    public static ResponseSpecification getErrorResponseSpecification() {
        return ERROR_RESPONSE_SPECIFICATION;
    }

    public static ResponseSpecification getNotFoundResponseSpecification() {
        return NOT_FOUND_RESPONSE_SPECIFICATION;
    }

    public static ResponseSpecification getUnauthorizedResponseSpecification() {
        return UNAUTHORIZED_RESPONSE_SPECIFICATION;
    }
}

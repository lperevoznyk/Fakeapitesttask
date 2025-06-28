package clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.ApiConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;

import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public abstract class BaseClient {

    static final ApiConfig CONFIG = ConfigFactory.create(ApiConfig.class);
    static final String BASE_URL = CONFIG.getBaseUrl();
    static final RequestSpecification BASE_REQUEST_SPEC = new RequestSpecBuilder()
            .setBaseUri(BASE_URL)
            .addHeader(CONTENT_TYPE, APPLICATION_JSON.toString())
            .build();
    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

}

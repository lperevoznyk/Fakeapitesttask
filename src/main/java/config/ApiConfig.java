package config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:api.properties")
public interface ApiConfig extends Config {
    @Key("baseUrl")
    String getBaseUrl();
    @Key("booksPath")
    String getBooksPath();
    @Key("authorsPath")
    String getAuthorsPath();
}

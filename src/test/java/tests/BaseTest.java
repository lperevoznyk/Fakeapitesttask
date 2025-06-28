package tests;

import clients.AuthorsClient;
import clients.BookClient;
import org.testng.annotations.Listeners;

@Listeners({io.qameta.allure.testng.AllureTestNg.class})
public class BaseTest {
    protected AuthorsClient authorsClient;
    protected BookClient bookClient;
}

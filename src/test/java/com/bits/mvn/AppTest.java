package com.bits.mvn;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.bits.mvn.App;

public class AppTest {
    @Test
    public void testGreetWithValidName(){
        App app = new App();
        String actualValue = app.greet("BITS");
        String expectedValue = "hello BITS!";
        String message = "result is not matching actual:" + actualValue + " expected:" + expectedValue;
        assertEquals(expectedValue,actualValue,message);
    }
}

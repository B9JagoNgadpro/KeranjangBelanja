package jagongadpro.keranjang.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WebResponseTest {

    @Test
    public void testWebResponseConstructorAndGetters() {
        WebResponse<String> webResponse = new WebResponse<>("data", "error");

        assertEquals("data", webResponse.getData());
        assertEquals("error", webResponse.getErrors());
    }

    @Test
    public void testWebResponseSetters() {
        WebResponse<String> webResponse = new WebResponse<>();
        webResponse.setData("data");
        webResponse.setErrors("error");

        assertEquals("data", webResponse.getData());
        assertEquals("error", webResponse.getErrors());
    }

    @Test
    public void testWebResponseBuilder() {
        WebResponse<String> webResponse = WebResponse.<String>builder()
                .data("data")
                .errors("error")
                .build();

        assertEquals("data", webResponse.getData());
        assertEquals("error", webResponse.getErrors());
    }

    @Test
    public void testWebResponseBuilderEmpty() {
        WebResponse<String> webResponse = WebResponse.<String>builder().build();

        assertNull(webResponse.getData());
        assertNull(webResponse.getErrors());
    }

    @Test
    public void testWebResponseToString() {
        WebResponse<String> webResponse = WebResponse.<String>builder()
                .data("data")
                .errors("error")
                .build();

        String expectedString = "WebResponse(data=data, errors=error)";
        assertEquals(expectedString, webResponse.toString());
    }
}

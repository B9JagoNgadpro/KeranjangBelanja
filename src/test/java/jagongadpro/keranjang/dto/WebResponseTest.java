package jagongadpro.keranjang.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WebResponseTest {

    @Test
    public void testDefaultConstructor() {
        WebResponse<String> response = new WebResponse<>();
        assertNull(response.getData());
        assertNull(response.getErrors());
    }

    @Test
    public void testAllArgsConstructor() {
        WebResponse<String> response = new WebResponse<>("data", "errors");
        assertEquals("data", response.getData());
        assertEquals("errors", response.getErrors());
    }

    @Test
    public void testSetData() {
        WebResponse<String> response = new WebResponse<>();
        response.setData("data");
        assertEquals("data", response.getData());
    }

    @Test
    public void testSetErrors() {
        WebResponse<String> response = new WebResponse<>();
        response.setErrors("errors");
        assertEquals("errors", response.getErrors());
    }

    @Test
    public void testToString() {
        WebResponse<String> response = new WebResponse<>("data", "errors");
        assertEquals("WebResponse(data=data, errors=errors)", response.toString());
    }
}

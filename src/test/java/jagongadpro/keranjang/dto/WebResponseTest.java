package jagongadpro.keranjang.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

 class WebResponseTest {

    @Test
     void testDefaultConstructor() {
        WebResponse<String> response = new WebResponse<>();
        assertNull(response.getData());
        assertNull(response.getErrors());
    }

    @Test
     void testAllArgsConstructor() {
        WebResponse<String> response = new WebResponse<>("data", "errors");
        assertEquals("data", response.getData());
        assertEquals("errors", response.getErrors());
    }

    @Test
     void testSetData() {
        WebResponse<String> response = new WebResponse<>();
        response.setData("data");
        assertEquals("data", response.getData());
    }

    @Test
     void testSetErrors() {
        WebResponse<String> response = new WebResponse<>();
        response.setErrors("errors");
        assertEquals("errors", response.getErrors());
    }

    @Test
     void testToString() {
        WebResponse<String> response = new WebResponse<>("data", "errors");
        assertEquals("WebResponse(data=data, errors=errors)", response.toString());
    }
}

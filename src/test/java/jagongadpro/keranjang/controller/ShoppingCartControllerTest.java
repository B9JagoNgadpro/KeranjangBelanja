package jagongadpro.keranjang.controller;

import jagongadpro.keranjang.dto.KeranjangResponse;
import jagongadpro.keranjang.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShoppingCartController.class)
public class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartService shoppingCartService;

    private KeranjangResponse sampleResponse;
    private KeranjangResponse emptyResponse;

    @BeforeEach
    public void setUp() {
        sampleResponse = new KeranjangResponse("test@example.com", Map.of("item1", 2), 200.0);
        emptyResponse = new KeranjangResponse("test@example.com", Map.of(), 0.0);
    }

    @Test
    public void testHome() throws Exception {
        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to the Shopping Cart API"));
    }

    @Test
    public void testAddItem() throws Exception {
        when(shoppingCartService.addItem(anyString(), anyString(), eq(2))).thenReturn(sampleResponse);

        mockMvc.perform(post("/api/cart/add")
                        .param("email", "test@example.com")
                        .param("itemId", "item1")
                        .param("quantity", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.items.item1").value(2))
                .andExpect(jsonPath("$.totalPrice").value(200.0));
    }

    @Test
    public void testAddItem_withInvalidArgument() throws Exception {
        when(shoppingCartService.addItem(anyString(), anyString(), eq(2))).thenThrow(new IllegalArgumentException("Invalid item"));

        mockMvc.perform(post("/api/cart/add")
                        .param("email", "test@example.com")
                        .param("itemId", "item1")
                        .param("quantity", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid item"));
    }

    @Test
    public void testRemoveItem() throws Exception {
        when(shoppingCartService.deleteItem(anyString(), anyString())).thenReturn(sampleResponse);

        mockMvc.perform(delete("/api/cart/remove")
                        .param("email", "test@example.com")
                        .param("itemId", "item1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.items.item1").value(2))
                .andExpect(jsonPath("$.totalPrice").value(200.0));
    }

    @Test
    public void testUpdateItem() throws Exception {
        when(shoppingCartService.updateItem(anyString(), anyString(), eq(3))).thenReturn(sampleResponse);

        mockMvc.perform(put("/api/cart/update")
                        .param("email", "test@example.com")
                        .param("itemId", "item1")
                        .param("quantity", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.items.item1").value(2))
                .andExpect(jsonPath("$.totalPrice").value(200.0));
    }

    @Test
    public void testViewCart() throws Exception {
        when(shoppingCartService.findCartByEmail(anyString())).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/cart/view/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.items.item1").value(2))
                .andExpect(jsonPath("$.totalPrice").value(200.0));
    }

    @Test
    public void testViewCart_notFound() throws Exception {
        when(shoppingCartService.findCartByEmail(anyString())).thenReturn(null);

        mockMvc.perform(get("/api/cart/view/test@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testClearCart() throws Exception {
        when(shoppingCartService.clearCart(anyString())).thenReturn(emptyResponse);

        mockMvc.perform(delete("/api/cart/clear/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.items").isEmpty())
                .andExpect(jsonPath("$.totalPrice").value(0.0));
    }
}

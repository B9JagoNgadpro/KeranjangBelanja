package jagongadpro.keranjang.controller;

import jagongadpro.keranjang.dto.KeranjangResponse;
import jagongadpro.keranjang.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShoppingCartController.class)
class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartService shoppingCartService;

    private KeranjangResponse sampleResponse;
    private KeranjangResponse emptyResponse;

    @BeforeEach
    void setUp() {
        sampleResponse = new KeranjangResponse("test@example.com", Map.of("item1", 2), 200);
        emptyResponse = new KeranjangResponse("test@example.com", Map.of(), 0);
    }

    @Test
    void testHome() throws Exception {
        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to the Shopping Cart API"));
    }

    @Test
    void testAddItem() throws Exception {
        when(shoppingCartService.addItem(anyString(), anyString(), eq(2))).thenReturn(sampleResponse);

        mockMvc.perform(post("/api/cart/add")
                        .param("email", "test@example.com")
                        .param("itemId", "item1")
                        .param("quantity", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.items.item1").value(2))
                .andExpect(jsonPath("$.totalPrice").value(200));
    }

    @Test
    void testAddItem_withInvalidArgument() throws Exception {
        when(shoppingCartService.addItem(anyString(), anyString(), eq(2))).thenThrow(new IllegalArgumentException("Invalid item"));

        mockMvc.perform(post("/api/cart/add")
                        .param("email", "test@example.com")
                        .param("itemId", "item1")
                        .param("quantity", "2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testIncrementItem() throws Exception {
        when(shoppingCartService.incrementItem(anyString(), anyString())).thenReturn(sampleResponse);

        mockMvc.perform(post("/api/cart/increment")
                        .param("email", "test@example.com")
                        .param("itemId", "item1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.items.item1").value(2))
                .andExpect(jsonPath("$.totalPrice").value(200));
    }

    @Test
    void testDecrementItem() throws Exception {
        when(shoppingCartService.decrementItem(anyString(), anyString())).thenReturn(sampleResponse);

        mockMvc.perform(post("/api/cart/decrement")
                        .param("email", "test@example.com")
                        .param("itemId", "item1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.items.item1").value(2))
                .andExpect(jsonPath("$.totalPrice").value(200));
    }

    @Test
    void testRemoveItem() throws Exception {
        when(shoppingCartService.deleteItem(anyString(), anyString())).thenReturn(sampleResponse);

        mockMvc.perform(delete("/api/cart/remove")
                        .param("email", "test@example.com")
                        .param("itemId", "item1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.items.item1").value(2))
                .andExpect(jsonPath("$.totalPrice").value(200));
    }

    @Test
    void testUpdateItem() throws Exception {
        when(shoppingCartService.updateItem(anyString(), anyString(), eq(3))).thenReturn(sampleResponse);

        mockMvc.perform(put("/api/cart/update")
                        .param("email", "test@example.com")
                        .param("itemId", "item1")
                        .param("quantity", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.items.item1").value(2))
                .andExpect(jsonPath("$.totalPrice").value(200));
    }

    @Test
    void testViewCart() throws Exception {
        when(shoppingCartService.findCartByEmail(anyString())).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/cart/view/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.items.item1").value(2))
                .andExpect(jsonPath("$.totalPrice").value(200));
    }

    @Test
    void testViewCart_notFound() throws Exception {
        when(shoppingCartService.findCartByEmail(anyString())).thenReturn(null);

        mockMvc.perform(get("/api/cart/view/test@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testClearCart() throws Exception {
        when(shoppingCartService.clearCart(anyString())).thenReturn(emptyResponse);

        mockMvc.perform(delete("/api/cart/clear/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.items").isEmpty())
                .andExpect(jsonPath("$.totalPrice").value(0));
    }

    @Test
    void testCreateCart() throws Exception {
        KeranjangResponse response = new KeranjangResponse("newuser@example.com", Map.of(), 0);

        when(shoppingCartService.createCart(anyString())).thenReturn(response);

        mockMvc.perform(post("/api/cart/create/newuser@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.items").isEmpty())
                .andExpect(jsonPath("$.totalPrice").value(0));

        verify(shoppingCartService, times(1)).createCart("newuser@example.com");
    }

    @Test
    void testCreateCartAlreadyExists() throws Exception {
        when(shoppingCartService.createCart(anyString())).thenThrow(new IllegalArgumentException("Keranjang dengan email ini sudah ada."));

        mockMvc.perform(post("/api/cart/create/test@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(shoppingCartService, times(1)).createCart("test@example.com");
    }

    @Test
    void testGetTotalPrice() throws Exception {
        when(shoppingCartService.getTotalPrice(anyString())).thenReturn(200);

        mockMvc.perform(get("/api/cart/total/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("200"));
    }

    @Test
    void testGetTotalPrice_notFound() throws Exception {
        when(shoppingCartService.getTotalPrice(anyString())).thenThrow(new IllegalArgumentException("Keranjang dengan email tersebut tidak ditemukan."));

        mockMvc.perform(get("/api/cart/total/test@example.com"))
                .andExpect(status().isNotFound());
    }
}

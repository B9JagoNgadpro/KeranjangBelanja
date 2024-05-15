// package jagongadpro.keranjang.controller;

// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import jagongadpro.keranjang.dto.KeranjangResponse;
// import jagongadpro.keranjang.model.ShoppingCart;
// import jagongadpro.keranjang.service.ShoppingCartService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.HashMap;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// @SpringBootTest
// @AutoConfigureMockMvc
// public class ShoppingCartControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @Autowired
//     private ShoppingCartService shoppingCartService;  

//     @BeforeEach
//     void setUp() {

//     }

//     @Test
//     public void testAddItem() throws Exception {
//         mockMvc.perform(post("/api/cart/add")
//                 .param("email", "user@example.com")
//                 .param("itemId", "100")
//                 .param("quantity", "2")
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.email").value("user@example.com"))
//                 .andExpect(jsonPath("$.totalPrice").isNumber())
//                 .andDo(print());
//     }

//     @Test
//     public void testRemoveItem() throws Exception {
//         mockMvc.perform(delete("/api/cart/remove")
//                 .param("email", "user@example.com")
//                 .param("itemId", "100")
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andDo(print());
//     }

//     @Test
//     public void testUpdateItem() throws Exception {
//         mockMvc.perform(put("/api/cart/update")
//                 .param("email", "user@example.com")
//                 .param("itemId", "100")
//                 .param("quantity", "5")
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.totalPrice").isNumber())
//                 .andDo(print());
//     }

//     @Test
//     public void testViewCart() throws Exception {
//         mockMvc.perform(get("/api/cart/view/user@example.com")
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.email").value("user@example.com"))
//                 .andDo(print());
//     }

//     @Test
//     public void testClearCart() throws Exception {
//         mockMvc.perform(delete("/api/cart/clear/user@example.com")
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andDo(print());
//     }
// }

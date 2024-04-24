// package jagongadpro.keranjang.controller;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.web.servlet.MockMvc;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

// @SpringBootTest
// @AutoConfigureMockMvc
// public class ShoppingCartControllerTest {
//     @Autowired
//     private MockMvc mockMvc;

//     @Test
//     public void testAddItemToCart() throws Exception {
//         mockMvc.perform(post("/api/cart/add")
//                 .contentType("application/json")
//                 .content("{\"id\":\"game001\",\"quantity\":1}"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.status").value("success"))
//                 .andExpect(jsonPath("$.message").value("Item added successfully"));
//     }

//     @Test
//     public void testRemoveItemFromCart() throws Exception {
//         mockMvc.perform(post("/api/cart/remove")
//                 .contentType("application/json")
//                 .content("{\"id\":\"game001\"}"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.status").value("success"))
//                 .andExpect(jsonPath("$.message").value("Item removed successfully"));
//     }

//     @Test
//     public void testUpdateItemInCart() throws Exception {
//         mockMvc.perform(post("/api/cart/update")
//                 .contentType("application/json")
//                 .content("{\"id\":\"game001\",\"quantity\":2}"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.status").value("success"))
//                 .andExpect(jsonPath("$.message").value("Item updated successfully"));
//     }
// }

// package jagongadpro.keranjang.service;

// import jagongadpro.keranjang.model.ShoppingCart;
// import jagongadpro.keranjang.dto.KeranjangResponse;
// import jagongadpro.keranjang.repository.ShoppingCartRepository;
// import jagongadpro.keranjang.dto.GameResponse;
// import jagongadpro.keranjang.dto.WebResponse;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.core.ParameterizedTypeReference;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.client.RestTemplate;

// import java.util.List;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.*;

// @SpringBootTest
// public class ShoppingCartServiceTest {

//     @Mock
//     private ShoppingCartRepository shoppingCartRepository;

//     @Mock
//     @Qualifier("discountPricingStrategy")
//     private BillingStrategy discountStrategy;

//     @Mock
//     @Qualifier("normalPricingStrategy")
//     private BillingStrategy normalStrategy;

//     @Mock
//     private RestTemplate restTemplate;

//     @InjectMocks
//     private ShoppingCartService shoppingCartService;

//     @BeforeEach
//     public void setUp() {
//         MockitoAnnotations.openMocks(this);
//         shoppingCartService = new ShoppingCartService(shoppingCartRepository, discountStrategy, normalStrategy, restTemplate);
//     }

//     @Test
//     public void testAddItem() {
//         String email = "test@example.com";
//         String itemId = "Nama Game";
//         int quantity = 2;

//         ShoppingCart cart = new ShoppingCart(email);
//         when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

//         GameResponse gameResponse = new GameResponse();
//         gameResponse.setNama(itemId);
//         gameResponse.setHarga(10000);

//         WebResponse<List<GameResponse>> webResponse = new WebResponse<>();
//         webResponse.setData(List.of(gameResponse));

//         ResponseEntity<WebResponse<List<GameResponse>>> responseEntity = ResponseEntity.ok(webResponse);
//         ParameterizedTypeReference<WebResponse<List<GameResponse>>> responseType = new ParameterizedTypeReference<>() {};

//         when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(responseType)))
//                 .thenReturn(responseEntity);

//         when(discountStrategy.calculateTotal(anyMap(), anyMap())).thenReturn(20000.0);

//         KeranjangResponse response = shoppingCartService.addItem(email, itemId, quantity);

//         assertThat(response).isNotNull();
//         assertThat(response.getEmail()).isEqualTo(email);
//         assertThat(response.getItems().get(itemId)).isEqualTo(quantity);
//         assertThat(response.getTotalPrice()).isEqualTo(20000.0);

//         verify(shoppingCartRepository, times(1)).save(cart);
//     }

//     @Test
//     public void testUpdateItem() {
//         String email = "test@example.com";
//         String itemId = "Nama Game";
//         int quantity = 3;

//         ShoppingCart cart = new ShoppingCart(email);
//         cart.getItems().put(itemId, 1);
//         when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

//         GameResponse gameResponse = new GameResponse();
//         gameResponse.setNama(itemId);
//         gameResponse.setHarga(10000);

//         WebResponse<List<GameResponse>> webResponse = new WebResponse<>();
//         webResponse.setData(List.of(gameResponse));

//         ResponseEntity<WebResponse<List<GameResponse>>> responseEntity = ResponseEntity.ok(webResponse);
//         ParameterizedTypeReference<WebResponse<List<GameResponse>>> responseType = new ParameterizedTypeReference<>() {};

//         when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(responseType)))
//                 .thenReturn(responseEntity);

//         when(discountStrategy.calculateTotal(anyMap(), anyMap())).thenReturn(30000.0);

//         KeranjangResponse response = shoppingCartService.updateItem(email, itemId, quantity);

//         assertThat(response).isNotNull();
//         assertThat(response.getEmail()).isEqualTo(email);
//         assertThat(response.getItems().get(itemId)).isEqualTo(quantity);
//         assertThat(response.getTotalPrice()).isEqualTo(30000.0);

//         verify(shoppingCartRepository, times(1)).save(cart);
//     }

//     @Test
//     public void testDeleteItem() {
//         String email = "test@example.com";
//         String itemId = "Nama Game";

//         ShoppingCart cart = new ShoppingCart(email);
//         cart.getItems().put(itemId, 1);
//         when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

//         GameResponse gameResponse = new GameResponse();
//         gameResponse.setNama(itemId);
//         gameResponse.setHarga(10000);

//         WebResponse<List<GameResponse>> webResponse = new WebResponse<>();
//         webResponse.setData(List.of(gameResponse));

//         ResponseEntity<WebResponse<List<GameResponse>>> responseEntity = ResponseEntity.ok(webResponse);
//         ParameterizedTypeReference<WebResponse<List<GameResponse>>> responseType = new ParameterizedTypeReference<>() {};

//         when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(responseType)))
//                 .thenReturn(responseEntity);

//         when(discountStrategy.calculateTotal(anyMap(), anyMap())).thenReturn(0.0);

//         shoppingCartService.deleteItem(email, itemId);

//         assertThat(cart.getItems().containsKey(itemId)).isFalse();
//         assertThat(cart.getTotalPrice()).isEqualTo(0.0);

//         verify(shoppingCartRepository, times(1)).save(cart);
//     }

//     @Test
//     public void testFindCartByEmail() {
//         String email = "test@example.com";
//         ShoppingCart cart = new ShoppingCart(email);
//         when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

//         KeranjangResponse response = shoppingCartService.findCartByEmail(email);

//         assertThat(response).isNotNull();
//         assertThat(response.getEmail()).isEqualTo(email);

//         verify(shoppingCartRepository, times(1)).findByEmail(email);
//     }

//     @Test
//     public void testClearCart() {
//         String email = "test@example.com";
//         ShoppingCart cart = new ShoppingCart(email);
//         cart.getItems().put("Nama Game", 1);
//         when(shoppingCartRepository.findByEmail(email)).thenReturn(cart);

//         shoppingCartService.clearCart(email);

//         assertThat(cart.getItems()).isEmpty();
//         assertThat(cart.getTotalPrice()).isEqualTo(0.0);

//         verify(shoppingCartRepository, times(1)).save(cart);
//     }
// }

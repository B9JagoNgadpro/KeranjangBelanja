package jagongadpro.keranjang.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

 class BillingStrategyTest {

    @Test
     void testCalculateTotal() {
        // Membuat mock dari BillingStrategy
        BillingStrategy billingStrategy = Mockito.mock(BillingStrategy.class);

        // Data untuk pengujian
        Map<String, Integer> itemQuantities = new HashMap<>();
        itemQuantities.put("item1", 2);
        itemQuantities.put("item2", 3);

        Map<String, Double> itemPrices = new HashMap<>();
        itemPrices.put("item1", 10.0);
        itemPrices.put("item2", 20.0);

        // Menentukan perilaku mock
        when(billingStrategy.calculateTotal(itemQuantities, itemPrices)).thenReturn(80.0);

        // Menghitung total menggunakan mock
        double total = billingStrategy.calculateTotal(itemQuantities, itemPrices);

        // Memastikan hasil yang diharapkan
        assertEquals(80.0, total);

        // Verifikasi bahwa metode calculateTotal dipanggil dengan parameter yang tepat
        verify(billingStrategy, times(1)).calculateTotal(itemQuantities, itemPrices);
    }

    @Test
     void testCalculateTotalWithMissingPrice() {
        // Membuat mock dari BillingStrategy
        BillingStrategy billingStrategy = Mockito.mock(BillingStrategy.class);

        // Data untuk pengujian
        Map<String, Integer> itemQuantities = new HashMap<>();
        itemQuantities.put("item1", 2);
        itemQuantities.put("item2", 3);

        Map<String, Double> itemPrices = new HashMap<>();
        itemPrices.put("item1", 10.0);
        // item2 tidak ada harga

        // Menentukan perilaku mock
        when(billingStrategy.calculateTotal(itemQuantities, itemPrices)).thenReturn(20.0);

        // Menghitung total menggunakan mock
        double total = billingStrategy.calculateTotal(itemQuantities, itemPrices);

        // Memastikan hasil yang diharapkan
        assertEquals(20.0, total);

        // Verifikasi bahwa metode calculateTotal dipanggil dengan parameter yang tepat
        verify(billingStrategy, times(1)).calculateTotal(itemQuantities, itemPrices);
    }

    @Test
     void testCalculateTotalWithEmptyMaps() {
        // Membuat mock dari BillingStrategy
        BillingStrategy billingStrategy = Mockito.mock(BillingStrategy.class);

        // Data untuk pengujian
        Map<String, Integer> itemQuantities = new HashMap<>();
        Map<String, Double> itemPrices = new HashMap<>();

        // Menentukan perilaku mock
        when(billingStrategy.calculateTotal(itemQuantities, itemPrices)).thenReturn(0.0);

        // Menghitung total menggunakan mock
        double total = billingStrategy.calculateTotal(itemQuantities, itemPrices);

        // Memastikan hasil yang diharapkan
        assertEquals(0.0, total);

        // Verifikasi bahwa metode calculateTotal dipanggil dengan parameter yang tepat
        verify(billingStrategy, times(1)).calculateTotal(itemQuantities, itemPrices);
    }
}

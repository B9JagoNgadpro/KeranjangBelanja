package jagongadpro.keranjang.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class KeranjangResponse {
    private String email;
    private Map<String, Integer> items;
    private double totalPrice;
}

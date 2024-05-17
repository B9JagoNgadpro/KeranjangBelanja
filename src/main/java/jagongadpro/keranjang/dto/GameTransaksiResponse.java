package jagongadpro.keranjang.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameTransaksiResponse {
    private String nama;
    private Integer quantity;
    private Integer hargaSatuan;
    private Integer total;
}

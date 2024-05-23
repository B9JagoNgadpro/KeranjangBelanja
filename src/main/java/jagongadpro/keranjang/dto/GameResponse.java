package jagongadpro.keranjang.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameResponse {
    private String id;
    private String nama;
    private String deskripsi;
    private Integer harga;
    private String kategori;
    private Integer stok;
}

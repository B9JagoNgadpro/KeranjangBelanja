package jagongadpro.keranjang.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor // Anotasi untuk menghasilkan constructor tanpa argumen
@AllArgsConstructor // Anotasi untuk menghasilkan constructor dengan semua argumen
public class WebResponse<T> {
    private T data; // Data yang akan dikirimkan sebagai respons
    private String message; // Pesan yang dapat digunakan untuk menyampaikan informasi tambahan atau kesalahan
    private boolean success; // Status untuk menunjukkan apakah operasi berhasil atau tidak
}

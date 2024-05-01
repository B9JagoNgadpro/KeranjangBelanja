package jagongadpro.keranjang.dto;

public class WebResponse<T> {
    private T data; // Data yang akan dikirimkan sebagai respons
    private String message; // Pesan yang dapat digunakan untuk menyampaikan informasi tambahan atau kesalahan
    private boolean success; // Status untuk menunjukkan apakah operasi berhasil atau tidak

    public WebResponse() {}

    public WebResponse(T data, String message, boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    // Getter dan Setter
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

package jagongadpro.keranjang.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {
    private T data;
    private String errors;

    @Override
    public String toString() {
        return "WebResponse(data=" + data + ", errors=" + errors + ")";
    }
}

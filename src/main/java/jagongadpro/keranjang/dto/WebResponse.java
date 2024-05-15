package jagongadpro.keranjang.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class WebResponse<T> {
    private T data; 
    private String message; 
    private boolean success; 
}

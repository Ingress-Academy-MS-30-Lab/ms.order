package az.ingress.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingAddressRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String addressLine1;
    private String addressLine2;

    @NotBlank
    private String city;
    private String region;
    private String postalCode;

    @NotBlank
    private String country;

    @NotBlank
    private String phone;

}

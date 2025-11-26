package az.ingress.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "shipping_addresses")
public class ShippingAddressEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String fullName;

    private String addressLine1;
    private String addressLine2;

    private String city;
    private String region;
    private String postalCode;

    private String country;

    private String phone;

    @OneToOne(mappedBy = "shippingAddress")
    private OrderEntity order;
}
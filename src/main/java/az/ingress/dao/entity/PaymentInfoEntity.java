package az.ingress.dao.entity;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "payment_info")
public class PaymentInfoEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String paymentMethod;
    private String transactionId;
    private Boolean paymentSuccess;

    @OneToOne(mappedBy = "paymentInfo")
    private OrderEntity order;
}
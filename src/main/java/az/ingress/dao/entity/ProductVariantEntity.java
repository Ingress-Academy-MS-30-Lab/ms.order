    package az.ingress.dao.entity;

    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.EqualsAndHashCode;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import javax.persistence.Entity;
    import javax.persistence.FetchType;
    import javax.persistence.GeneratedValue;
    import javax.persistence.Id;
    import javax.persistence.JoinColumn;
    import javax.persistence.ManyToOne;
    import javax.persistence.Table;
    import java.math.BigDecimal;

    import static javax.persistence.GenerationType.IDENTITY;

    @Entity
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(of = "id")
    @Table(name = "product_variant")
    public class ProductVariantEntity {

        @Id
        @GeneratedValue(strategy = IDENTITY)
        private Long id;

        private Long productVariantId;
        private String imageUrl;
        private BigDecimal price;
        private boolean onSale;
        private BigDecimal salePrice;
        private int requestedQuantity;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "order_item_id")
        private OrderItemEntity orderItem;

    }

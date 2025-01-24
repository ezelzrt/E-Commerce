package ar.uba.fi.ingsoft1.orders.repository;

import ar.uba.fi.ingsoft1.user.User;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_order")
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime orderDate;
    
    @Builder.Default
    private LocalDateTime confirmationDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ENTERED', 'CONFIRMED', 'IN_PROGRESS', 'DELIVERED', 'CANCELLED')")
    private OrderStatus status = OrderStatus.ENTERED; // Default value

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;

}

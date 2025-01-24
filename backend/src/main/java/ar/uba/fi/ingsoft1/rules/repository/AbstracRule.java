package ar.uba.fi.ingsoft1.rules.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_Rules")
public class AbstracRule {
    /*
        Regla para persistencia en la base de datos.
        Al cargarse la regla, la misma se transforma (into_rule) a la regla concreta

        Ejemplos practicos
        Regla: No podes llevar mas de cinco remeras
        Persistido como:
            rulesFollower = MASTER_PRODUCT
            rulesFollowerID = Id de remera en la base de master products
            operation = LOWER
            value = 5

        Regla: No podes llevar mas de cinco remeras XXL rojas (producto= Remera XXL Roja)
            rulesFollower = PRODUCT     
            (el resto es igual al anterior)
        
        Regla: No podes llevar cosas de talle XXL (error en fabricacion de ese talle)
            rulesFollower = ATTRIBUTES
            rulesFollowerID = Id del atributo talle=XXL en la tabla correspondiente
            operation = LOWER
            value = 0

        Regla: La suma del peso de los productos debe ser menor a 10 
            rulesFollower = ATTRIBUTES
            rulesFollowerID = Id del atributo peso
            operation = SUM_LOWER
            value = 10 
        
        Regla: No se puede enviar producto liquido con gaseoso
            rulesFollower = ATTRIBUTES
            rulesFollowerId(rule group id=x, regla1) = ID atributo Liquido
            rulesFollowerId(rule group id=x, regla2) = ID atributo gaseoso
        Opcion 1 
            operation = COMBINATION
            value (ambas reglas) = n/a      ( y el rules tiene que saber resolver)
        Opcion 2 
            operation = LOWER
            value (ambas reglas) = 1        
    */
    @Id
    @GeneratedValue
    private Long id;

    // @Column(nullable = false)
    private Long ruleGroupId;         // Agrupamiento de reglas

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RulesFollowerType rulesFollowerType;    // Tipo de objeto

    @Column(nullable = false)
    private Long rulesFollowerId;         // Id del objeto concreto en la base de datos 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleOperation operation;    // Operacion

    @Column(nullable = false)
    private Long value;               // Valor a comparar

    @Builder.Default
    private Boolean state = false;

    // Transforma de RulePersistence a la regla particular que implementa la interfaz regla
    public Rule intoRule(){
        return this.operation.intoRules(this);
    }

    public void switchState() {
        this.state = !this.state;
    }

}

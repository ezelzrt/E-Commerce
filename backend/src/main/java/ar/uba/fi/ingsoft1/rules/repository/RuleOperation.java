package ar.uba.fi.ingsoft1.rules.repository;

public enum RuleOperation {
    // Cantidad de producto
    LOWER{
        @Override
        public Rule intoRules(AbstracRule abstracRule){
            return new RuleNumericLower(abstracRule);
        }
    },
    LOWER_EQUAL{
        @Override
        public Rule intoRules(AbstracRule abstracRule){
            return new RuleNumericLowerEqual(abstracRule);
        }
    },
    EQUAL{
        @Override
        public Rule intoRules(AbstracRule abstracRule){
            return new RuleNumericEqual(abstracRule);
        }
    },
    HIGHER{
        @Override
        public Rule intoRules(AbstracRule abstracRule){
            return new RuleNumericHigher(abstracRule);
        }
    }, 
    HIGHER_EQUAL{
        @Override
        public Rule intoRules(AbstracRule abstracRule){
            return new RuleNumericHigherEqual(abstracRule);
        }
    },
    SUM {
        @Override
        public Rule intoRules(AbstracRule abstracRule){
            return new RuleSum(abstracRule);
        }
    },
    COMBINATION {
        @Override
        public Rule intoRules(AbstracRule abstracRule){
            return new RuleCombination(abstracRule);
        }
    };

    // Otras operaciones
//    COMBINATION,  // líquido con gaseoso
//    SUM;           // Suma de atributos 

    public abstract Rule intoRules(AbstracRule abstracRule);
}

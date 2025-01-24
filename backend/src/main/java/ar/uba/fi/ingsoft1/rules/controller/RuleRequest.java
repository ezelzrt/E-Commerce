package ar.uba.fi.ingsoft1.rules.controller;
import ar.uba.fi.ingsoft1.rules.repository.RulesFollowerType;
import ar.uba.fi.ingsoft1.rules.repository.RuleOperation;

public class RuleRequest {
    
    // private Long ruleGroupId;
    
    private RulesFollowerType rulesFollowerType;
    
    private Long rulesFollowerId;
    
    private RuleOperation operation;
    
    private Long value;

    // public Long getRuleGroupId(){
    //     return ruleGroupId;
    // }
    
    public RulesFollowerType getRulesFollowerType(){
        return rulesFollowerType;
    }
    public Long getRulesFollowerId(){
        return rulesFollowerId;
    }
    public RuleOperation getOperation(){
        return operation;
    }
    public Long getValue(){
        return value;
    }

}

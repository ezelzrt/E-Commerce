package ar.uba.fi.ingsoft1.rules.service;

import ar.uba.fi.ingsoft1.rules.repository.Rule;
import ar.uba.fi.ingsoft1.rules.repository.RulesFollowerType;
import ar.uba.fi.ingsoft1.rules.repository.AbstracRule;
import ar.uba.fi.ingsoft1.rules.repository.RuleOperation;
import ar.uba.fi.ingsoft1.orders.repository.OrderDetail;
import ar.uba.fi.ingsoft1.rules.controller.RuleRequest;
import ar.uba.fi.ingsoft1.rules.repository.RuleRepository;

import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class RuleService {
    private ArrayList<Rule> rules;
    private final RuleRepository ruleRepository;

    public AbstracRule createRule(RuleRequest request){
        AbstracRule abstracRule = AbstracRule.builder()
            .rulesFollowerType(request.getRulesFollowerType())
            .rulesFollowerId(request.getRulesFollowerId())
            .operation(request.getOperation())
            .value(request.getValue())
            .build();
        AbstracRule savedRule = ruleRepository.save(abstracRule);

        // Agregar 
        rules.add(savedRule.intoRule());
        return savedRule;
    }

    // Aplica las reglas a la orden. 
    public Optional<HashMap<Long, List<OrderDetail>>> apply(Long actOrderId, HashMap<Long, List<OrderDetail>> orders) {
/* 
        Codigo con ruleGroup
        HashMap<Long, List<AbstracRule>> ruleGroups = this.getRuleGroups();
        List<AbstracRule> ruleGroup; 
        for (Long ruleGroupId: ruleGroups.keySet()){
            ruleGroup = ruleGroups.get(ruleGroupId);
            if (ruleGroup.size() == 1){
                AbstracRule abstracRule = ruleGroup.get(0);
                Rule rule = abstracRule.intoRule();
                RulesFollowerType followerType = abstracRule.getRulesFollowerType();
    	        orders = followerType.verifyWith(rule, actOrderId, orders);
            } else {
                // TODO Multiples rulegroups
            }
        }
*/
        for (AbstracRule abstracRule: getRulesByState(true)){
    	    Rule rule = abstracRule.intoRule();
	        RulesFollowerType followerType = abstracRule.getRulesFollowerType();
	        orders = followerType.verifyWith(rule, actOrderId, orders);
        }

        return Optional.of(orders);
    }

    public List<AbstracRule> getRules() {
        return ruleRepository.findAll();
    }

    public List<AbstracRule> getRulesByState(Boolean state) {
        return ruleRepository.findByState(state);
    }

    public Map<String, Object> getRulesOptions() {
        Map<String, Object> rulesOptions = new HashMap<>();
        rulesOptions.put("rulesFollowers", RulesFollowerType.values());
        rulesOptions.put("rulesOpertaions", RuleOperation.values());
        return rulesOptions;
    }

    public Boolean switchStateById(Long ruleId) {
        AbstracRule rule = ruleRepository.findById(ruleId).orElse(null);
        rule.switchState();
        return ruleRepository.save(rule).getState();
    }
    
    public HashMap<Long, List<Rule>> getRuleGroups() {
        HashMap<Long, List<Rule>> ruleGroups = new HashMap<>();

        Long ruleGroupId;
        List<Rule> ruleGroup;
        for (AbstracRule abstracRule: this.getRules()){
            ruleGroupId = abstracRule.getRuleGroupId();
            ruleGroup = ruleGroups.get(ruleGroupId);

            if ( ruleGroup == null ) {
                List<Rule> rules = new ArrayList<>();
                rules.add(abstracRule.intoRule());
                ruleGroups.put(ruleGroupId, rules);
            } else {
                ruleGroup.add(abstracRule.intoRule());
            }
        }
        return ruleGroups;
    }

}

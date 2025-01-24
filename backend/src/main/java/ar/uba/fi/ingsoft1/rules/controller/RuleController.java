package ar.uba.fi.ingsoft1.rules.controller;

import java.util.List;
import java.util.Map;

import ar.uba.fi.ingsoft1.rules.repository.AbstracRule;
import ar.uba.fi.ingsoft1.rules.service.RuleService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RuleController {
    private final RuleService rulesService;
    
    @GetMapping
    public ResponseEntity<List<AbstracRule>> getRules() {
        return ResponseEntity.ok(rulesService.getRules());
    }
    
    @GetMapping("/options")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getRulesOptions() {
        return rulesService.getRulesOptions();
    }

    @PostMapping
    public ResponseEntity<?> postRule(@RequestBody final RuleRequest request) {
        AbstracRule rule = rulesService.createRule(request);
        return ResponseEntity.ok(rule);
    }
    
    @PatchMapping("/{ruleId}/state")
    @ResponseStatus(HttpStatus.OK)
    public void patchStateById(@PathVariable Long ruleId){
        rulesService.switchStateById(ruleId);
        return;
    }
}

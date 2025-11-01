package com.workintech.dependencyinjection.rest;

import com.workintech.dependencyinjection.model.*;
import com.workintech.dependencyinjection.tax.Taxable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/workintech")
public class DeveloperController {

    private final Taxable taxService;
    private Map<Integer, Developer> developers;

    @Autowired
    public DeveloperController(Taxable taxService) {
        this.taxService = taxService;
    }

    @PostConstruct
    public void init() {
        developers = new HashMap<>();
        // Örnek data ekleyelim
        addDeveloper(new JuniorDeveloper(1, "Alice", 4000));
        addDeveloper(new MidDeveloper(2, "Bob", 6000));
        addDeveloper(new SeniorDeveloper(3, "Charlie", 10000));
    }

    private void addDeveloper(Developer developer) {
        double salary = developer.getSalary();
        switch (developer.getExperience()) {
            case JUNIOR -> salary -= salary * taxService.getSimpleTaxRate();
            case MID -> salary -= salary * taxService.getMiddleTaxRate();
            case SENIOR -> salary -= salary * taxService.getUpperTaxRate();
        }
        developer.setSalary(salary);
        developers.put(developer.getId(), developer);
    }

    @GetMapping("/developers")
    public List<Developer> getAllDevelopers() {
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/developers/{id}")
    public Developer getDeveloperById(@PathVariable int id) {
        return developers.get(id);
    }

    @PostMapping("/developers")
    public String createDeveloper(@RequestParam int id,
                                  @RequestParam String name,
                                  @RequestParam double salary,
                                  @RequestParam Experience experience) {
        Developer developer;
        switch (experience) {
            case JUNIOR -> developer = new JuniorDeveloper(id, name, salary);
            case MID -> developer = new MidDeveloper(id, name, salary);
            case SENIOR -> developer = new SeniorDeveloper(id, name, salary);
            default -> throw new IllegalArgumentException("Invalid experience level");
        }
        addDeveloper(developer);
        return "Developer added!";
    }

    @PutMapping("/developers/{id}")
    public String updateDeveloper(@PathVariable int id,
                                  @RequestParam String name,
                                  @RequestParam double salary,
                                  @RequestParam Experience experience) {
        Developer developer;
        switch (experience) {
            case JUNIOR -> developer = new JuniorDeveloper(id, name, salary);
            case MID -> developer = new MidDeveloper(id, name, salary);
            case SENIOR -> developer = new SeniorDeveloper(id, name, salary);
            default -> throw new IllegalArgumentException("Invalid experience level");
        }
        addDeveloper(developer);
        return "Developer updated!";
    }

    @DeleteMapping("/developers/{id}")
    public String deleteDeveloper(@PathVariable int id) {
        developers.remove(id);
        return "Developer removed!";
    }
}

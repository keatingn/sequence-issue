package example.sequence.issue.model;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.valuerange.CountableValueRange;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeFactory;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@PlanningSolution
public class Plan {
    private List<Period> periods;
    private List<Product> products;

    @ValueRangeProvider(id = "quantities")
    private CountableValueRange<Long> jobSizes = ValueRangeFactory.createLongValueRange(3, 35);

    @PlanningEntityCollectionProperty
    private List<Job> jobs;
    @PlanningEntityCollectionProperty
    private List<Stock> stocks;

    @PlanningScore
    private HardSoftLongScore score;

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public CountableValueRange<Long> getJobSizes() {
        return jobSizes;
    }

    public void setJobSizes(CountableValueRange<Long> jobSizes) {
        this.jobSizes = jobSizes;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public HardSoftLongScore getScore() {
        return score;
    }

    public void setScore(HardSoftLongScore score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "products=" + products +
                ", stocks=" + stocks +
                ", score=" + score +
                '}';
    }
}

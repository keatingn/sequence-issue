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
@Data
@Builder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

    private List<Period> periods;
    private List<Product> products;

    @ValueRangeProvider(id = "quantities")
    @Builder.Default
    private CountableValueRange<Long> jobSizes = ValueRangeFactory.createLongValueRange(3, 35);

    @PlanningEntityCollectionProperty
    private List<Job> jobs;
    @PlanningEntityCollectionProperty
    private List<Stock> stocks;

    @PlanningScore
    private HardSoftLongScore score;

}

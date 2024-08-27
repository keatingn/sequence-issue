package example.sequence.issue.model;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@PlanningEntity
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Product product;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Period period;

    @PlanningId
    @EqualsAndHashCode.Include private String jobId;

    @PlanningVariable(valueRangeProviderRefs = "quantities")
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Long quantity = 0L;
}

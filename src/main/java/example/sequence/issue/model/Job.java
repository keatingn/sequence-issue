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

    @ToString.Exclude
    private Product product;
    @ToString.Exclude
    private Period period;

    @PlanningId
    @EqualsAndHashCode.Include private String jobId;

    @PlanningVariable(valueRangeProviderRefs = "quantities")
    @Builder.Default
    private Long quantity = 0L;
}

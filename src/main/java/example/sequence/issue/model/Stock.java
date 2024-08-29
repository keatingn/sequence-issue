package example.sequence.issue.model;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import ai.timefold.solver.core.api.domain.variable.ShadowVariable;
import example.sequence.issue.model.solver.StockQuantityVariableListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@PlanningEntity
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @PlanningId
    @EqualsAndHashCode.Include
    private String stockId;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Period period;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Product product;
    @ShadowVariable(
            sourceEntityClass = Job.class,
            sourceVariableName = "quantity",
            variableListenerClass = StockQuantityVariableListener.class)
    @EqualsAndHashCode.Exclude
    private Long quantity;
}

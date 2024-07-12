package example.sequence.issue.repository;

import example.sequence.issue.model.Plan;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class PlanRepository {

    private final AtomicReference<Plan> planReference = new AtomicReference<>();

    public Plan read() {
        return planReference.get();
    }

    public void write(Plan schedule) {
        planReference.set(schedule);
    }
}

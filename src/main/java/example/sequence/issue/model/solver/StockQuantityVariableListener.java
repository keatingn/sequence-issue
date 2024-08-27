package example.sequence.issue.model.solver;

import ai.timefold.solver.core.api.domain.variable.VariableListener;
import ai.timefold.solver.core.api.score.director.ScoreDirector;
import example.sequence.issue.model.Job;
import example.sequence.issue.model.Period;
import example.sequence.issue.model.Plan;
import example.sequence.issue.model.Product;
import example.sequence.issue.model.Stock;

import java.util.Comparator;
import java.util.List;

public class StockQuantityVariableListener implements VariableListener<Plan, Job> {

    @Override
    public boolean requiresUniqueEntityEvents() {
        return true;
    }

    @Override
    public void beforeVariableChanged(ScoreDirector<Plan> scoreDirector, Job job) {
        if (job.getQuantity() != null) {
            adjust(scoreDirector, job, -job.getQuantity());
        }
    }

    @Override
    public void afterVariableChanged(ScoreDirector<Plan> scoreDirector, Job job) {
        if (job.getQuantity() != null) {
            adjust(scoreDirector, job, job.getQuantity());
        }
    }

    private void adjust(ScoreDirector<Plan> scoreDirector, Job job, long adjustment) {
        if (adjustment == 0) {
            return;
        }
        List<Stock> futureStocks = getFutureStocks(scoreDirector, job.getProduct(), job.getPeriod());


        for (Stock stock : futureStocks) {
            scoreDirector.beforeVariableChanged(stock, "quantity");
            stock.setQuantity(stock.getQuantity() + adjustment);
            scoreDirector.afterVariableChanged(stock, "quantity");
        }
    }

    private List<Stock> getFutureStocks(
            ScoreDirector<Plan> scoreDirector, Product product, Period period) {
        return scoreDirector.getWorkingSolution().getStocks().stream()
                .filter(stock -> stock.getProduct().equals(product))
                .filter(stock -> stock.getPeriod().index() >= period.index())
                .sorted(Comparator.comparing(stock -> stock.getPeriod().index()))
                .toList();
    }

    @Override
    public void beforeEntityAdded(ScoreDirector<Plan> scoreDirector, Job job) {

    }

    @Override
    public void afterEntityAdded(ScoreDirector<Plan> scoreDirector, Job job) {

    }

    @Override
    public void beforeEntityRemoved(ScoreDirector<Plan> scoreDirector, Job job) {

    }

    @Override
    public void afterEntityRemoved(ScoreDirector<Plan> scoreDirector, Job job) {

    }
}

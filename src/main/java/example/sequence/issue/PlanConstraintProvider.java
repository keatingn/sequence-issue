package example.sequence.issue;

import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintCollectors;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import ai.timefold.solver.core.api.score.stream.common.SequenceChain;
import example.sequence.issue.model.Job;
import example.sequence.issue.model.Period;
import example.sequence.issue.model.Stock;
import reactor.core.publisher.Flux;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlanConstraintProvider implements ConstraintProvider {

    public static final Comparator<Stock> STOCK_COMPARATOR = Comparator.comparing(Stock::getPeriod, Comparator.comparing(Period::index));

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                outOfStock(constraintFactory),
//                emptyJob(constraintFactory),
                service(constraintFactory)
        };
    }

    protected Constraint service(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Stock.class)
                .filter(stock -> stock.getQuantity() < 0)
                .penalize(HardSoftLongScore.ONE_SOFT)
                .asConstraint("service");
    }

    protected Constraint emptyJob(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Job.class)
                .filter(job -> job.getQuantity() <= 0)
                .groupBy(Job::getProduct,
                        ConstraintCollectors.toConsecutiveSequences(job -> job.getPeriod().index()))
                .flattenLast(SequenceChain::getConsecutiveSequences)
//                .filter((product, sequence) -> sequence.getCount() > 1)
                .penalize(HardSoftLongScore.ONE_HARD, (product, sequence) -> sequence.getCount() -1)
                .asConstraint("emptyJob");
    }

    protected Constraint outOfStock(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Stock.class)
                .filter(stock -> stock.getQuantity() < 0)
                .groupBy(Stock::getProduct,
                        ConstraintCollectors.toConsecutiveSequences(stock -> stock.getPeriod().index()))
                .flattenLast(SequenceChain::getConsecutiveSequences)
//                .filter((product, sequence) -> sequence.getCount() > 1)
                .penalize(HardSoftLongScore.ONE_HARD, (product, sequence) -> sequence.getCount() -1)
                .asConstraint("outOfStock");
    }

    protected Constraint outOfStockBad(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Stock.class)
                .filter(stock -> stock.getQuantity() < 0)
                .filter(stock -> {
                    System.out.println("Stock: +" + stock);
                    return true;
                })
                .groupBy(Stock::getProduct, ConstraintCollectors.toList())
                .filter((product, sequence) -> sequence.size() > 1)
                .penalize(HardSoftLongScore.ONE_HARD, (product, sequence) -> calculatePenalty(sequence))
                .asConstraint("outOfStock");
    }

    private Integer calculatePenalty(List<Stock> stocks) {
        return Flux.fromIterable(stocks)
                .sort(STOCK_COMPARATOR)
                .bufferUntilChanged(stock -> stock.getQuantity() < 0)
                .toStream()
                .filter(l -> l.getFirst().getQuantity() < 0)
                .mapToInt(List::size)
                .map(count -> count - 1)
                .sum();
    }
}


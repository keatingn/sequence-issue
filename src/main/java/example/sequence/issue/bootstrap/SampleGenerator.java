package example.sequence.issue.bootstrap;

import ai.timefold.solver.core.api.solver.SolverJob;
import ai.timefold.solver.core.api.solver.SolverManager;

import example.sequence.issue.model.Job;
import example.sequence.issue.model.Period;
import example.sequence.issue.model.Plan;
import example.sequence.issue.model.Product;
import example.sequence.issue.model.Stock;
import example.sequence.issue.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class SampleGenerator {

    private final PlanRepository planRepository;

    private final SolverManager<Plan, String> solverManager;

    @EventListener
    public void onApplicationEvent(ApplicationStartedEvent event) throws ExecutionException, InterruptedException {
        log.info("Generating plan...");

        List<Period> periods = IntStream.rangeClosed(1, 4).mapToObj(index -> new Period(index, "p" + index)).toList();
        List<Product> products = IntStream.rangeClosed(1, 1).mapToObj(index -> new Product("product-" + index)).toList();

        List<Job> jobs = new ArrayList<>();
        List<Stock> stocks = new ArrayList<>();
        for (Product product : products) {
            long demand = 0;
            for (Period period : periods) {
                var job = new Job();
                job.setJobId(period.name() + "-" + product.name());
                job.setPeriod(period);
                job.setProduct(product);
                jobs.add(job);
                demand += 10;

                var stock = new Stock();
                stock.setStockId(period.name() + "-" + product.name());
                stock.setPeriod(period);
                stock.setProduct(product);
                stock.setQuantity(-demand);
                stocks.add(stock);
            }
        }

        Plan plan = new Plan();
        plan.setPeriods(periods);
        plan.setProducts(products);
        plan.setJobs(jobs);
        plan.setStocks(stocks);
        planRepository.write(plan);

        plan.getStocks().forEach(job -> log.info("Stock:: {}", job));

        SolverJob<Plan, String> solution = solverManager.solveBuilder()
                .withProblemId("1")
                .withProblem(plan)
                .withBestSolutionConsumer(planRepository::write)
                .withExceptionHandler((jobId_, exception) -> {
                    log.error("Failed solving plan ({}).", plan.getScore(), exception);
                })
                .run();

        Plan result = solution.getFinalBestSolution();

        log.info("Solution   {}", result);


        result.getJobs().stream().filter(job -> job.getQuantity() > 0).forEach(job -> log.info("Job:: {}", job));
        result.getStocks().stream().filter(job -> job.getQuantity() < 0).forEach(job -> log.info("Stock:: {}", job));
    }
}

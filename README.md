# Sequence issue

Running `mvn spring-boot:run` results in the following output:

```
2024-07-12T14:20:58.172+01:00  INFO 68734 --- [sequence-issue] [           main] e.s.issue.SequenceIssueApplication       : Started SequenceIssueApplication in 1.253 seconds (process running for 2.047)
2024-07-12T14:20:58.173+01:00  INFO 68734 --- [sequence-issue] [           main] e.s.issue.bootstrap.SampleGenerator      : Generating plan...
2024-07-12T14:20:58.174+01:00  INFO 68734 --- [sequence-issue] [           main] e.s.issue.bootstrap.SampleGenerator      : Stock:: Stock(stockId=p1-product-1, quantity=-10)
2024-07-12T14:20:58.174+01:00  INFO 68734 --- [sequence-issue] [           main] e.s.issue.bootstrap.SampleGenerator      : Stock:: Stock(stockId=p2-product-1, quantity=-20)
2024-07-12T14:20:58.174+01:00  INFO 68734 --- [sequence-issue] [           main] e.s.issue.bootstrap.SampleGenerator      : Stock:: Stock(stockId=p3-product-1, quantity=-30)
2024-07-12T14:20:58.174+01:00  INFO 68734 --- [sequence-issue] [           main] e.s.issue.bootstrap.SampleGenerator      : Stock:: Stock(stockId=p4-product-1, quantity=-40)
2024-07-12T14:20:58.201+01:00  INFO 68734 --- [sequence-issue] [pool-1-thread-1] a.t.s.core.impl.solver.DefaultSolver     : Solving started: time spent (18), best score (-3hard/-4soft), environment mode (FULL_ASSERT), move thread count (NONE), random (JDK with seed 0).
2024-07-12T14:20:58.203+01:00  INFO 68734 --- [sequence-issue] [pool-1-thread-1] a.t.s.core.impl.solver.DefaultSolver     : Problem scale: entity count (4), variable count (4), approximate value count (32), approximate problem scale (1,048,576).
2024-07-12T14:20:58.204+01:00  INFO 68734 --- [sequence-issue] [pool-1-thread-1] .c.i.c.DefaultConstructionHeuristicPhase : Construction Heuristic phase (0) ended: time spent (21), best score (-3hard/-4soft), score calculation speed (0/sec), step total (0).
2024-07-12T14:20:58.206+01:00 TRACE 68734 --- [sequence-issue] [pool-1-thread-1] a.t.s.c.i.h.s.m.d.FilteringMoveSelector  :         Move (Job(jobId=p1-product-1, quantity=0) {0} <-> Job(jobId=p3-product-1, quantity=0) {0}) filtered out by a selection filter (Doable moves only).
2024-07-12T14:20:58.207+01:00 TRACE 68734 --- [sequence-issue] [pool-1-thread-1] a.t.s.c.i.l.decider.LocalSearchDecider   :         Move index (0), score (-3hard/-4soft), accepted (true), move (Job(jobId=p3-product-1, quantity=0) {0 -> 29}).
2024-07-12T14:20:58.208+01:00 DEBUG 68734 --- [sequence-issue] [pool-1-thread-1] a.t.s.c.i.l.DefaultLocalSearchPhase      :     LS step (0), time spent (25), score (-3hard/-4soft),     best score (-3hard/-4soft), accepted/selected move count (1/1), picked move (Job(jobId=p3-product-1, quantity=0) {0 -> 29}).
2024-07-12T14:21:02.851+01:00 ERROR 68734 --- [sequence-issue] [pool-1-thread-1] e.s.issue.bootstrap.SampleGenerator      : Failed solving plan (-3hard/-4soft).

java.lang.IllegalStateException: Score corruption (-2hard): the workingScore (-3hard/-3soft) is not the uncorruptedScore (-1hard/-3soft) after completedAction (Job(jobId=p2-product-1, quantity=29) {29} <-> Job(jobId=p3-product-1, quantity=0) {0}):
Score corruption analysis:
  The corrupted scoreDirector has 1 ConstraintMatch(es) which should not be there:
    example.sequence.issue.model/outOfStock/[Product(name=product-1), Sequence [Stock(stockId=p1-product-1, quantity=-10), Stock(stockId=p2-product-1, quantity=9), Stock(stockId=p3-product-1, quantity=-1), Stock(stockId=p4-product-1, quantity=-11)]]=-3hard/0soft
  The corrupted scoreDirector has 2 ConstraintMatch(es) which are missing:
    example.sequence.issue.model/outOfStock/[Product(name=product-1), Sequence [Stock(stockId=p3-product-1, quantity=-1), Stock(stockId=p4-product-1, quantity=-11)]]=-1hard/0soft
    example.sequence.issue.model/outOfStock/[Product(name=product-1), Sequence [Stock(stockId=p1-product-1, quantity=-10)]]=0hard/0soft
  Maybe there is a bug in the score constraints of those ConstraintMatch(s).
  Maybe a score constraint doesn't select all the entities it depends on,
    but discovers some transitively through a reference from the selected entity.
    This corrupts incremental score calculation,
    because the constraint is not re-evaluated if the transitively discovered entity changes.
Shadow variable corruption in the corrupted scoreDirector:
  None
```

The 2 `Stock` in the sequence `Stock(stockId=p2-product-1, quantity=9)` shouldn't be allowed based on 
the `.filter(stock -> stock.getQuantity() < 0)` filter in the `outOfStock` constraint.

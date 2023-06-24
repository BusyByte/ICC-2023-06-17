# ICC-2023-06-17
Iowa Code Camp - Spring 2023

## Type Driven Development in Scala
Source found in modules/type-driven-dev

### Scala
### Credit Card Domain
#### Event Storming the Problem
#### Capture Business Rules
### Methodology
- Actions (case objects to start)
- Events (case objects to start)
- Aggregates (traits with no implementations)
  - Take in action and return event
- Business Activities (traits with no implementations)
  - Fill in return types handling the business failure conditions as a branches in the sum cases
- Fill in fields on Actions / Events
### Note on Types
 - value classes
 - type class pattern

### Resources
- https://www.scala-lang.org
- https://www.scala-exercises.org
- [Event Storming - Wikipedia](https://en.wikipedia.org/wiki/Event_storming)
- [Scala 2 Type Classes 101: Introduction - by alvin alexander](https://alvinalexander.com/scala/fp-book/type-classes-101-introduction/)
- [Propositions as Types - Strange Loop Conference](https://youtu.be/IOiZatlZtGU)



## Mocks in Scala
Source found in modules/mocks-in-scala and fakes-in-scala


### Scala
### Mocks
### Functional Programming (FP)
### Problems
#### SmartNullPointerExceptions
#### Verbosity vs Correctness
#### Correctness of Effects
#### Matching Parameters
#### Matching Implicit Parameters
#### Final Classes in FP
#### Argument Capture
#### Lack of Abstraction
### My Solution
#### Compiler Options
- [sbt-tpolecat sbt plugin](https://github.com/typelevel/sbt-tpolecat)
#### Algebras
#### Fakes
#### Scalacheck
#### Benefits and Tradeoffs



### Resources
- https://www.scala-lang.org
- https://www.scala-exercises.org
- https://www.47deg.com/blog/mocking-and-how-to-avoid-it
- [Propositions as Types - Strange Loop Conference](https://youtu.be/IOiZatlZtGU)



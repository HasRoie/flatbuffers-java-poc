# flatbuffers-java-poc
Proof of concept FlatBuffers implementation of an alternative Java Api

## Links
* FlatBuffers https://github.com/google/flatbuffers
* Original Syntax Sample https://github.com/ennerf/flatbuffers-java-poc/blob/master/src/test/java/org/enner/flatbuffers/validation/GoogleTestData.java
* Proposed Syntax Sample https://github.com/ennerf/flatbuffers-java-poc/blob/master/src/test/java/org/enner/flatbuffers/validation/PocTestData.jav

## Behavioral Differences
* Values set to their defaults do not get omitted from serialization
* Table payload is not limited to contiguous memory
* Vector tables currently aren't shared, but could be
* Setting the table size is currently not implemented. However, I have some ideas on how to add it, e.g., Table.finish() can know about the sizes of each element and compute it by itself. Once the size is set, the vector table could be immutable. This could enable sharing of vector tables as well.

## Current Status
* The static methods should already provide fairly complete access to the whole protocol
* The object level implements the core functionality, but is missing some more advanced types, such as vectors of vectors etc.

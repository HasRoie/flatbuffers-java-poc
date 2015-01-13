# flatbuffers-java-poc
Proof of concept FlatBuffers implementation of an alternative Java Api

## Links
* [FlatBuffers Documentation](https://github.com/google/flatbuffers)
* [Original Syntax Sample](https://github.com/ennerf/flatbuffers-java-poc/blob/master/src/test/java/org/enner/flatbuffers/validation/GoogleTestData.java)
* [Proposed Syntax Sample](https://github.com/ennerf/flatbuffers-java-poc/blob/master/src/test/java/org/enner/flatbuffers/validation/PocTestData.java)

## Behavioral Differences
* Buffers get built top-down for simpler Syntax. Vector Table pointers get initialized when they are set. If necessary, they can be initialized to NULL using initXXX() methods
* Aside from reusable objects and String conversions, there is no allocation of dynamic memory whatsoever. The main goals for this Api are determinism and ease of use
* Raw addresses are not exposed to the user when using the object api
* Values set to their defaults do not get omitted from serialization
* Table payload is not limited to contiguous memory
* Vector tables currently aren't shared, but could be
* Setting the table size is currently not implemented. However, I have some ideas on how to add it, e.g., Table.finish() can know about the sizes of each element and compute it by itself. Once the size is set, the vector table could be immutable. This could enable sharing of vector tables as well.

## Current Status
* The static methods should already provide fairly complete access to the whole protocol
* The object level implements the core functionality, but is missing some more advanced types, such as vectors of vectors etc.
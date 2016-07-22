# spark-examples
spark operation sample codes

```scala
import org.sws9f.sparkex._

val sc = ToolSpark.sc
val persons = SampleData.persons
val rddPersons = sc.parallelize(persons, 2)

rddPerson.first

rddPersons.partitions.size
rddPersons.count

```
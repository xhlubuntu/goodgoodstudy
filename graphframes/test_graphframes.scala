
//graphframes
//1.failed in intelij
//
// added jars
// scala-logging-slf4j , scala-logging-api

//2.spark-shell
// windows 部署 hadoop 2.7.6
// https://blog.csdn.net/taogumo/article/details/80693292
// https://blog.csdn.net/chy2z/article/details/80484848
// hadoop mirror site
// https://www.apache.org/dyn/closer.cgi/hadoop/common/hadoop-2.7.7/hadoop-2.7.7.tar.gz

// namenode format
// hdfs.cmd namenode -format
// %HADOOP_HOME%\sbin\start-all.cmd
// %HADOOP_HOME%\sbin\stop-all.cmd

// %HADOOP_HOME%\bin\winutils.exe ls \tmp\hive
// %HADOOP_HOME%\bin\winutils.exe chmod 777 \tmp\hive
// %HADOOP_HOME%\bin\winutils.exe ls \tmp\hive

// run as root: windows power shell
// cd D:\code\scala\external_jars
// D:\spark\hadoop\spark-2.3.0-bin-hadoop2.7\bin\spark-shell.cmd  --master local[*] --jars graphframes-0.6.0-spark2.3-s_2.11.jar

// added jars
// scala-logging-slf4j , scala-logging-api

val spark = SparkSession.builder().appName("graphframe test").master("local[*]").getOrCreate()


val sqlContext = spark.sqlContext
// Create a Vertex DataFrame with unique ID column "id"
val v = sqlContext.createDataFrame(List(
  ("a", "Alice", 34),
  ("b", "Bob", 36),
  ("c", "Charlie", 30)
)).toDF("id", "name", "age")
// Create an Edge DataFrame with "src" and "dst" columns
val e = sqlContext.createDataFrame(List(
  ("a", "b", "friend"),
  ("b", "c", "follow"),
  ("c", "b", "follow")
)).toDF("src", "dst", "relationship")
// Create a GraphFrame

import org.graphframes._
val g = GraphFrame(v, e)

// Query: Get in-degree of each vertex.
g.inDegrees.show()

// Query: Count the number of "follow" connections in the graph.
g.edges.filter("relationship = 'follow'").count()

// Run PageRank algorithm, and show results.
val results = g.pageRank.resetProbability(0.01).maxIter(20).run()
results.vertices.select("id", "pagerank").show()

spark.stop()

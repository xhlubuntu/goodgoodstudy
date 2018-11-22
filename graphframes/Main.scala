package com.cmb.graphframeapp

import org.apache.spark.sql.SparkSession
import org.graphframes._

/**
  * Created by hualongxie on 2018/11/22.
  */
object Main {
  def main(args: Array[String]): Unit = {
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
    //e.show()

    val g = GraphFrame(v, e)

    // Query: Get in-degree of each vertex.
    g.inDegrees.show()

    // Query: Count the number of "follow" connections in the graph.
    g.edges.filter("relationship = 'follow'").count()

    // Run PageRank algorithm, and show results.
    val results = g.pageRank.resetProbability(0.01).maxIter(20).run()
    results.vertices.select("id", "pagerank").show()


    spark.stop()
  }

}

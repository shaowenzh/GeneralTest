package com.zsw

import org.apache.spark.sql.QueryTest
import org.apache.spark.sql.test.SharedSQLContext

class MainSuite extends QueryTest with SharedSQLContext {
  test("test") {
    val filepath = Thread.currentThread().getContextClassLoader.getResource("testdata/").toString
    val tokenized = spark.sparkContext.textFile(filepath).flatMap(_.split(" "))

    // count the occurrence of each word
    val wordCounts = tokenized.map((_, 1)).reduceByKey(_ + _)

    System.out.println(wordCounts.collect().mkString(", "))
    // filter out words with fewer than threshold occurrences
    val filtered = wordCounts.filter(_._2 >= 1)

    // count characters
    val charCounts = filtered.flatMap(_._1.toCharArray).map((_, 1)).reduceByKey(_ + _)

    System.out.println(charCounts.collect().mkString(", "))
  }

  test("test2") {
    var n = 0

  }
}

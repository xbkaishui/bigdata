package spark

import org.apache.spark.sql.SparkSession

/**
  * Created by xbkaishui on 16/11/27.
  */
object StructuredNetworkWordCount {

  def main(args: Array[String]) {

    if (args.length < 2) {
      System.err.println("Usage: StructuredNetworkWordCount <hostname> <port>")
      System.exit(1)
    }

    val host = args(0)
    val port = args(1).toInt

    val spark = SparkSession.builder
      .appName("StructuredNetWorkWordCount")
      .master("local[4]")
      .getOrCreate()

    import spark.implicits._

    val lines = spark.readStream.format("socket")
      .option("host", host)
      .option("port", port)
      .load().as[String]

    val words = lines.flatMap(_.split(" "))

    val wordCounts = words.groupBy("value").count()

    val query = wordCounts.writeStream
      .outputMode("complete")
      .format("console")
      .start()

    query.awaitTermination()
  }

}

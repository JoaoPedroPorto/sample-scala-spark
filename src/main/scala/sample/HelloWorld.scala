package sample

/*import org.apache.commons.lang3.StringUtils.EMPTY
import scala.collection.immutable.Stream.Empty*/
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.functions.{col, concat, lit}

object HelloWorld {

  def main(args: Array[String]): Unit = {

    // col("colName") === $"colName"

    // DELIGA ALGUMAS INFORMAÇÕES DO LOG
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    val sparkConf =
      new SparkConf()
        .setAppName("Sample Spark")
        .setMaster("local")

    // LE O JSON
    val df = new SQLContext(new SparkContext(sparkConf)).read.json("Jsons/User.json")

    // PRINTA O SCHEMA DO JSON
    df.printSchema()

    // COLLUMNS
    val columnAge      = df("age")
    val columnId       = df("id")
    val columnLastName = df("lastName")
    val columnMail     = df("mail")
    val columnName     = df("name")
    val columnProfile  = df("profile")
    val columnSex      = df("sex")
    val columnStatus   = df("status")
    val notEqualsStatus = df("status") !== "INACTIVE"

    // QUERY USERS ACTIVES
    df
      .select(columnId, columnName, columnMail, columnStatus, columnProfile)
      .where(notEqualsStatus)
      .orderBy(columnName)
      .show()

    // QUERY COUNT USERS BY PROFILE
    df
      .select(columnProfile)
      .groupBy(columnProfile)
      .count()
      .show()

    // QUERY COUNT USERS BY SEX
    df
      .select(columnSex)
      .groupBy(columnSex)
      .count()
      .show()

    // QUERY GET USER BY MAIL
    df
      .select(columnId, columnName, columnStatus)
      .filter(notEqualsStatus)
      .where(df("mail") === "joao.porto@dextra-sw.com")
      .show()

    // QUERY GET USER BY ID
    df
      .select(columnId, columnName.as("Nome"), columnMail.as("E-mail"), columnStatus)
      .where(df("id") === 3)
      .show()

    // QUERY AVERAGE BY AGE DESC
    df
      .select(columnAge)
      .groupBy(columnAge)
      .count()
      .orderBy(col("count").desc)
      .show()

    // QUERY NAME LIKE JOAO PEDRO
    df
      .filter(col("name").like("%João%"))
      .select(columnId, columnName)
      .show()

    // QUERY CONCAT NAME AND LAST NAME
    df
      .select(concat(col("name"), lit(" "), col("lastName")).as("Nome"))
      .orderBy(columnName)
      .show()

  }

}

package sample

/*import org.apache.commons.lang3.StringUtils.EMPTY
import scala.collection.immutable.Stream.Empty*/
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}


object HelloWorld {

  def main(args: Array[String]): Unit = {
    /*println("Cálculo IMC...")
    val weight: Double = 94
    val height: Double = 1.77
    println(calculateIMC(weight, height))*/
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)
    val conf = new SparkConf().setAppName("Sample Spark")
    conf.setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    // LE O JSON
    val df = sqlContext.read.json("Jsons/User.json")

    // PRINTA O SCHEMA DO JSON
    df.printSchema()

    // COLLUMNS
    val columnAge = df("age")
    val columnId = df("id")
    val columnLastName = df("lastName")
    val columnMail = df("mail")
    val columnName = df("name")
    val columnProfile = df("profile")
    val columnSex = df("sex")
    val columnStatus = df("status")

    // QUERY USERS ACTIVES
    df
      .select(columnId, columnName, columnMail, columnStatus, columnProfile)
      .where(df("status") !== "INACTIVE")
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
      .filter(df("status") !== "INACTIVE")
      .where(df("mail") === "joao.porto@dextra-sw.com")
      .show()

    df
      .select(columnId, columnName.as("Nome"), columnMail.as("E-mail"), columnStatus)
      .where(df("id") === 3)
      .show()

  }

  /*private def calculateIMC(weight: Double, height: Double): String = {
    var imc = weight / Math.pow(height, 2)
    return imc match {
      case value if (value < 17)                      => "IMC: " + BigDecimal(value).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble + " Muito abaixo do peso"
      case value if (value >= 17 && value <= 18.49)   => "IMC: " + BigDecimal(value).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble + " Abaixo do peso"
      case value if (value >= 18.5 && value <= 24.99)	=> "IMC: " + BigDecimal(value).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble + " Peso normal"
      case value if (value >= 25 && value <= 29.99)   => "IMC: " + BigDecimal(value).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble + " Acima do peso"
      case value if (value >= 30 && value <= 34.99)   => "IMC: " + BigDecimal(value).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble + " Obesidade I"
      case value if (value >= 35 && value <= 39.99)   => "IMC: " + BigDecimal(value).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble + " Obesidade II"
      case value if (value >= 40)                     => "IMC: " + BigDecimal(value).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble + " Obesidade |||"
      case _ => EMPTY
    }
  }*/



}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright © 2019-2022 Tensil AI Company */

package tensil.tools

import java.io._
import scala.reflect.ClassTag
import tensil.tools.golden.{Processor, ExecutiveTraceContext}
import scala.collection.mutable
import scala.io.Source
import tensil.ArchitectureDataType

object TinyYolo {
  val GoldenOutputFileNames = Map(
    "model/conv2d_17/BiasAdd" -> "./models/data/yolov4_tiny_416_conv17.csv",
    "model/conv2d_20/BiasAdd" -> "./models/data/yolov4_tiny_416_conv20.csv"
  )

  def assertOutput(
      outputName: String,
      dataType: ArchitectureDataType,
      arraySize: Int,
      bytes: Array[Byte],
  ): Unit = {
    val rmse   = new RMSE(printValues = false)
    val source = Source.fromFile(GoldenOutputFileNames(outputName))
    val output =
      new DataInputStream(new ByteArrayInputStream(bytes))

    for (line <- source.getLines()) {
      val goldenPixel = line.split(",").map(_.toFloat)
      val pixel =
        Util.readResult(dataType, output, arraySize, goldenPixel.size)

      for (i <- 0 until goldenPixel.size)
        rmse.addSample(pixel(i), goldenPixel(i))
    }

    source.close()
    assert(rmse.compute < 0.6)
  }

  def prepareInputStream(
      dataType: ArchitectureDataType,
      arraySize: Int,
      count: Int
  ): InputStream = {
    val fileName = s"./models/data/yolov4_tiny_${count}x416x416x${arraySize}.csv"

    val inputPrep           = new ByteArrayOutputStream()
    val inputPrepDataStream = new DataOutputStream(inputPrep)

    Util.writeCsv(
      dataType,
      inputPrepDataStream,
      arraySize,
      fileName
    )

    new ByteArrayInputStream(inputPrep.toByteArray())
  }
}

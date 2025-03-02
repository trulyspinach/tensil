/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright © 2019-2022 Tensil AI Company */

package tensil.tools

import tensil.Architecture

case class TracepointCondition(
    tag: Int,
    prefix: String,
)

case class CompilerOptions(
    arch: Architecture,
    inputBatchSize: Int = 1,
    printSummary: Boolean = false,
    printLayersSummary: Boolean = false,
    printSchedulerSummary: Boolean = false,
    printProgress: Boolean = true,
    printProgramWithComments: Boolean = false,
    printProgramFileName: Option[String] = None,
    printGraphFileName: Option[String] = None,
    tracepointConditions: Seq[TracepointCondition] = Nil,
    collectBackendStats: Boolean = false
)

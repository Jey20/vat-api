/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.vatapi.models.des

import play.api.libs.json.{Format, Json, Reads}
import uk.gov.hmrc.vatapi.models.EnumJson
import uk.gov.hmrc.vatapi.models.des.DesErrorCode.DesErrorCode

case class DesError(code: DesErrorCode, reason: String)

object DesError {
  implicit val reads: Reads[DesError] = Json.reads[DesError]
}

object DesErrorCode extends Enumeration {
  type DesErrorCode = Value

  val INVALID_VRN,
  INVALID_ARN,
  INVALID_PAYLOAD,
  INVALID_PERIODKEY,
  DUPLICATE_SUBMISSION,
  DATE_RANGE_TOO_LARGE,
  SERVER_ERROR,
  SERVICE_UNAVAILABLE = Value

  implicit val format: Format[DesErrorCode] = EnumJson.enumFormat(DesErrorCode,
    Some(s"Recognized DesErrorCode values: ${DesErrorCode.values.mkString(", ")}"))
}

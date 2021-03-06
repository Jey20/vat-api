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

package uk.gov.hmrc.vatapi.models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import uk.gov.hmrc.vatapi.models.Validation._
import uk.gov.hmrc.vatapi.models.des
import org.joda.time.DateTime

case class VatReturnDeclaration(
  periodKey: String,
  vatDueSales: Amount,
  vatDueAcquisitions: Amount,
  totalVatDue: Amount,
  vatReclaimedCurrPeriod: Amount,
  netVatDue: Amount,
  totalValueSalesExVAT: Amount,
  totalValuePurchasesExVAT: Amount,
  totalValueGoodsSuppliedExVAT: Amount,
  totalAcquisitionsExVAT: Amount,
  finalised: Boolean
) {

  def toDes: des.VatReturnDeclaration =
    des.VatReturnDeclaration(
      periodKey = periodKey,
      vatDueSales = vatDueSales,
      vatDueAcquisitions = vatDueAcquisitions,
      vatDueTotal = totalVatDue,
      vatReclaimedCurrPeriod = vatReclaimedCurrPeriod,
      vatDueNet = netVatDue,
      totalValueSalesExVAT = totalValueSalesExVAT,
      totalValuePurchasesExVAT = totalValuePurchasesExVAT,
      totalValueGoodsSuppliedExVAT = totalValueGoodsSuppliedExVAT,
      totalAllAcquisitionsExVAT = totalAcquisitionsExVAT,
      receivedAt = new DateTime()
    )

}

object VatReturnDeclaration {

  implicit val writes: OWrites[VatReturnDeclaration] = Json.writes[VatReturnDeclaration]

  private val periodKeyValidator: Reads[String] = Reads
    .of[String]
    .filter(JsonValidationError("period key should be a 4 character string",
                                ErrorCode.INVALID_PERIOD_KEY))(_.length == 4)

  implicit val reads: Reads[VatReturnDeclaration] = (
    (__ \ "periodKey").read[String](periodKeyValidator) and
      (__ \ "vatDueSales").read[Amount](vatAmountValidator) and
      (__ \ "vatDueAcquisitions").read[Amount](vatAmountValidator) and
      (__ \ "totalVatDue").read[Amount](vatAmountValidator) and
      (__ \ "vatReclaimedCurrPeriod").read[Amount](vatAmountValidator) and
      (__ \ "netVatDue").read[Amount](vatNonNegativeAmountValidator) and
      (__ \ "totalValueSalesExVAT").read[Amount](vatWholeAmountValidator) and
      (__ \ "totalValuePurchasesExVAT")
        .read[Amount](vatWholeAmountValidator) and
      (__ \ "totalValueGoodsSuppliedExVAT")
        .read[Amount](vatWholeAmountValidator) and
      (__ \ "totalAcquisitionsExVAT").read[Amount](vatWholeAmountValidator) and
      (__ \ "finalised").read[Boolean]
  )(VatReturnDeclaration.apply _)
    .validate(
      Seq(
        Validation[VatReturnDeclaration](
          JsPath \ "totalVatDue",
          vatReturn =>
            vatReturn.totalVatDue == vatReturn.vatDueSales + vatReturn.vatDueAcquisitions,
          JsonValidationError(
            "totalVatDue should be equal to vatDueSales + vatDueAcquisitions",
            ErrorCode.VAT_TOTAL_VALUE)
        ),
        Validation[VatReturnDeclaration](
          JsPath \ "vatDueSales",
          vatReturn =>
            vatReturn.vatDueSales <= vatReturn.totalValueSalesExVAT * 0.2,
          JsonValidationError(
            "vatDueSales should not be greater than 20% of totalValueSalesExVAT",
            ErrorCode.VAT_DUE_SALES_LIMIT)
        ),
        Validation[VatReturnDeclaration](
          JsPath \ "vatReclaimedCurrPeriod",
          vatReturn =>
            vatReturn.vatReclaimedCurrPeriod <= vatReturn.totalValuePurchasesExVAT * 0.2,
          JsonValidationError(
            "vatReclaimedCurrPeriod should not be greater than 20% of totalValuePurchasesExVAT",
            ErrorCode.VAT_RECLAIMED_CURR_PERIOD_LIMIT)
        ),
        Validation[VatReturnDeclaration](
          JsPath \ "netVatDue",
          vatReturn =>
            vatReturn.netVatDue == (vatReturn.totalVatDue - vatReturn.vatReclaimedCurrPeriod).abs,
          JsonValidationError(
            "netVatDue should be the difference between the largest and the smallest values among totalVatDue and vatReclaimedCurrPeriod",
            ErrorCode.VAT_NET_VALUE)
        )
      )
    )

}

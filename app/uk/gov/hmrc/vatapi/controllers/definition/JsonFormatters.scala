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

package uk.gov.hmrc.vatapi.controllers.definition

import play.api.libs.json._

object JsonFormatters {

  implicit val formatAPIStatus = EnumJson.enumFormat(APIStatus)
  implicit val formatAuthType = EnumJson.enumFormat(AuthType)
  implicit val formatHttpMethod = EnumJson.enumFormat(HttpMethod)
  implicit val formatResourceThrottlingTier = EnumJson.enumFormat(ResourceThrottlingTier)
  implicit val formatGroupName = EnumJson.enumFormat(GroupName)

  implicit val formatParameter = Json.format[Parameter]
  implicit val formatEndpoint = Json.format[Endpoint]
  implicit val formatAccess = Json.format[Access]
  implicit val formatAPIVersion = Json.format[APIVersion]
  implicit val formatAPIDefinition = Json.format[APIDefinition]
  implicit val formatScope = Json.format[Scope]
  implicit val formatDefinition = Json.format[Definition]

}

class wikidata {

  def getWikidataId(link: String): String = {
    val url = "https://en.wikipedia.org/w/api.php"
    val title: String = link.substring(link.indexOf("wiki/") + 5)
    val params = Map(
      "action" -> "query",
      "prop" -> "pageprops",
      "format" -> "json",
      "titles" -> title
    )

    val data = requests.get(url=url, params=params)
    val json: JsValue = Json.parse(data.text)

    (json \\ "wikibase_item").head.as[String]

  }

  def wikidataIdDefinition(id: String): String = {
    val url = "https://www.wikidata.org/w/api.php"
    val params = Map(
      "action" -> "wbgetentities",
      "languages" -> "en",
      "format" -> "json",
      "ids" -> id
    )

    val data = requests.get(url=url, params=params)
    val json: JsValue = Json.parse(data.text)

    (json \ "entities" \ id \ "labels" \ "en" \ "value").as[String]
  }

  def getWikidataSummary(id: String): Map[String, Seq[String]] = {
    val url = "https://www.wikidata.org/w/api.php"
    val params = Map(
      "action" -> "wbgetentities",
      "languages" -> "en",
      "format" -> "json",
      "ids" -> id
    )

    val data = requests.get(url=url, params=params)
    val json: JsValue = Json.parse(data.text)

    val name: String = (json \ "entities" \ id \ "labels" \ "en" \ "value").as[String]
    val description: String = (json \ "entities" \ id \ "descriptions" \ "en" \ "value").as[String]

    val sexOrGender: Seq[String] =
      for (x <- (json \ "entities" \ id \ "claims" \ "P21").as[Seq[JsValue]])
        yield wikidataIdDefinition((x \ "mainsnak" \ "datavalue" \ "value" \ "id").as[String])

    val countryOfCitizenship: Seq[String] =
      for (x <- (json \ "entities" \ id \ "claims" \ "P27").as[Seq[JsValue]])
        yield wikidataIdDefinition((x \ "mainsnak" \ "datavalue" \ "value" \ "id").as[String])

    val educatedAt: Seq[String] =
      for (x <- (json \ "entities" \ id \ "claims" \ "P69").as[Seq[JsValue]])
        yield wikidataIdDefinition((x \ "mainsnak" \ "datavalue" \ "value" \ "id").as[String])

    val dateOfBirth: Seq[String] =
      for (x <- (json \ "entities" \ id \ "claims" \ "P569").as[Seq[JsValue]])
        yield (x \ "mainsnak" \ "datavalue" \ "value" \ "time").as[String]


    val allClaims: Map[String, Seq[String]] = Map(
      "Name" -> Seq(name),
      "Description" -> Seq(description),
      "Sex or gender" -> sexOrGender,
      "Date of birth" -> dateOfBirth,
      "Country/ies of citizenship" -> countryOfCitizenship,
      "Educated at" -> educatedAt
    )

    allClaims.foreach(x => println(x._1 + ": " + x._2.mkString(", ")))
    allClaims
  }
}

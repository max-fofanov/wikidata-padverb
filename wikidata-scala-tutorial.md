#  Easy wikidata access with Scala

## General overview
As for now, Wikidata-Scal

### Getting a Wikidata ID
You can use getWikidataId(link: String) method to get a wikidata ID from a wikipedia link, if there's one, e.g.
```scala
  getWikidataId("https://en.wikipedia.org/wiki/Elon_Musk")
  // returns "Q317521"
  getWikidataId("https://en.wikipedia.org/wiki/Max_Levchin")
  // returns "Q93014"
  getWikidataId("https://en.wikipedia.org/wiki/something_stupid")
  // returns error
```
### Getting a property standing behind Wikidata ID
One can get a definition of a wikidata ID, if passes it to getWikidataIdDefinition(id: String), have a look:
```scala
  getWikidataIdDefinition("Q317521")
  // returns "Elon Musk"
  getWikidataIdDefinition("P361")
  // returns "part of"
  getWikidataIdDefinition("Qnotnid)
  // returns error
```

### Getting someone's wikidata "card"
Finally, the most important of the three, getWikidataSummary(link: String) - a method which returns the most valuable data chosen by You (although right now it's hardcoded).
Here's an example:
```scala
  getWikidataSummary("https://en.wikipedia.org/wiki/Elon_Musk")
  /* returns (both prints into the console and returns as a Map[String, Seq[String]]
  Name: Elon Musk
  Description: South African–born American entrepreneur
  Educated at: Smith School of Business, The Wharton School, University of Pennsylvania, Pretoria Boys High School, Waterkloof House Preparatory School, Queen's University, Stanford University
  Country/ies of citizenship: South Africa, Canada, United States of America
  Sex or gender: male
  Date of birth: +1971-06-28T00:00:00Z
  */
```

### Plans for the future
In the nearest future I'm planning to make data, which is being returned by getWikidataSummary user-oriented and work on exception handling.

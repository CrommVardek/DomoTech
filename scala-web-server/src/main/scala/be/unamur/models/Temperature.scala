package be.unamur.models

import scala.util.parsing.json.JSONObject

/**
  * Created by Axel on 16-04-16.
  */
case class Temperature(value:Double){
  def toJson = JSONObject.apply(Map(("value",value)))
}

object TemperatureData {
  val all = List{Temperature(50)}
}

package be.unamur.models

import scala.util.parsing.json.JSONObject

/**
  * Created by Axel on 16-04-16.
  */
case class Luminosity(value:Integer){
  def toJson = JSONObject.apply(Map(("value",value)))
}



object LuminosityData {
  val all = Luminosity(2)
}
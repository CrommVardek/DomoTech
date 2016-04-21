package be.unamur.models

import scala.util.parsing.json.JSONObject

/**
  * Created by Axel on 18-04-16.
  */
case class Spot(number:Integer) {
  def toJson = JSONObject.apply(Map(("value",number)))
}


object SpotData{
  val all = Spot(6)
}
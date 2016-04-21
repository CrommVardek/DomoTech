package be.unamur.models

import scala.util.parsing.json.JSONObject

/**
  * Created by Axel on 16-04-16.
  */
case class Spice(name:String, description:String) {
  def toJson = JSONObject.apply(Map(("name",name),("description",description)))
}


object SpiceData{
  val all = List(
                  Spice("Poivre","Épice originaire des côtes de Malabar"),
                  Spice("Paprika","Épice de couleur rouge obtenu à partir du piment doux ou poivron"),
                  Spice("Muscade","Épice utilisée pour accomoder les viandes, soupes, purées de légumes et certains cocktails.")
                )
}

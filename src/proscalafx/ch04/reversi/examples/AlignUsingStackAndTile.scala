package proscalafx.ch04.reversi.examples

import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.StackPane
import scalafx.scene.layout.TilePane
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import scalafx.scene.text.Text
import scalafx.stage.Stage

object AlignUsingStackAndTile extends JFXApp {
  val left = new StackPane {
    style = "-fx-background-color: black"
    content = new Text {
      text = "ScalaFX"
      font = Font.font(null, FontWeight.BOLD, 18)
      fill = Color.WHITE
      alignment = Pos.CENTER_RIGHT
    }
  }

  stage = new Stage {
    scene = new Scene(400, 100) {
      content = new TilePane {
        snapToPixel = false
        content = List(left, new Text {
          text = "Reversi"
          font = Font.font(null, FontWeight.BOLD, 18)
          alignment = Pos.CENTER_LEFT
        })
      }
    }
  }

  left.prefWidth <== stage.scene.width / 2
  left.prefHeight <== stage.scene.height

}
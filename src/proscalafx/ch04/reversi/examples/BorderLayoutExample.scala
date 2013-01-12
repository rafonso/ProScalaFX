package proscalafx.ch04.reversi.examples

import proscalafx.ch04.reversi.model.BLACK
import proscalafx.ch04.reversi.model.Owner
import proscalafx.ch04.reversi.model.ReversiModel
import proscalafx.ch04.reversi.model.WHITE
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.geometry.Pos
import scalafx.scene.Node
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.effect.InnerShadow
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.FlowPane
import scalafx.scene.layout.Region
import scalafx.scene.layout.StackPane
import scalafx.scene.layout.TilePane
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.shape.Ellipse
import scalafx.scene.text.{FontWeight, Font, Text}
import scalafx.stage.Stage


object BorderLayoutExample extends JFXApp {

  val borderPane = new BorderPane {
    top = createTitle
    center = createBackground
    bottom = createScoreBoxes
  }

  stage = new Stage {
    scene = new Scene(600, 400) {
      // NOTE: Assign borderPane directly to `root` to avoid layout issues.
      // If assigned to `content` there will be `Group` node at root that interferes with automatic rescaling.
      root = borderPane
    }
  }


  //---------------------------------------------------------------------------


  private def createTitle = new TilePane {
    snapToPixel = false
    content = List(
      new StackPane {
        style = "-fx-background-color: black"
        content = new Text("ScalaFX") {
          font = Font.font(null, FontWeight.BOLD, 18)
          fill = Color.WHITE
          alignment = Pos.CENTER_RIGHT
        }
      },
      new Text("Reversi") {
        font = Font.font(null, FontWeight.BOLD, 18)
        alignment = Pos.CENTER_LEFT
      })
    prefTileHeight = 40
    prefTileWidth <== parent.selectDouble("width") / 2
  }


  private def createBackground = new Region {
    style = "-fx-background-color: radial-gradient(radius 100%, white, gray)"
  }


  private def createScoreBoxes = new TilePane {
    snapToPixel = false
    prefColumns = 2
    content = List(
      createScore(BLACK),
      createScore(WHITE)
    )
    prefTileWidth <== parent.selectDouble("width") / 2
  }


  private def createScore(owner: Owner): Node = {

    val innerShadow = new InnerShadow() {
      color = Color.DODGERBLUE
      choke = 0.5
    }
    val background = new Region {
      style = "-fx-background-color: " + owner.opposite.colorStyle
      if (BLACK == owner) {
        effect = innerShadow
      }
    }

    val dropShadow = new DropShadow() {
      color = Color.DODGERBLUE
      spread = 0.2
    }

    val piece = new Ellipse {
      radiusX = 32
      radiusY = 20
      fill = owner.color
      if (BLACK == owner) {
        effect = dropShadow
      }
    }

    val score = new Text {
      font = Font.font(null, FontWeight.BOLD, 100)
      fill = owner.color
      text <== ReversiModel.score(owner).asString
    }

    val remaining = new Text {
      font = Font.font(null, FontWeight.BOLD, 12)
      fill = owner.color
      text <== ReversiModel.turnsRemaining(owner).asString() + " turns remaining"
    }

    new StackPane {
      content = List(
        background,
        new FlowPane {
          hgap = 20
          vgap = 10
          innerAlignment = Pos.CENTER
          content = List(
            score,
            new VBox {
              innerAlignment = Pos.CENTER
              spacing = 10
              content = List(
                piece,
                remaining)
            }
          )
        }
      )
    }
  }

}
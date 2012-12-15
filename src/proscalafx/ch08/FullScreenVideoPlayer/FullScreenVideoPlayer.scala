package proscalafx.ch08.FullScreenVideoPlayer

import java.io.File
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.StackPane
import scalafx.scene.media.{Media, MediaPlayer, MediaView}
import scalafx.stage.Stage


/**
 * @author Jarek Sacha
 */
object FullScreenVideoPlayer extends JFXApp {

  val file = new File("media/omgrobots.flv")
  val media = new Media(file.toURI.toString)
  val mediaPlayer = new MediaPlayer(media)
  val mediaView = new MediaView(mediaPlayer) {
    fitWidth <== scene.selectDouble("width")
    fitHeight <== scene.selectDouble("height")
    preserveRatio = true
  }

  val root = new StackPane {content = mediaView}

  stage = new Stage {
    title = "Video Player 1"
    fullScreen = true
    scene = new Scene(root, 960, 540)
  }

  mediaPlayer.play()
}

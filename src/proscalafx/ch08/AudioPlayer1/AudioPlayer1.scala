package proscalafx.ch08.AudioPlayer1

import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.media.{MediaPlayer, Media}
import scalafx.stage.Stage


/**
 * A very simple application that plays an audio file.
 *
 * @author Jarek Sacha 
 */
object AudioPlayer1 extends JFXApp {
  val resource = getClass.getResource("resources/keeper.mp3")
  val media = new Media(resource.toString)
  val mediaPlayer = new MediaPlayer(media)
  mediaPlayer.play()

  stage = new Stage {
    title = "Audio Player 1"
    scene = new Scene(200, 200)
  }
}

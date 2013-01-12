package proscalafx.ch08.AudioPlayer4

import javafx.scene.{media => jffxsm}
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.event.ActionEvent
import scalafx.event.subscriptions.Subscription
import scalafx.geometry.{HPos, Pos, VPos, Insets}
import scalafx.scene.Node
import scalafx.scene.control.{Button, Label, Slider}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{Priority, ColumnConstraints, GridPane, HBox}
import scalafx.scene.media.MediaPlayer
import scalafx.stage.FileChooser
import scalafx.util.Duration
import scalafx.scene.media.MediaPlayer.Status


/**
 * @author Jarek Sacha 
 */
class PlayerControlsView(songModel: SongModel) extends AbstractView[GridPane](songModel) {
  private var pauseImg: Image = _
  private var playImg: Image = _
  private var playPauseIcon: ImageView = _
  // ScalaFX uses `subscription` to keep track of assigned listeners
  private var statusInvalidationSubscription: Subscription = _
  private var currentTimeSubscription: Subscription = _
  private var controlPanel: Node = _
  private var statusLabel: Label = _
  private var currentTimeLabel: Label = _
  private var totalDurationLabel: Label = _
  private var volumeSlider: Slider = _
  private var positionSlider: Slider = _
  private var eqButton: Button = _

  songModel.mediaPlayer.onChange {
    (_, oldValue, newValue) =>
      if (oldValue != null) removeListenersAndBinding(oldValue)
      addListenersAndBindings(newValue)
  }

  addListenersAndBindings(songModel.mediaPlayer())


  protected def initView(): GridPane = {
    val openButton = createOpenButton()
    eqButton = createEQButton()
    controlPanel = createControlPanel()
    volumeSlider = createSlider("volumeSlider")
    statusLabel = createLabel("Buffering", "statusDisplay")
    positionSlider = createSlider("positionSlider")
    totalDurationLabel = createLabel("00:00", "mediaText")
    currentTimeLabel = createLabel("00:00", "mediaText")

    positionSlider.valueChanging.onChange {
      (_, oldValue, newValue) =>
        if (oldValue && !newValue) {
          val pos = positionSlider.value()
          val mediaPlayer = songModel.mediaPlayer()
          val seekTo = mediaPlayer.totalDuration() * pos
          seekAndUpdatePosition(seekTo)
        }
    }

    val volLow = new ImageView {id = "volumeLow"}
    val volHigh = new ImageView {id = "volumeHigh"}

    val buttonCol = new ColumnConstraints(100)
    val spacerCol = new ColumnConstraints(40, 80, 80)
    val middleCol = new ColumnConstraints {hgrow = Priority.ALWAYS}

    val gp = new GridPane {
      hgap = 1
      vgap = 1
      padding = Insets(10)
      columnConstraints = List(buttonCol, spacerCol, middleCol, spacerCol, buttonCol)
    }

    GridPane.setValignment(openButton, VPos.BOTTOM)
    eqButton.alignment = Pos.BOTTOM_RIGHT
    GridPane.setHalignment(volHigh, HPos.RIGHT)
    GridPane.setValignment(volumeSlider, VPos.TOP)
    statusLabel.alignment = Pos.TOP_RIGHT
    GridPane.setHalignment(currentTimeLabel, HPos.RIGHT)

    gp.add(openButton, 0, 0, 1, 3)
    gp.add(volLow, 1, 0)
    gp.add(volHigh, 1, 0)
    gp.add(volumeSlider, 1, 1)
    gp.add(controlPanel, 2, 0, 1, 2)
    gp.add(statusLabel, 3, 1)
    gp.add(currentTimeLabel, 1, 2)
    gp.add(positionSlider, 2, 2)
    gp.add(totalDurationLabel, 3, 2)
    gp.add(eqButton, 4, 0, 1, 3)

    gp
  }

  override def onNextPageAction(nextHandler: (ActionEvent => Unit)) {
    eqButton.onAction = nextHandler
  }

  private def createOpenButton() = new Button {
    id = "openButton"
    onAction = (ae: ActionEvent) => {
      val fileChooser = new FileChooser() {
        title = "Pick a Sound File"
      }

      val song = fileChooser.showOpenDialog(viewNode.scene().window())
      if (song != null) {
        songModel.url = song.toURI.toString
        songModel.mediaPlayer().play()
      }
    }
    prefWidth = 48
    prefHeight = 32
  }

  private def createEQButton() = new Button("EQ") {
    id = "eqButton"
    prefWidth = 48
    prefHeight = 32
  }

  private def createControlPanel(): Node = {
    val playPauseButton = createPlayPauseButton()
    val seekStartButton = new Button {
      id = "seekStartButton"
      onAction = (ae: ActionEvent) => seekAndUpdatePosition(Duration.ZERO)
    }
    val seekEndButton = new Button {
      id = "seekEndButton"
      onAction = (ae: ActionEvent) => {
        val mediaPlayer = songModel.mediaPlayer()
        val totalDuration = mediaPlayer.totalDuration()
        seekAndUpdatePosition(totalDuration - (1 s))
      }
    }

    new HBox {
      innerAlignment = Pos.CENTER
      fillHeight = false
      content = List(seekStartButton, playPauseButton, seekEndButton)
    }
  }


  def createPlayPauseButton(): Button = {
    val pauseUrl = getClass.getResource("resources/pause.png")
    pauseImg = new Image(pauseUrl.toString)

    val playUrl = getClass.getResource("resources/play.png")
    playImg = new Image(playUrl.toString)

    playPauseIcon = new ImageView(playImg)

    new Button {
      graphic = playPauseIcon
      id = "playPauseButton"
      onAction = (ae: ActionEvent) => {
        val mediaPlayer = songModel.mediaPlayer()
        mediaPlayer.status() match {
          case Status.PLAYING.delegate => mediaPlayer.pause()
          case _ => mediaPlayer.play()
        }
      }
    }
  }


  def createSlider(sliderId: String) = new Slider {
    min = 0
    max = 1
    value = 0
    id = sliderId
  }

  private def createLabel(labelText: String, labelStyleClass: String) = new Label {
    text = labelText
    styleClass += labelStyleClass
  }


  private def updatePositionSlider(currentTime: Duration) {
    if (positionSlider.isValueChanging) return

    val mediaPlayer = songModel.mediaPlayer()
    val total = mediaPlayer.totalDuration()

    positionSlider.value = if (total == null || currentTime == null) {
      0d
    } else {
      currentTime.toMillis / total.toMillis
    }
  }


  private def seekAndUpdatePosition(duration: Duration) {
    val mediaPlayer = songModel.mediaPlayer()
    if (mediaPlayer.status == jffxsm.MediaPlayer.Status.STOPPED) mediaPlayer.pause()

    mediaPlayer.seek(duration)

    if (mediaPlayer.status != jffxsm.MediaPlayer.Status.PLAYING) updatePositionSlider(duration)
  }

  private def formatDuration(duration: Duration): String = {
    val millis = duration.toMillis
    val seconds = (millis / 1000).toInt % 60
    val minutes = (millis / (1000 * 60)).toInt
    "%02d:%02d".format(minutes, seconds)
  }

  def updateStatus(newStatus: jffxsm.MediaPlayer.Status) {
    if (newStatus == jffxsm.MediaPlayer.Status.UNKNOWN || newStatus == null) {
      controlPanel.disable = true
      positionSlider.disable = true
      statusLabel.text = "Buffering"
    } else {
      controlPanel.disable = false
      positionSlider.disable = false
      statusLabel.text = newStatus.toString
      playPauseIcon.image = if (newStatus == jffxsm.MediaPlayer.Status.PLAYING) pauseImg else playImg
    }
  }


  private def addListenersAndBindings(mp: MediaPlayer) {
    statusInvalidationSubscription = mp.status.onInvalidate {
      Platform.runLater {
        updateStatus(songModel.mediaPlayer().status())
      }
    }

    currentTimeSubscription = mp.currentTime.onInvalidate {
      Platform.runLater {
        val mediaPlayer = songModel.mediaPlayer()
        val currentTime = mediaPlayer.currentTime()
        currentTimeLabel.text = formatDuration(currentTime)
        updatePositionSlider(currentTime)
      }
    }

    mp.totalDuration.onInvalidate {
      val mediaPlayer = songModel.mediaPlayer()
      val totalDuration = mediaPlayer.totalDuration()
      totalDurationLabel.text = formatDuration(totalDuration)
    }

    mp.onEndOfMedia = runnable {songModel.mediaPlayer().stop()}

    volumeSlider.value <==> mp.volume
  }


  private def removeListenersAndBinding(mp: MediaPlayer) {
    volumeSlider.value.unbind()
    statusInvalidationSubscription.cancel()
    currentTimeSubscription.cancel()
  }


  private def runnable(op: => Unit) = new Runnable {
    def run() {
      op
    }
  }
}

package proscalafx.ch07

import javafx.scene.{chart => jfxsc}
import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.chart.PieChart
import scalafx.scene.layout.StackPane
import scalafx.stage.Stage


/**
 * @author Jarek Sacha
 */
object ChartApp1 extends JFXApp {

  stage = new Stage {
    title = "PieChart"
    scene = new Scene(400, 250) {
      root = new StackPane {
        content = new PieChart() {
          data = chartData()
        }
      }.delegate
    }
  }


  private def chartData() = ObservableBuffer[jfxsc.PieChart.Data](
    PieChart.Data("java", 17.56),
    PieChart.Data("C", 17.06),
    PieChart.Data("C++", 8.25),
    PieChart.Data("C#", 8.20),
    PieChart.Data("ObjectiveC", 6.8),
    PieChart.Data("PHP", 6.0),
    PieChart.Data("(Visual)Basic", 4.76),
    PieChart.Data("Other", 31.37)
  )
}

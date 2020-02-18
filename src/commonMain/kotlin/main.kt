import com.soywiz.klogger.Logger
import com.soywiz.korev.Key
import com.soywiz.korge.Korge
import com.soywiz.korge.input.onClick
import com.soywiz.korge.input.onKeyDown
import com.soywiz.korim.color.Colors
import j4k.candycrush.*
import j4k.candycrush.compontens.KorgeLogo
import j4k.candycrush.config.fruits
import j4k.candycrush.config.testTiles
import j4k.candycrush.model.GameField

val log = Logger("main")
const val useTestTiles = false

suspend fun main() = Korge(width = 1280, height = 1024, bgcolor = Colors["#2b2b2b"], debug = false) {

    Logger.defaultLevel = Logger.Level.DEBUG

    val fieldData = """
                        |[A,B,C,D,E,B,D,A]
                        |[B,B,C,A,D,D,B,C]
                        |[A,C,B,E,E,B,D,C]
                        |[D,E,E,B,A,E,C,A]
                        |[E,D,A,E,E,C,D,B]
                        |[E,A,D,C,B,A,A,E]
                        """.trimMargin()

    val gameField = GameField.fromString(fieldData)

    val gameMechanics = GameMechanics(gameField)


    val fieldRenderer = GameFieldRenderer(gameField, width.toInt(), height.toInt(), getTilesSheet())
    addChild(fieldRenderer)
    val animator = TileAnimator(this, fieldRenderer, fieldRenderer.positionGrid)

    val gameFlow = GameFlow(gameField, gameMechanics, animator)
    addComponent(MoveTileObserver(this, fieldRenderer.positionGrid, gameFlow))

    onClick { }

    onKeyDown {
        if (it.key == Key.P) {
            println("---------------")
            println(gameField)
        }
        if (it.key == Key.I) {
            println("---------------")
            println(fieldRenderer)
            println("Renderer data is equal to field data: " + (fieldRenderer.toString() == gameField.toString()))
        }
        if (it.key == Key.S) {
            gameField.shuffle()
            fieldRenderer.updateImagesFromField()
        }
    }

    KorgeLogo(this).addLogo()
}

suspend fun getTilesSheet() = if (useTestTiles) testTiles() else fruits()

package com.unal.reto6

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import kotlin.system.exitProcess

class EasyFragment() : Fragment(){

    lateinit var buttons: List<ImageButton>

    lateinit var button1 : ImageButton
    lateinit var button2 : ImageButton
    lateinit var button3 : ImageButton
    lateinit var button4 : ImageButton
    lateinit var button5 : ImageButton
    lateinit var button6 : ImageButton
    lateinit var button7 : ImageButton
    lateinit var button8 : ImageButton
    lateinit var button9 : ImageButton
    lateinit var botonRst : Button

    lateinit var textView : TextView
    lateinit var textView2 : TextView

    private lateinit var soundManager: SoundManager

    var playerTurn = true

    // Contador de jugadores
    var player1Count = 0
    var player2Count = 0

    //variables varias
    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()
    var emptyCells = ArrayList<Int>()
    var activeUser = 1

    companion object {
        private const val ARG_PLAYER1_COUNT = "player1Count"
        private const val ARG_PLAYER2_COUNT = "player2Count"
        private const val ARG_LIST_PLAYER1_MOVES = "player1Moves"
        private const val ARG_LIST_PLAYER2_MOVES = "player2Moves"

        fun newInstance(player1Count: Int, player2Count: Int, player1Moves: ArrayList<Int>, player2Moves: ArrayList<Int>): EasyFragment {
            val fragment = EasyFragment()
            val args = Bundle()
            args.putInt(ARG_PLAYER1_COUNT, player1Count)
            args.putInt(ARG_PLAYER2_COUNT, player2Count)
            args.putIntegerArrayList(ARG_LIST_PLAYER1_MOVES,player1Moves)
            args.putIntegerArrayList(ARG_LIST_PLAYER2_MOVES,player2Moves)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soundManager = SoundManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_easy,container,false)
        /*val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        val layoutResId = if (isLandscape) {
            R.layout.fragment_easy
        } else {
            R.layout.fragment_easy
        }
        val view=inflater.inflate(layoutResId,container,false)*/
        player1Count = arguments?.getInt(EasyFragment.ARG_PLAYER1_COUNT, 0) ?: 0
        player2Count = arguments?.getInt(EasyFragment.ARG_PLAYER2_COUNT, 0) ?: 0

        player1 = arguments?.getIntegerArrayList(EasyFragment.ARG_LIST_PLAYER1_MOVES) ?: ArrayList()
        player2 = arguments?.getIntegerArrayList(EasyFragment.ARG_LIST_PLAYER2_MOVES) ?: ArrayList()

        button1=view.findViewById<ImageButton>(R.id.button1)
        button2=view.findViewById<ImageButton>(R.id.button2)
        button3=view.findViewById<ImageButton>(R.id.button3)
        button4=view.findViewById<ImageButton>(R.id.button4)
        button5=view.findViewById<ImageButton>(R.id.button5)
        button6=view.findViewById<ImageButton>(R.id.button6)
        button7=view.findViewById<ImageButton>(R.id.button7)
        button8=view.findViewById<ImageButton>(R.id.button8)
        button9=view.findViewById<ImageButton>(R.id.button9)
        botonRst=view.findViewById<Button>(R.id.button10)
        textView=view.findViewById<TextView>(R.id.textView1)
        textView2=view.findViewById<TextView>(R.id.textView2)
        textView.text = "Jugador 1: "+player1Count.toString()
        textView2.text = "Teléfono: "+player2Count.toString()


        button1.setOnClickListener {
            clickfun(it)
        }
        button2.setOnClickListener {
            clickfun(it)
        }
        button3.setOnClickListener {
            clickfun(it)
        }
        button4.setOnClickListener {
            clickfun(it)
        }
        button5.setOnClickListener {
            clickfun(it)
        }
        button6.setOnClickListener {
            clickfun(it)
        }
        button7.setOnClickListener {
            clickfun(it)
        }
        button8.setOnClickListener {
            clickfun(it)
        }
        button9.setOnClickListener {
            clickfun(it)
        }

        botonRst.setOnClickListener {
            reset()
        }
        buttons = listOf(
            view.findViewById(R.id.button1),
            view.findViewById(R.id.button2),
            view.findViewById(R.id.button3),
            view.findViewById(R.id.button4),
            view.findViewById(R.id.button5),
            view.findViewById(R.id.button6),
            view.findViewById(R.id.button7),
            view.findViewById(R.id.button8),
            view.findViewById(R.id.button9)
        )

        if (player1.size!=0){
            restoreGamePlayer1(player1)
            emptyCells.addAll(player1)
        }

        if (player2.size!=0){
            restoreGamePlayer2(player2)
            emptyCells.addAll(player2)
        }

        return view
    }

    fun clickfun(view: View)
    {
        if(playerTurn) {
            val but = view as ImageButton
            var cellID = 0
            when (but.id) {
                R.id.button1 -> cellID = 1
                R.id.button2 -> cellID = 2
                R.id.button3 -> cellID = 3
                R.id.button4 -> cellID = 4
                R.id.button5 -> cellID = 5
                R.id.button6 -> cellID = 6
                R.id.button7 -> cellID = 7
                R.id.button8 -> cellID = 8
                R.id.button9 -> cellID = 9
            }
            playerTurn = false;
            Handler().postDelayed(Runnable { playerTurn = true } , 600)
            playnow(but, cellID)
        }
    }

    // Función que actualiza el tablero luego de cada movimiento
    fun playnow(buttonSelected:ImageButton , currCell:Int)
    {
        if(activeUser == 1)
        {
            buttonSelected.setImageResource(R.drawable.checkbox_cross_orange_icon)
            player1.add(currCell)
            emptyCells.add(currCell)
            buttonSelected.isEnabled = false
            soundManager.playPlayerSound()
            val checkWinner = checkwinner()
            if(checkWinner == 1){
                Handler().postDelayed(Runnable { reset() } , 2000)
            }
            else
                Handler().postDelayed(Runnable { robot() } , 500)
        }
        else
        {
            buttonSelected.setImageResource(R.drawable.starfilledminor_svgrepo_com)
            activeUser = 1
            player2.add(currCell)
            emptyCells.add(currCell)
            buttonSelected.isEnabled = false
            val checkWinner  = checkwinner()
            if(checkWinner == 1)
                Handler().postDelayed(Runnable { reset() } , 4000)
        }
    }

    //Fucnión que revisa si hay un ganador
    fun checkwinner(): Int {
        if ((player1.contains(1) && player1.contains(2) && player1.contains(3)) || (player1.contains(
                1
            ) && player1.contains(4) && player1.contains(7)) ||
            (player1.contains(3) && player1.contains(6) && player1.contains(9)) || (player1.contains(
                7
            ) && player1.contains(8) && player1.contains(9)) ||
            (player1.contains(4) && player1.contains(5) && player1.contains(6)) || (player1.contains(
                1
            ) && player1.contains(5) && player1.contains(9)) ||
            player1.contains(3) && player1.contains(5) && player1.contains(7) || (player1.contains(2) && player1.contains(
                5
            ) && player1.contains(8))
        ) {
            player1Count += 1
            buttonDisable()
            disableReset()
            soundManager.playWinSound()
            val build = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            build.setTitle("Ganador!")
            build.setMessage("Has ganado el juego.."+"\n\n"+"Deseas continuar jugando?")
            build.setPositiveButton("Si") { dialog, which ->
                reset()
            }
            build.setNegativeButton("No") { dialog, which ->
                exitProcess(1)

            }
            Handler().postDelayed(Runnable { build.show() }, 2000)
            return 1


        } else if ((player2.contains(1) && player2.contains(2) && player2.contains(3)) || (player2.contains(
                1
            ) && player2.contains(4) && player2.contains(7)) ||
            (player2.contains(3) && player2.contains(6) && player2.contains(9)) || (player2.contains(
                7
            ) && player2.contains(8) && player2.contains(9)) ||
            (player2.contains(4) && player2.contains(5) && player2.contains(6)) || (player2.contains(
                1
            ) && player2.contains(5) && player2.contains(9)) ||
            player2.contains(3) && player2.contains(5) && player2.contains(7) || (player2.contains(2) && player2.contains(
                5
            ) && player2.contains(8))
        ) {
            player2Count += 1
            buttonDisable()
            disableReset()
            soundManager.playLooseSound()
            val build = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            build.setTitle("Perdiste!")
            build.setMessage("El teléfono ha ganado!"+"\n\n"+"Deseas continuar jugando?")
            build.setPositiveButton("Si") { dialog, which ->
                reset()
            }
            build.setNegativeButton("No") { dialog, which ->
                exitProcess(1)
            }
            Handler().postDelayed(Runnable { build.show() }, 2000)
            return 1
        } else if (emptyCells.contains(1) && emptyCells.contains(2) && emptyCells.contains(3) && emptyCells.contains(
                4
            ) && emptyCells.contains(5) && emptyCells.contains(6) && emptyCells.contains(7) &&
            emptyCells.contains(8) && emptyCells.contains(9)
        ) {

            val build = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            build.setTitle("Empate!")
            build.setMessage("Nadie ha ganado" + "\n\n" + "Deseas jugar de nuevo?")
            build.setPositiveButton("Si") { dialog, which ->
                reset()
            }
            build.setNegativeButton("No") { dialog, which ->
                exitProcess(1)
            }
            build.show()
            return 1

        }
        return 0
    }

    // this function resets the game.
    fun reset()
    {
        player1.clear()
        player2.clear()
        emptyCells.clear()
        activeUser = 1;
        for(i in 1..9)
        {
            var buttonselected : ImageButton?
            buttonselected = when(i){
                1 -> button1
                2 -> button2
                3 -> button3
                4 -> button4
                5 -> button5
                6 -> button6
                7 -> button7
                8 -> button8
                9 -> button9
                else -> {button1}
            }
            buttonselected.isEnabled = true
            buttonselected.setImageResource(android.R.color.transparent)
            textView.text = "Jugador1 : $player1Count"
            textView2.text = "Teléfono : $player2Count"
        }
    }

    fun robot()
    {
        val rnd = (1..9).random()
        if(emptyCells.contains(rnd))
            robot()
        else {
            val buttonselected : ImageButton?
            buttonselected = when(rnd) {
                1 -> button1
                2 -> button2
                3 -> button3
                4 -> button4
                5 -> button5
                6 -> button6
                7 -> button7
                8 -> button8
                9 -> button9
                else -> {button1}
            }
            emptyCells.add(rnd);
            buttonselected.setImageResource(R.drawable.starfilledminor_svgrepo_com)
            player2.add(rnd)
            buttonselected.isEnabled = false
            soundManager.playRobotSound()
            var checkWinner = checkwinner()
            if(checkWinner == 1)
                Handler().postDelayed(Runnable { reset() } , 2000)
        }
    }

    fun buttonDisable() {
        for (i in 1..9) {
            val buttonSelected = when (i) {
                1 -> button1
                2 -> button2
                3 -> button3
                4 -> button4
                5 -> button5
                6 -> button6
                7 -> button7
                8 -> button8
                9 -> button9
                else -> {
                    button1
                }

            }
            if (buttonSelected.isEnabled == true)
                buttonSelected.isEnabled = false
        }
    }

    fun disableReset() {
        botonRst.isEnabled = false
        Handler().postDelayed(Runnable { botonRst.isEnabled = true }, 2200)
    }

    override fun onPause() {
        super.onPause()
        soundManager.releaseMediaPlayer()
    }

    fun restoreGamePlayer1(movePlayer1List:ArrayList<Int>){
        val imageButton : ImageButton
        for (i in movePlayer1List) {
            val imageButton = buttons[i - 1]
            imageButton.setImageResource(R.drawable.checkbox_cross_orange_icon)
            imageButton.isEnabled = false
        }
    }

    fun restoreGamePlayer2(movePlayer2List:ArrayList<Int>){
        val imageButton : ImageButton
        for (i in movePlayer2List) {
            val imageButton = buttons[i - 1]
            imageButton.setImageResource(R.drawable.starfilledminor_svgrepo_com)
            imageButton.isEnabled = false
        }
    }

}
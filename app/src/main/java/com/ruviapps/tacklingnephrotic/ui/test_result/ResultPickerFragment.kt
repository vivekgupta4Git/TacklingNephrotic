package com.ruviapps.tacklingnephrotic.ui.test_result

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ruviapps.tacklingnephrotic.BuildConfig
import com.ruviapps.tacklingnephrotic.database.entities.ResultCode
import com.ruviapps.tacklingnephrotic.databinding.ReadingSliderBinding
import com.ruviapps.tacklingnephrotic.domain.TestResult
import com.ruviapps.tacklingnephrotic.utility.NavigationCommand
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.util.*


@AndroidEntryPoint
class ResultPickerFragment : BaseFragment() {
    override var bottomAppBarVisibility: Int = View.GONE
    override var fabVisibility: Int = View.GONE
    companion object {
        const val NO_SELECTION = -100
    }
    private var selectedReadingValue = NO_SELECTION
    private var latestImageUri : Uri? = null
    private lateinit var scrollView : ScrollView

    private lateinit var firstCardPreviewImage : ImageView
    private lateinit var secondCardPreviewImage : ImageView
    private lateinit var thirdCardPreviewImage : ImageView
    private lateinit var forthCardPreviewImage : ImageView
    private lateinit var fifthCardPreviewImage : ImageView
    private lateinit var sixthCardPreviewImage : ImageView

    private lateinit var readingContainer: CardView
    private lateinit var firstCardView : CardView
    private lateinit var secondCardView : CardView
    private lateinit var thirdCardView : CardView
    private lateinit var forthCardView : CardView
    private lateinit var fifthCardView : CardView
    private lateinit var sixthCardView : CardView
    private   lateinit var sliderContainer : CardView


    private val takePictureContract = registerForActivityResult(ActivityResultContracts.TakePicture()) { taken ->
        if (taken) {

            latestImageUri.let { uri->
                imageCollection.forEach {
                    it.visibility = View.VISIBLE
                    it.setImageURI(uri)
                }
            }
        }else{
            imageCollection.forEach {
                it.visibility = View.GONE
            }
        }
    }
    /*    latestImageUri.let {
            firstCardPreviewImage.visibility = View.VISIBLE
            secondCardPreviewImage.visibility = View.VISIBLE
            thirdCardPreviewImage.visibility = View.VISIBLE
            forthCardPreviewImage.visibility = View.VISIBLE
            fifthCardPreviewImage.visibility = View.VISIBLE
            sixthCardPreviewImage.visibility = View.VISIBLE
            firstCardPreviewImage.setImageURI(it)
            secondCardPreviewImage.setImageURI(it)
            thirdCardPreviewImage.setImageURI(it)
            forthCardPreviewImage.setImageURI(it)
            fifthCardPreviewImage.setImageURI(it)
            sixthCardPreviewImage.setImageURI(it)

        }
    }else
    {
        firstCardPreviewImage.visibility  = View.GONE
        secondCardPreviewImage.visibility = View.GONE
        thirdCardPreviewImage.visibility = View.GONE
        forthCardPreviewImage.visibility = View.GONE
        fifthCardPreviewImage.visibility = View.GONE
        sixthCardPreviewImage.visibility = View.GONE
    }
}

     */
    val viewModel: ResultPickerViewModel by viewModels()
    private lateinit var binding: ReadingSliderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ReadingSliderBinding.inflate(layoutInflater)

    //    viewModel.initializeDatabase()
        return binding.root
    }







    private fun clearSelection() = setCardActive(-1)
    private var imageCollection  = mutableListOf<ImageView>()
    private  var cardCollection : List<CardView> = mutableListOf()
    private fun initializedBinding(){
       scrollView = binding.scrollView

        firstCardView = binding.firstCard
        secondCardView = binding.secondCard
        thirdCardView = binding.thirdCard
        forthCardView = binding.fourthCard
        fifthCardView = binding.fifthCard
        sixthCardView =binding.SixthCard
        readingContainer = binding.readingsContainer
        sliderContainer = binding.sliderContainer

        firstCardPreviewImage = binding.firsCardUserImage
        secondCardPreviewImage = binding.secondCardUserImage
        thirdCardPreviewImage = binding.thirdCardUserImage
        forthCardPreviewImage = binding.fourthCardUserImage
        fifthCardPreviewImage = binding.fifthCardUserImage
        sixthCardPreviewImage = binding.SixthCardUserImage

        imageCollection.add(firstCardPreviewImage)
        imageCollection.add(secondCardPreviewImage)
        imageCollection.add(thirdCardPreviewImage)
        imageCollection.add(forthCardPreviewImage)
        imageCollection.add(fifthCardPreviewImage)
        imageCollection.add(sixthCardPreviewImage)

        cardCollection = mutableListOf(firstCardView,secondCardView,thirdCardView,forthCardView,fifthCardView,sixthCardView)
    }




    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializedBinding()



        viewModel.navigateToDashBoard.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { command ->
                when (command) {
                    is NavigationCommand.ToDirection ->
                        findNavController().navigate(command.directions)
                    is NavigationCommand.ShowError -> {
                        Toast.makeText(requireContext(), command.errMsg, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }


        val container = binding.constraintLayoutContainer
        container.setOnTouchListener { _, event ->
            scrollView.requestDisallowInterceptTouchEvent(true)
            when (event.action and event.actionMasked) {
                MotionEvent.ACTION_MOVE -> {
                    move(event)
                }
            }
            true
        }


        val yOffset = sliderContainer.height / 4

        lifecycleScope.launchWhenStarted {

            firstCardView.setOnClickListener {

                sliderContainer.y = (readingContainer.top + yOffset).toFloat()
                setCardActive(1)
            }
            secondCardView.setOnClickListener {
                val newYPosition = readingContainer.top + firstCardView.height + yOffset
                sliderContainer.y = newYPosition.toFloat()
                setCardActive(2)
            }
            thirdCardView.setOnClickListener {
                val newYPosition = readingContainer.top +
                        firstCardView.height +
                        secondCardView.height + yOffset
                sliderContainer.y = newYPosition.toFloat()
                setCardActive(3)
            }
            forthCardView.setOnClickListener {
                val newYPosition = readingContainer.top +
                        firstCardView.height +
                        secondCardView.height + thirdCardView.height + yOffset
                sliderContainer.y = newYPosition.toFloat()
                setCardActive(4)
            }
            fifthCardView.setOnClickListener {
                val newYPosition = readingContainer.top +
                        firstCardView.height +
                        secondCardView.height + thirdCardView.height + forthCardView.height +
                        yOffset
                sliderContainer.y = newYPosition.toFloat()
                setCardActive(5)
            }
            sixthCardView.setOnClickListener {
                val newYPosition = readingContainer.top +
                        firstCardView.height +
                        secondCardView.height + thirdCardView.height + forthCardView.height +
                        +fifthCardView.height +
                        yOffset
                sliderContainer.y = newYPosition.toFloat()
                setCardActive(6)
            }

            val cameraButton = binding.cameraButton
            cameraButton.setOnClickListener {
                takePicture()
            }

            val clearSelectionButton = binding.clearSelectionButton
            clearSelectionButton.setOnClickListener {
                clearSelection()
            }

            val confirmSelectionButton = binding.confirmSelectionButton
            confirmSelectionButton.setOnClickListener {
                val saveReading = when (selectedReadingValue) {
                    -1 -> ResultCode.NEGATIVE.name
                    0 -> ResultCode.TRACE.name
                    1 -> ResultCode.ONE_PLUS.name
                    2 -> ResultCode.TWO_PLUS.name
                    3 -> ResultCode.THREE_PLUS.name
                    4 -> ResultCode.FOUR_PLUS.name
                    else -> {
                        Toast.makeText(requireContext(), "No Selection", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }

                }

                viewModel.saveResult(TestResult(LocalDate.now(),
                    saveReading,
                    "",
                    1))

            }
        }
    }



    private fun setCardActive(cardAtPosition : Int){
        selectedReadingValue = NO_SELECTION
        val size = cardCollection.size
        cardCollection.forEachIndexed { index, cardView ->

            when(cardAtPosition){
                (index+1) -> {
                    selectedReadingValue = size - (index+2)
                    cardView.isActivated = true
                }
                else ->{
                    cardView.isActivated = false
                }

            }

        }


        /* when(cardAtPosition){

             1 -> {
                 selectedReadingValue = 4
                 firstCardView.isActivated = true
                 secondCardView.isActivated = false
                 thirdCardView.isActivated = false
                 forthCardView.isActivated = false
                 fifthCardView.isActivated= false
                 sixthCardView.isActivated = false
             }
             2 -> {
                 selectedReadingValue = 3
                 firstCardView.isActivated = false
                 secondCardView.isActivated = true
                 thirdCardView.isActivated = false
                 forthCardView.isActivated = false
                 fifthCardView.isActivated= false
                 sixthCardView.isActivated = false
             }
             3 ->{
                 selectedReadingValue = 2
                 firstCardView.isActivated = false
                 secondCardView.isActivated = false
                 thirdCardView.isActivated = true
                 forthCardView.isActivated = false
                 fifthCardView.isActivated= false
                 sixthCardView.isActivated = false
             }
             4 ->{
                 selectedReadingValue = 1
                 firstCardView.isActivated = false
                 secondCardView.isActivated = false
                 thirdCardView.isActivated = false
                 forthCardView.isActivated = true
                 fifthCardView.isActivated= false
                 sixthCardView.isActivated = false
             }
             5 ->{
                 selectedReadingValue = 0
                 firstCardView.isActivated = false
                 secondCardView.isActivated = false
                 thirdCardView.isActivated = false
                 forthCardView.isActivated = false
                 fifthCardView.isActivated= true
                 sixthCardView.isActivated = false
             }
             6 ->{
                 selectedReadingValue = -1
                 firstCardView.isActivated = false
                 secondCardView.isActivated = false
                 thirdCardView.isActivated = false
                 forthCardView.isActivated = false
                 fifthCardView.isActivated= false
                 sixthCardView.isActivated = true
             }
             else ->{
                 selectedReadingValue = NO_SELECTION
                 firstCardView.isActivated = false
                 secondCardView.isActivated = false
                 thirdCardView.isActivated = false
                 forthCardView.isActivated = false
                 fifthCardView.isActivated= false
                 sixthCardView.isActivated = false
             }
         }*/
    }

    private fun move(e : MotionEvent){
        val offset = sliderContainer.height/2
        val  maxUpwardMovement = readingContainer.top
        val minDownwardMovement = readingContainer.top + readingContainer.height
        val  firstCardYPos = readingContainer.top
        val  secondCardYPos = firstCardYPos + firstCardView.height
        val  thirdCardYPos = secondCardYPos + secondCardView.height
        val forthCardYPos = thirdCardYPos + thirdCardView.height
        val fifthCardYPos = forthCardYPos + forthCardView.height
        val sixthCardYPos = fifthCardYPos + fifthCardView.height

        when(e.y.toInt()){
            in maxUpwardMovement..minDownwardMovement -> {
                sliderContainer.y = e.y - offset

                when(e.y.toInt()){
                    in firstCardYPos..secondCardYPos ->  {
                        setCardActive(1)
                    }
                    in secondCardYPos..thirdCardYPos ->   {
                        setCardActive(2)
                    }
                    in thirdCardYPos..forthCardYPos ->    {
                        setCardActive(3)
                    }
                    in forthCardYPos..fifthCardYPos ->    {
                        setCardActive(4)
                    }
                    in fifthCardYPos..sixthCardYPos ->    {
                        setCardActive(5)
                    }
                    in sixthCardYPos..minDownwardMovement -> {
                        setCardActive(6)
                    }

                }

            }
        }



    }

    private fun takePicture(){
        lifecycleScope.launch {
            getTmpPicture().let {
                latestImageUri = it
                takePictureContract.launch(it)
            }
        }
    }
    private fun getTmpPicture() : Uri?{
        val tmpFile = File.createTempFile(Calendar.getInstance().timeInMillis.toString(),".jpeg",requireContext().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return   FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }



}




/* private val touchListener = OnTouchListener { view, event ->
     val x = event.rawX.toInt()
     val y = event.rawY.toInt()


     when (event.action and MotionEvent.ACTION_MASK) {



         MotionEvent.ACTION_DOWN -> {
             val lParams = view.layoutParams as FrameLayout.LayoutParams
             xDelta = x - lParams.leftMargin
             yDelta = y - lParams.topMargin
         }
         MotionEvent.ACTION_UP -> {
             val lparas = view.layoutParams as FrameLayout.LayoutParams
             xDelta = x - lparas.leftMargin
             yDelta = y- lparas.bottomMargin
         }

         MotionEvent.ACTION_MOVE -> {
             if (y - yDelta + view.height <= container.getHeight() && y - yDelta >= 0) {
             val layoutParams = view.layoutParams as FrameLayout.LayoutParams
             layoutParams.topMargin = y - yDelta
             layoutParams.bottomMargin = 0
             view.layoutParams = layoutParams
         }

           *//* if (x - xDelta + view.width <= container.getWidth() && y - yDelta + view.height <= container.getHeight() && x - xDelta >= 0 && y - yDelta >= 0) {
                    val layoutParams = view.layoutParams as FrameLayout.LayoutParams
                    layoutParams.leftMargin = x - xDelta
                    layoutParams.topMargin = y - yDelta
                    layoutParams.rightMargin = 0
                    layoutParams.bottomMargin = 0
                    view.layoutParams = layoutParams
                }*//*
            }

            MotionEvent.ACTION_BUTTON_PRESS ->{

            }


        }
        container.invalidate()
        true
    }

}*/

/*
//use of compose

override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
): View {
//below line is important for fragment; In activity we can simply call setContent() but not in fragment
    return ComposeView(requireContext()).apply {

        setContent {
            Column(verticalArrangement = Arrangement.Center,

                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()) {
                ResultButton(text = "NEGATIVE", viewModel = viewModel)
                ResultButton(text = "ONE_PLUS", viewModel = viewModel)
                ResultButton(text = "TWO_PLUS", viewModel = viewModel)
                ResultButton(text = "THREE_PLUS", viewModel = viewModel)
                ResultButton(text = "FOUR_PLUS", viewModel = viewModel)

            }

        }
    }

//Result button is a composable function
 */


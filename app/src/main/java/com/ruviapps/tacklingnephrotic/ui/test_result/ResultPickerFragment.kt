package com.ruviapps.tacklingnephrotic.ui.test_result

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Toast
import androidx.cardview.widget.CardView

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options

import com.ruviapps.tacklingnephrotic.R
import com.ruviapps.tacklingnephrotic.database.entities.ResultCode
import com.ruviapps.tacklingnephrotic.databinding.ReadingSliderBinding
import com.ruviapps.tacklingnephrotic.domain.TestResult
import com.ruviapps.tacklingnephrotic.utility.BaseFragment
import com.ruviapps.tacklingnephrotic.utility.observeAndHandleEvent
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate



@AndroidEntryPoint
class ResultPickerFragment : BaseFragment() {

   private val args : ResultPickerFragmentArgs by navArgs()

    override val isBottomBarVisible: Int
        get() = View.GONE
    override val isFabVisible: Int
        get() = View.GONE

    companion object {
        const val NO_SELECTION = -100
    }
    private var selectedReadingValue = NO_SELECTION
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


    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
                imageCollection.forEach {
                    it.visibility= View.VISIBLE
                    it.setImageURI(result.uriContent)
                }

        } else {
            // an error occurred
            val exception = result.error
            Log.d("myApps","$exception")
            imageCollection.forEach{
                it.visibility = View.INVISIBLE
            }
        }
    }
    private fun crop(){
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
                setOutputCompressFormat(Bitmap.CompressFormat.PNG)
            }
        )
    }

    val viewModel: ResultPickerViewModel by viewModels()
    private lateinit var binding: ReadingSliderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ReadingSliderBinding.inflate(layoutInflater)
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


        viewModel.navigateToDashBoard.observeAndHandleEvent(this)

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
                crop()
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
                        Toast.makeText(requireContext(), getString(R.string.no_reading_picked), Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }

                }

                viewModel.saveResult(TestResult(LocalDate.now(),
                    saveReading,
                    "",
                    args.patientId))

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


}




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


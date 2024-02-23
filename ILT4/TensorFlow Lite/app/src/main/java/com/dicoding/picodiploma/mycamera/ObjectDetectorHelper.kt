package com.dicoding.picodiploma.mycamera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.Surface
import androidx.camera.core.ImageProxy
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector

class ObjectDetectorHelper(
    var threshold: Float = 0.1f,
    var maxResults: Int = 3,
    //TODO 1 : Change model name
    val modelName: String = "efficientdet_lite0_v1.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    //TODO 2 : Rename to ObjectDetector
    private var objectDetector: ObjectDetector? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        // TODO 3 : Adjust to ObjectDetector.ObjectDetectorOptions
        val optionsBuilder = ObjectDetector.ObjectDetectorOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            // TODO 4 : Adjust to ObjectDetector
            objectDetector = ObjectDetector.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    //TODO 9 : remove unused code

    fun classifyLiveImage(image: ImageProxy) {

        if (objectDetector == null) {
            setupImageClassifier()
        }

        val imageProcessor = ImageProcessor.Builder()
            // TODO 5 : Add rotation
            .add(Rot90Op(-image.imageInfo.rotationDegrees / 90))
            .build()

        val bitmapBuffer = Bitmap.createBitmap(
            image.width,
            image.height,
            Bitmap.Config.ARGB_8888
        )
        image.use { bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer) }
        image.close()

        val tensorImage: TensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmapBuffer))

        val imageProcessingOptions = ImageProcessingOptions.builder()
            // TODO 6 : Remove orientation
//            .setOrientation(getOrientationFromRotation(image.imageInfo.rotationDegrees))
            .build()

        var inferenceTime = SystemClock.uptimeMillis()
        val results = objectDetector?.detect(tensorImage, imageProcessingOptions)
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime
        classifierListener?.onResults(
            results,
            inferenceTime,
            //TODO 8 : Add imageHeight and imageWidth argument
            tensorImage.height,
            tensorImage.width
        )
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            //TODO 7 : Add imageHeight and imageWidth parameter
            results: List<Detection>?,
            inferenceTime: Long,
            imageHeight: Int,
            imageWidth: Int
        )
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}
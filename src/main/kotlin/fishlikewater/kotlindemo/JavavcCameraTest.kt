package fishlikewater.kotlindemo

import org.bytedeco.javacpp.Loader
import org.bytedeco.javacpp.avcodec
import org.bytedeco.javacpp.opencv_objdetect
import org.bytedeco.javacv.*
import org.bytedeco.javacv.Frame
import java.awt.*
import javax.swing.JFrame






class JavavcCameraTest {


}

fun main(args: Array<String>) {
    val inputFile = "video=\"E:\\bootProject\\kotlin-demo\\output.mp4\""
    // Decodes-encodes
    val outputFile = "recorde.mp4"
    frameRecord(inputFile, outputFile, 1)
}

/**
 * 录制桌面
 */

fun video(){
    var screenSize = Toolkit.getDefaultToolkit().screenSize;
    val rectangle = Rectangle(screenSize)// 指定捕获屏幕区域大小，这里使用全屏捕获
    val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()//本地环境
    val gs = ge.screenDevices//获取本地屏幕设备列表
    System.err.println("eguid温馨提示，找到" + gs.size + "个屏幕设备")
    var robot: Robot? = null
    var ret = -1
    for (index in 0..9) {
        val g = gs[index]
        try {
            robot = Robot(g)
            val img = robot!!.createScreenCapture(rectangle)
            if (img != null && img!!.getWidth() > 1) {
                ret = index
                break
            }
        } catch (e: AWTException) {
            System.err.println("打开第" + index + "个屏幕设备失败，尝试打开第" + (index + 1) + "个屏幕设备")
        }

    }
    System.err.println("打开的屏幕序号：" + ret)
    val frame = CanvasFrame("eguid屏幕录制")// javacv提供的图像展现窗口
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    val width = 800
    val height = 600
    frame.setBounds((screenSize.getWidth().toInt() - width) as Int / 2, (screenSize.getHeight().toInt() - height) as Int / 2, width,
            height)// 窗口居中
    frame.setCanvasSize(width, height)// 设置CanvasFrame窗口大小
    while (frame.isShowing) {
        val image = robot!!.createScreenCapture(rectangle)// 从当前屏幕中读取的像素图像，该图像不包括鼠标光标
        frame.showImage(image)

        try {
            Thread.sleep(45)
        } catch (e: InterruptedException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

    }
    frame.dispose()
}

/**
 * 调用摄像头
 */
fun canvasFrame() {
    val grabber = OpenCVFrameGrabber(0)
    grabber.start()   //开始获取摄像头数据
    val canvas = CanvasFrame("摄像头")//新建一个窗口
    canvas.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    canvas.isAlwaysOnTop = true

    while (true) {
        if (!canvas.isDisplayable) {//窗口是否关闭
            grabber.stop()//停止抓取
            System.exit(2)//退出
        }
        canvas.showImage(grabber.grab())//获取摄像头图像并放到窗口上显示， 这里的Frame frame=grabber.grab(); frame是一帧视频图像

        Thread.sleep(50)//50毫秒刷新一次图像
    }
}

/**
 * 按帧录制本机摄像头视频（边预览边录制，停止预览即停止录制）
 *
 * @author eguid
 * @param outputFile -录制的文件路径，也可以是rtsp或者rtmp等流媒体服务器发布地址
 * @param frameRate - 视频帧率
 * @throws Exception
 * @throws InterruptedException
 * @throws org.bytedeco.javacv.FrameRecorder.Exception
 */
@Throws(Exception::class, InterruptedException::class, org.bytedeco.javacv.FrameRecorder.Exception::class)
fun recordCamera(outputFile: String, frameRate: Double) {
    Loader.load(opencv_objdetect::class.java)
    val grabber = FrameGrabber.createDefault(0)//本机摄像头默认0，这里使用javacv的抓取器，至于使用的是ffmpeg还是opencv，请自行查看源码
    grabber.start()//开启抓取器

    val converter = OpenCVFrameConverter.ToIplImage()//转换器
    var grabbedImage = converter.convert(grabber.grab())//抓取一帧视频并将其转换为图像，至于用这个图像用来做什么？加水印，人脸识别等等自行添加
    val width = grabbedImage.width()
    val height = grabbedImage.height()

    val recorder = FrameRecorder.createDefault(outputFile, width, height)
    recorder.videoCodec = avcodec.AV_CODEC_ID_H264 // avcodec.AV_CODEC_ID_H264，编码
    recorder.format = "mp4"//封装格式，如果是推送到rtmp就必须是flv封装格式
    recorder.frameRate = frameRate

    recorder.start()//开启录制器
    var startTime: Long = 0
    var videoTS: Long = 0
    val frame = CanvasFrame("camera", CanvasFrame.getDefaultGamma() / grabber.gamma)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.isAlwaysOnTop = true
    var rotatedFrame = converter.convert(grabbedImage)//不知道为什么这里不做转换就不能推到rtmp
    while (frame.isVisible && converter.convert(grabber.grab()).apply { grabbedImage = this } != null) {
        rotatedFrame = converter.convert(grabbedImage)
        frame.showImage(rotatedFrame)
        if (startTime == 0L) {
            startTime = System.currentTimeMillis()
        }
        videoTS = 1000 * (System.currentTimeMillis() - startTime)
        recorder.timestamp = videoTS
        recorder.record(rotatedFrame)
        grabbedImage = null;
        Thread.sleep(40)
    }
    frame.dispose()
    recorder.stop()
    recorder.release()
    grabber.stop()

}

/**
 * 按帧录制视频
 *
 * @param inputFile-该地址可以是网络直播/录播地址，也可以是远程/本地文件路径
 * @param outputFile
 * -该地址只能是文件地址，如果使用该方法推送流媒体服务器会报错，原因是没有设置编码格式
 * @throws FrameGrabber.Exception
 * @throws FrameRecorder.Exception
 * @throws org.bytedeco.javacv.FrameRecorder.Exception
 */
@Throws(Exception::class, org.bytedeco.javacv.FrameRecorder.Exception::class)
fun frameRecord(inputFile: String, outputFile: String, audioChannel: Int) {

    val isStart = true//该变量建议设置为全局控制变量，用于控制录制结束
    // 获取视频源
    val grabber = FFmpegFrameGrabber(inputFile)
    grabber.videoCodec = avcodec.AV_CODEC_ID_H264
    grabber.setFormat("mp4");
    // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
    val recorder = FFmpegFrameRecorder(outputFile, 1280, 720, audioChannel)
    //recorder.videoCodec = avcodec.AV_CODEC_ID_H264
    // 开始取视频源
    recordByFrame(grabber, recorder, isStart)
}

@Throws(Exception::class, org.bytedeco.javacv.FrameRecorder.Exception::class)
private fun recordByFrame(grabber: FFmpegFrameGrabber, recorder: FFmpegFrameRecorder, status: Boolean?) {
    try {//建议在线程中使用该方法
        grabber.start()
        recorder.start()
        var frame: Frame? = null
        while (status!! && grabber.grabFrame().apply { frame = this } != null) {
            recorder.record(frame)
        }
        recorder.stop()
        grabber.stop()
    } finally {
        grabber?.stop()
    }
}

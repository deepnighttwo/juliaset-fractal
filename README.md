# A Java Swing program for Julia Set image creation

## Tips and How to
 * Using mouse wheel to *room in or room out (This is really cool. Please have a try~)*. Left click can move the point to the center. Right click for reset.
 * Using the panel on the right side to adjust the parameters and take operations. 
 * For the best performance, please set the Thread Count on Performance tab to the *physical CPU core number*.
 * Parameters on Basic tab decide the *shape of the image*.  The Mandellbort image can help to choose value. For Instant mode, the value will depends on the mouse. For Continuous mode, it depends on mouse drag.
 * Parameters on Image tab decide the *size* of the image. Precision also can effect the look of the image. P.S. If the image size is set too big, it would bring Out Of Memory error.
 * Parameters on color table decide the *color* of the image. 
 * Parameters in Performance table effect the generation performance. Please specify the Thread Count to physical CPU core for the best performance. And if CPU usage is not high, please try to decrease the *Task Unit* parameter.
 * On Operation panel, it is provided to save the current image with selected format. The size of the image is set on the *Image* tab. 
 * Log can be enabled to show the generation time. But it also makes the program run a little slow 

## Videos
 * Images Generated: http://www.youtube.com/watch?v=OhPdifmZvpQ
 * Demo: http://www.youtube.com/watch?v=HH0sdF4m3vI
 * Old Demo: http://www.youtube.com/watch?v=S90lrVJvUhA
 
#一个用于生成Julia Set图的Java Swing小程序

## 帮助
 * 鼠标滚轮可以*放大缩小图像*。左键可以使点击处居中。右键恢复初始状态。
 * 在右边的面板中可以对参数进行各种调整和进行各种操作。
 * *提供以Mandelbrot集为参考，取鼠标划线坐标连续生成图像的功能* 。
 * 为了得到最高性能，推荐在性能调试面板中将计算线程数设置为电脑的 *CPU核心数* 。
 * 基本设置中的数字决定了图像的形状。可以通过输入参数改变形状，也可以通过Mandelbrot选择参数，即时选值时根据鼠标位置，连续选值时需要通过按住鼠标左键拖拽。
 * 图像配置中的参数决定了图像的*大小和比例*。其中精度也会影响图像的颜色和细节。注意，如果图像太大可能会引起内存不足。
 * 颜色设置里的参数决定了图像的颜色。
 * 性能调试里推荐修改的就是工作线程数和任务大小。出界阈值对性能影响不大。如果工作线程设置为CPU核心数后，CPU利用率依然低，可以尝试减少任务大小的值。*。
 * 在操作面板里，可以根据选择的格式保存当前图像，图像大小是在*图像配置*中设置的。
 * 可以打开日志来监测生成图像的时间，但是这会稍微影响程序整体性能。

##程序功能

  * *图像实时生成预览。*
  * *使用鼠标滚轮可以放大缩小图像*
  * *增加性能调试功能，通吃高中低档机器*
  * 方便设置用于生成Julia Set的常数的实部和虚部。
  * 方便设置控制坐标系大小的值。
  * 可以随意更改颜色，并有多种取色方案供选择。
  * 用于控制Julia Set的精度值 (精度越大计算越慢)。
  * 生成任意大小的图片。(生成图像的像素越大，则消耗内存越多，注意OutOfMemoryError)。
  * 除了上面常用的参数外，还提供更多丰富的参数，可以更灵活的生成JuliaSet分形图。
  * 保存生成的图像。
  * more...... 程序在线演示（旧版程序，新版性能和操作有改进，但是核功能没变）：http://v.youku.com/v_show/id_XMTU5MTY3MTEy.html

## 扩展性

  * *使用XML生成图形界面，以此为基础可以方便的增加对其它分形计算的支持。*

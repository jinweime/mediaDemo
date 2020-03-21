package way.king.com.media01

import org.junit.Test

import org.junit.Assert.*
import way.king.com.media01.util.KmUtils
import way.king.com.media01.util.TimeConstants
import way.king.com.media01.util.TimeUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @Test
    fun addition_isCorrect() {
       print( KmUtils.formatKm(9800))
//        print(gets())
//        if (span < 1000) {
//            return "刚刚"
//        } else if (span < TimeConstants.MIN) {
//            return String.format("%d秒前", span / TimeConstants.SEC)
//        } else if (span < TimeConstants.HOUR) {
//            return String.format("%d分钟前", span / TimeConstants.MIN)
//        }
//

//

//
    }


    fun gets(): String {
        val testTime = 1583462868000;
        val str = TimeUtils.getNowTimeMills()
        var info = ""
        //时差
        val value: Long = str - testTime
//        未超过24小时，显示“XX小时前”；
        var f1 = value < TimeConstants.DAY
//        大于等于24小时小于48小时，显示“1天前”
        var f2 = value >= TimeConstants.DAY && value <= TimeConstants.DAY * 2
//        大于等于48小时小于72小时，显示“2天前”；
        var f3 = value >= TimeConstants.DAY * 2 && value <= TimeConstants.DAY * 3
//        大于等于72小时，显示日期“X月X日”；
        var f4 = value >= TimeConstants.DAY * 3
        if (f1) {
            if (value <= TimeConstants.HOUR) {
                return String.format("%d小时前", 1)
            } else {
                return String.format("%d小时前", (value / TimeConstants.HOUR))
            }
        } else if (f2) {
            return String.format("%d天前", 1)
        } else if (f3) {
            return String.format("%d天前", 2)
        } else if (f4) {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Date(java.lang.Long.parseLong(testTime.toString())))
            return format
        }
        return ""
    }
}

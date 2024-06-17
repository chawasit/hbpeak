package coffee.hh.hbpeak

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import coffee.hh.hbpeak.theme.HBPeakTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            HBPeakTheme {
                HBPeakNavHost()
            }
        }
    }
}
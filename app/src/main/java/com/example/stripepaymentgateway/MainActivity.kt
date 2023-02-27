package com.example.stripepaymentgateway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stripepaymentgateway.databinding.ActivityMainBinding
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    val PUBLISH_KEY = "pk_test_51Mfnd0JI0n14QA9nQB2R4pJWQ15zR7sL8oS7mny89AaENIUwGuq9MBz2kFnaoQ3JE6W7q9k4kd632GLZWCdPYcFn00DJwAjpmf"
    val SECRET_KEY = "sk_test_51Mfnd0JI0n14QA9n8s6PboXTj53PjuDnk0UaDfjzE0SWA7Uc22OygRiGin7mpm0CkQqVCIPFakvtYiO44ZBVvom7008nddNBGn"
    lateinit var paymentSheet : PaymentSheet
    lateinit var paymentIntentClientSecret :String
    lateinit var customerConfig :PaymentSheet.CustomerConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        return setContentView(mainBinding.root)

        PaymentConfiguration.init(this,PUBLISH_KEY)
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret,
        PaymentSheet.Configuration(
            merchantDisplayName = "Aafiyath",
            customer =customerConfig))


    }
}
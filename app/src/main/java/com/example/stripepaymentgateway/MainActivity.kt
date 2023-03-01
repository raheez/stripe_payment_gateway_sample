import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.stripepaymentgateway.databinding.ActivityMainBinding
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    val PUBLISH_KEY =
        "pk_test_51Mfnd0JI0n14QA9nQB2R4pJWQ15zR7sL8oS7mny89AaENIUwGuq9MBz2kFnaoQ3JE6W7q9k4kd632GLZWCdPYcFn00DJwAjpmf"
    val SECRET_KEY =
        "sk_test_51Mfnd0JI0n14QA9n8s6PboXTj53PjuDnk0UaDfjzE0SWA7Uc22OygRiGin7mpm0CkQqVCIPFakvtYiO44ZBVvom7008nddNBGn"
    lateinit var paymentSheet: PaymentSheet
    lateinit var paymentIntentClientSecret: String
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration

    var mCustomerID = ""
    var mEphemeralKey = ""
    var mClientSecret = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)


        PaymentConfiguration.init(this, PUBLISH_KEY)

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        mainBinding?.buttonPay?.setOnClickListener {
            paymemntFlow()
        }


        var mStrRequest = object : StringRequest(com.android.volley.Request.Method.POST,
            "https://api.stripe.com/v1/customers", {

                val mObject = JSONObject(it)
                mCustomerID = mObject.getString("id")
                Toast.makeText(this, "Customer-id obtained " + mCustomerID, Toast.LENGTH_SHORT)
                    .show()
                getEphermalKey()

            },
            {

            }

        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + SECRET_KEY
                return headers
            }
        };

        val mRequestQueue = Volley.newRequestQueue(this)
        mRequestQueue.add(mStrRequest)

        return setContentView(mainBinding.root)

    }

    private fun getEphermalKey() {
        var mStrRequest = object : StringRequest(com.android.volley.Request.Method.POST,
            "https://api.stripe.com/v1/ephemeral_keys", {

                val mObject = JSONObject(it)
                mEphemeralKey = mObject.getString("id")
                Toast.makeText(this, "ephermal key obtained " + mEphemeralKey, Toast.LENGTH_SHORT)
                    .show()

                getClientSecret(mCustomerID, mEphemeralKey)
            },
            {

            }

        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + SECRET_KEY
                headers["Stripe-Version"] = "2022-11-15"
                return headers
            }

            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["customer"] = mCustomerID
                return params
            }
        };

        val mRequestQueue = Volley.newRequestQueue(this)
        mRequestQueue.add(mStrRequest)
    }

    private fun getClientSecret(mCustomerID: String, mEphemeralKey: String) {

        var mStrRequest = object : StringRequest(com.android.volley.Request.Method.POST,
            "https://api.stripe.com/v1/payment_intents", {

                val mObject = JSONObject(it)
                mClientSecret = mObject.getString("client_secret")
                Toast.makeText(
                    this,
                    "client_secret key obtained " + mClientSecret,
                    Toast.LENGTH_SHORT
                )
                    .show()

            },
            {

            }

        ) {

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + SECRET_KEY
                return headers
            }

            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["customer"] = mCustomerID
                params["amount"] = "350000"
                params["currency"] = "aed"
                params["automatic_payment_methods[enabled]"] = "true"
                return params
            }
        };

        val mRequestQueue = Volley.newRequestQueue(this)
        mRequestQueue.add(mStrRequest)
    }

    private fun paymemntFlow() {
        paymentSheet.presentWithPaymentIntent(
            mClientSecret, PaymentSheet.Configuration(
                "Aafiyath", PaymentSheet.CustomerConfiguration(
                    mCustomerID,
                    mEphemeralKey
                )
            )
        )
    }


    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(this, "${paymentSheetResult.error}", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                print("Completed")
                Toast.makeText(this, "completed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
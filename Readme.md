# *COWPAY Android SDK*

This document is a guide for Cowpay Android SDK. In addition, following the below steps will help
you learn how to add and use (Cowpay SDK) in your Android Application.

## REQUIREMENTS

_Minimum Android SDK 24_
_Target API level 33_

## INSTALLATION

1- Add JitPack repository to the project level build.gradle file:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
} 
```
2- Add Frames SDK dependency to the module gradle file:

```
dependencies {
    implementation 'com.github.CowpayPayment:Cowpay-Android:1.0.0'
}
```

## IMPORT

```
import 'com.luminsoft.cowpay_sdk'
```

## USAGE

Step 1: Initialize the payment info.

```
val paymentInfo = PaymentInfo(
    merchantReferenceId = "merchantReferenceId",
    customerMerchantProfileId = "customerMerchantProfileId",
    amount = 200.0,
    customerFirstName = "FirstName",
    customerLastName = "LastName",
    customerMobile = "01234567890",
    customerEmail = "customer@customer.com",
    description = "description",
    isFeesOnCustomer = false
)
```
Step 2: Initialize the sdk.
CowpaySDK.init function is used for Initializing Cowpay sdk instance to use it. It’s a throws function so please put it in a try…catch blocs.

```
try {
    CowpaySDK.init(
        merchantCode = merchantCode,
        hashKey = merchantHashKey,
        merchantPhoneNumber = merchantPhoneNumber,
        paymentInfo = paymentInfo,
        environment = CowpayEnvironment.STAGING or CowpayEnvironment.PRODUCTION,
        localizationCode = LocalizationCode.AR or LocalizationCode.EN,
        merchantLogo = merchantIconLink
    )
} catch (e: Exception) {
    Log.e("error", e.toString())
}
```

Step 3: launch sdk and create a callback object.
CowpaySDK.launch function is used for launch Cowpay sdk. It’s a throws function so please put it in a try…catch blocs.
CowpayCallBack object contains Success, error and closedByUser call backs

```
try {
    CowpaySDK.launch(activity, object : CowpayCallback {
        override fun success(paymentSuccessModel: PaymentSuccessModel) {
            // Successful payment call back
            // paymentSuccessModel.paymentMethodName
            // paymentSuccessModel.paymentReferenceId

        }
        override fun error(paymentFailedModel: PaymentFailedModel) {
            // error during the payment process
        }
        override fun closedByUser() {
            // the user decided to leave the payment page
        }

    })

} catch (e: Exception) {
    Log.e("error", e.toString())
}
```
## Additional Options
```
If you want to do an action based on payment method you can make success call back like that:

override fun success(paymentSuccessModel: PaymentSuccessModel) {
if (paymentSuccessModel is PaymentSuccessModel.FawrySuccessModel) {
Log.e("SuccessFawry", paymentSuccessModel.paymentMethodName)
} else if (paymentSuccessModel is PaymentSuccessModel.CreditCardSuccessModel) {
Log.e("SuccessCard", paymentSuccessModel.paymentReferenceId)
Log.e("SuccessCard", paymentSuccessModel.paymentMethodName)
}
Log.e("SuccessCard", paymentSuccessModel.toString())
}
```

## VALUES DESCRIPTION

| Key                      | values                                                                                                                                         |
|--------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| localizationCode         | Select your language code enum value LocalizationCode.EN for English, and LocalizationCode.AR if Arabic. Default value is LocalizationCode.EN. |
| environment              | Select the environment CowpayEnvironment.STAGING for staging and CowpayEnvironment.PRODUCTION for production                                   |
| amount                   | Transaction amount. Two decimal values like "15.60", were sent in case of partial capture.                                                     |
| description              | charge request description that reserve the payment name                                                                                       |
| customerMerchantProfileId | ID of the customer being charged on your system                                                                                                |
| merchantReferenceId      | Unique alphanumeric value required as identifier for the charge request                                                                        |
| customerFirstName        | customer first name being charged                                                                                                              |
| customerLastName         | customer last name being charged                                                                                                               |
| customerEmail            | customer valid email address                                                                                                                   |
| customerMobile           | internationally formatted customer mobile. should be starts with 01                                                                            |
| isFeesOnCustomer         | Boolean variable, True if the customer will pay transaction fees and false if not. Default value is false                                      |
| merchantCode             | Your code that is presented in your panel.                                                                                                     |
| merchantHashKey          | Hash Key that is presented in your panel.                                                                                                      |
| merchantPhoneNumber      | merchant phone number                                                                                                                          |
| merchantLogo             | merchant logo url                                                                                                                              |
| CowpayCallBack           | Call back functions model. it contains success, error and closedByUser call backs                                                              |
| success                  | Call back function when translation succeeds                                                                                                   |
| error                    | Call back function if an error occurred.                                                                                                       |
| closedByUser             | Call back function if customer goes back before making a transaction.                                                                          |
| PaymentSuccessModel      | Payment success model that contains paymentReferenceId and paymentMethodName.                                                                  |
| paymentReferenceId       | Payment reference number.                                                                                                                      |
| paymentMethodName        | Payment Method name (CreditCard, Fawry, etc...).                                                                                               |

## DOCUMENTATION

You can check the [documentation](https://lumin-soft.gitbook.io/cowpay/cowpay-android-sdk).



# ValidationNotifierEditText
AbhinavChauhan97/ValidationNotifierEditText

A simple EditText which notifies observes when the text inside is valid or invalid by matching the text againt the regular expression that client provide.
Also client can choose to draw a border with desired color when it text becomes valid or invalid a default border color can also be provided

<b> Usage </b>

    <com.abhinav.chouhan.validationnotifieredittext.ValidationNotifierEditText
        android:id="@+id/vne"
        app:vne_borderColor="@android:color/holo_blue_dark"
        app:vne_borderWidth="2dp"
        app:vne_cornerRadius="5dp"
        app:vne_giveBorder="true"                                
        android:textColorHint="@android:color/darker_gray"
        app:vne_validBorderColor="@android:color/holo_blue_dark"
        app:vne_invalidBorderColor="@android:color/black"
        android:layout_width="match_parent"
        android:layout_height="wrap_content
        android:hint="enter only numbers"
        app:vne_validatorRegex="[1-9]+"
       />

 <i>note</i> no border drawing will happen if <b>vne_giveBorder</b> attribute is not set true
          
           val validationNotiferEditText = findViewById(R.id.vne)
           validationNotifierEditText.addValidationChangeListener(object : ValidationNotifierEditText.ValidationChangeListener{
            //called when text is valid
                  override fun onBecomeValid(validationNotifierEditText: ValidationNotifierEditText) {
                
                  }
            //called when text is invalid
                 override fun onBecomeInvalid(validationNotifierEditText: ValidationNotifierEditText) {
              }
        })

A Helper viewgroup is also provided when can contain any number ValidationNotifierEditText and can notify you when all of the ValidationNotifierEditText has valid texts 
and when any one ValidationNotifierEditText is now invalid but was previous valid , it can be helpful in situations like when you have a form and user should fill corrent data 
in all text fields then only you want to enable "OK" button.
In this situation you can use ValidationNotifierEditTextGroup just like a RadioGroup and put your ValidationNotifierEditText inside that container , if will greatly simplyfy the code
at client side and will decouple all the validation logic from real business logic, if tomorrow you like to add another field in the form you just change in the xml and all your business logic remains untouched

<b> Usage </b>

        <com.abhinav.chouhan.validationnotifieredittext.ValidationNotifierEditTextGroup xmlns:android="http://schemas.android.com/apk/res/android"
           android:id="@+id/vneg"
           android:layout_width="match_parent"
           android:layout_height="wrap_content"
           android:orientation="vertical">

         <com.abhinav.chouhan.validationnotifieredittext.ValidationNotifierEditText
             android:id="@+id/vne1"
             app:vne_borderColor="@android:color/holo_red_dark"
             app:vne_borderWidth="2dp"
             app:vne_giveBorder="true"
             android:layout_margin="5dp"
             app:vne_cornerRadius="10dp"
             app:vne_validBorderColor="@android:color/holo_green_dark"
             app:vne_invalidBorderColor="@android:color/holo_red_dark"
             android:layout_width="match_parent"
             android:layout_height="wrap_content"
             android:hint="enter only lower case letters"
             android:textColorHint="@android:color/darker_gray"
            app:vne_validatorRegex="[a-z]+"/>
 
           <com.abhinav.chouhan.validationnotifieredittext.ValidationNotifierEditText
              android:id="@+id/vne2"
              app:vne_borderColor="@android:color/holo_blue_dark"
              app:vne_borderWidth="2dp"
              app:vne_cornerRadius="5dp"
              app:vne_giveBorder="true"
              app:vne_validBorderColor="@android:color/holo_blue_dark"
              app:vne_invalidBorderColor="@android:color/black"
              android:layout_width="match_parent"
              android:layout_height="wrap_content"
              android:hint="enter only numbers"
              app:vne_validatorRegex="[1-9]+"
            />
 
        <com.abhinav.chouhan.validationnotifieredittext.ValidationNotifierEditText
            android:id="@+id/vne3"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="enter only upper case letters"
            app:vne_validatorRegex="[A-Z]+"
         />

      </com.abhinav.chouhan.validationnotifieredittext.ValidationNotifierEditTextGroup>


in code 

        val validationNotifierEditTextGroup = findViewById(R.id.vneg)
        validationNotifierEditTextGroup.setOnGroupValidationListener(object : ValidationNotifierEditTextGroup.ValidationEditTextGroupValidationListener{
              /*called when all ValidationNotifierEdiText contains valid text , you can enable ok button here you are all passed all ValidationNotiferEditText because maybe you 
              want to do something else with them like fading them away when all are valid and perform a fragment transaciton after that *.
                  override fun onAllBecomeValid(childValidationNotifierEditTexts: List<ValidationNotifierEditText>) {
                       childValidationNotifierEditTexts.forEach { _ ->
                       okButton.isEnabled = true
                     }
                 }
                 /* called when any ValidationNotifierEditText is now contains invalid text but was previous containing valid text you can disable ok button here because form 
                   should contain all valid text in all text fields you are also passed the ValidationNotfierEditText which now conatian invalid text so that you can 
                  tell user where things went wrong*/
                  override fun onAnyBecomeInvalid(validationNotifierEditText: ValidationNotifierEditText) {
                      okButton.isEnabled = false
                   }

              })


add in build.gradle to use in your project ->  implementation 'com.github.AbhinavChauhan97:ValidationNotifierEditText:1.1' // use latest vesion 

     

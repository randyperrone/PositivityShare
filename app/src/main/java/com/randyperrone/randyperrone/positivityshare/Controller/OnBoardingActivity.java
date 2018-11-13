package com.randyperrone.randyperrone.positivityshare.Controller;

import android.os.Bundle;

import com.cuneytayyildiz.onboarder.OnboarderActivity;
import com.cuneytayyildiz.onboarder.OnboarderPage;
import com.cuneytayyildiz.onboarder.utils.OnboarderPageChangeListener;
import com.randyperrone.randyperrone.positivityshare.R;

import java.util.Arrays;
import java.util.List;

public class OnBoardingActivity extends OnboarderActivity implements OnboarderPageChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int textSize = getResources().getInteger(R.integer.title_text_size);
        int descriptionTextSize = getResources().getInteger(R.integer.description_text_size);

        List<OnboarderPage> pages = Arrays.asList(
                new OnboarderPage.Builder()
                        .title("Create Profile")
                        .description("Create your profile.  Add a username, age, about me, and image.  Username is the only requirement. When finished, click Done")
                        .imageResourceId( R.drawable.onboarding1)
                        .backgroundColor(R.color.colorAccent)
                        .titleTextSize(textSize)
                        .descriptionTextSize(descriptionTextSize)
                        .multilineDescriptionCentered(true)
                        .build(),

                new OnboarderPage.Builder()
                        .title("Browse Users")
                        .description("Browse through different users.  Click on user to send them a positivity!")
                        .imageResourceId( R.drawable.onboarding2)
                        .backgroundColor(R.color.colorPrimaryDark)
                        .titleTextSize(textSize)
                        .descriptionTextSize(descriptionTextSize)
                        .multilineDescriptionCentered(true)
                        .build(),

                new OnboarderPage.Builder()
                        .title("Send Positivity")
                        .description("You may send the current positivity or press Get Next to choose a different one. Click send to send to user. Easy as that!")
                        .imageResourceId( R.drawable.onboarding3)
                        .backgroundColor(R.color.colorAccent)
                        .titleTextSize(textSize)
                        .descriptionTextSize(descriptionTextSize)
                        .multilineDescriptionCentered(true)
                        .build(),

                new OnboarderPage.Builder()
                        .title("Check your messages")
                        .description("Your messages will be listed out under Notifications.  Click a message to read it and see the user. After reading the message, it will be deleted.")
                        .imageResourceId( R.drawable.onboarding4)
                        .backgroundColor(R.color.colorPrimaryDark)
                        .titleTextSize(textSize)
                        .descriptionTextSize(descriptionTextSize)
                        .multilineDescriptionCentered(true)
                        .build()
        );

        setOnboarderPageChangeListener(this);
        initOnboardingPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {
        onBackPressed();
    }

    @Override
    public void onPageChanged(int position) {

    }
}

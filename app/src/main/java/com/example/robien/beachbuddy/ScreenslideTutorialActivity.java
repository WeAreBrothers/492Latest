package com.example.robien.beachbuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;


public class ScreenslideTutorialActivity extends FragmentActivity
        implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener{

    /**
     * fields
     */
    private static final int NUMBER_OF_PAGES = 5;
    private RadioGroup radioGroup;
    ViewPager pager;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_layout);

        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(this);

        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
    }

    /*************************************************************
     * Listeners for ViewPager
     *************************************************************/
    /**
     * When the current page is scrolled
     * @param position
     * @param v
     * @param i
     */
    @Override
    public void onPageScrolled(int position, float v, int i) {

    }

    /**
     * When a new page becomes selected
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        switch(position) {
            case 0:
                radioGroup.check(R.id.radioButton1);
                break;
            case 1:
                radioGroup.check(R.id.radioButton2);
                break;
            case 2:
                radioGroup.check(R.id.radioButton3);
                break;
            case 3:
                radioGroup.check(R.id.radioButton4);
                break;
            case 4:
                radioGroup.check(R.id.radioButton5);
                break;
            default:
                radioGroup.check(R.id.radioButton1);
        }
    }

    /**
     * When the pager is automatically setting to the current page
     * @param position
     */
    @Override
    public void onPageScrollStateChanged(int position) {

    }

    /**
     * On checked listener to Radio Buttons.
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId) {
            case R.id.radioButton1:
                pager.setCurrentItem(0);
                break;
            case R.id.radioButton2:
                pager.setCurrentItem(1);
                break;
            case R.id.radioButton3:
                pager.setCurrentItem(2);
                break;
            case R.id.radioButton4:
                pager.setCurrentItem(3);
                break;
            case R.id.radioButton5:
                pager.setCurrentItem(4);
                break;
        }
    }

    /**
     * Custom PagerAdapter class
     */
    private class MyPagerAdapter extends FragmentPagerAdapter{

        /**
         * Constructor
         * @param fm
         */
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment based on the position.
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return FirstFragment.newInstance
                            ("Opening the app will take you to the login page which consists of the" +
                                    " app's background image, name, and a login button that logs in with" +
                                    " your Facebook account. A successful login takes you to the main page shown above" +
                                    " From the main page the user can access any of the listed options," +
                                    " allowing them to add classes, search for classes, view their invites," +
                                    " messages, or groups.  Lastly if they so desire the user is able to" +
                                    " logout and it will return them to the login screen seen on the left.");
                case 1:
                    return SecondFragment.newInstance("From the main page the user can access any of the listed" +
                            " options, allowing them to add classes, search for classes, view their invites, messages, " +
                            " or groups.  Lastly if they so desire the user is able to logout and it will return them " +
                            " to the login screen seen on the left. The right panel shows a user is able to enter " +
                            " their classes in which they must provide a course name, course #, year, and instructor name. " +
                            " The bottom panel shows the class search function which allows users to search for other users " +
                            " who have registered the same or similar classes.");
                case 2:
                    return ThirdFragment.newInstance("The search function returns a list of users who are registered" +
                            " for that specific course.  The user is able to select a profile to view.If the user clicks" +
                            " ‘Send Group Invite’ an invite is sent to the student" +
                            " and the user is returned to the home screen.  If the user selects ‘Send Message’ they" +
                            " are taken to the screen on the right and the user is able to compose a message and send" +
                            " it to their selected recipient. ");
                case 3:
                    return FourthFragment.newInstance("Another core function of the app is the Invite. From the main" +
                            " page the user can select ‘View Invites’ which is seen here.  This allows the user to accept" +
                            " an invitation to join a class group, or deny it and delete the invitation. Accepting an invite" +
                            " puts you in a group of that class invite along with other members who have been invited to the" +
                            " group and accepted the invite.");
                case 4:
                    return FifthFragment.newInstance("From the main screen the user can select to ‘View Messages’. The app will" +
                            " take you to a screen with listed unread messages if you have any. From this screen the user can" +
                            " select the message at which point it will take them to the a screen that displays the message, " +
                            " the sender, and an option to delete the message or return to profile. You can also send a message" +
                            " back to the sender as shown above. This can be accomplished in a couple of ways. You can search for the person" +
                            " you want to send the message to. You can also select someone in your group and send them a message" +
                            " that way as well.");
                default:
                    return FirstFragment.newInstance("First, Default");
            }
        }

        /**
         * Return the number of pages.
         * @return
         */
        @Override
        public int getCount() {
            return NUMBER_OF_PAGES;
        }


    }


}
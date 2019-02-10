package com.example.ptquy.placepipi;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

public class QuyenActivity extends AppCompatActivity {

	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quyen);
		fragmentManager = getSupportFragmentManager();

		final PaperOnboardingFragment onBoardingFragment = PaperOnboardingFragment.newInstance(getDataForOnboarding());

		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.fragment_container, onBoardingFragment);
		fragmentTransaction.commit();

		onBoardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
			@Override
			public void onRightOut() {
				/*FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				Fragment bf = new BlankFragment();
				fragmentTransaction.replace(R.id.fragment_container, bf);
				fragmentTransaction.commit();*/
			}
		});
	}

	private ArrayList<PaperOnboardingPage> getDataForOnboarding() {
		// prepare data
		PaperOnboardingPage scr1 = new PaperOnboardingPage("Quyen Pham", "Full-stack Developer",
				Color.parseColor("#678FB4"), R.drawable.quyen, R.drawable.key);
		PaperOnboardingPage scr2 = new PaperOnboardingPage("Research Interests", "Android OS, Django, Machine Learning...",
				Color.parseColor("#65B0B4"), R.drawable.research, R.drawable.wallet);
		PaperOnboardingPage scr3 = new PaperOnboardingPage("Contact", "ptquyen@apcs.vn",
				Color.parseColor("#9B90BC"), R.drawable.contact, R.drawable.shopping_cart);

		ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
		elements.add(scr1);
		elements.add(scr2);
		elements.add(scr3);
		return elements;
	}
}

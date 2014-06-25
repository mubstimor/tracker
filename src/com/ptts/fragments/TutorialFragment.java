/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ptts.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptts.R;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 * <p>This class is used by the {@link CardFlipActivity} and {@link
 * ScreenSlideActivity} samples.</p>
 */
public class TutorialFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static TutorialFragment create(int pageNumber) {
        TutorialFragment fragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public TutorialFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_tutorial_page, container, false);

        if(mPageNumber == 0){
          	 ((TextView) rootView.findViewById(android.R.id.text1)).setText("Page 1");
          	((ImageView)rootView.findViewById(R.id.image)).setImageResource(R.drawable.lenovo);
          	 ((TextView) rootView.findViewById(android.R.id.text2)).setText("very first page");
          }
           else
           if(mPageNumber == 1){
           	 ((TextView) rootView.findViewById(android.R.id.text1)).setText("Page 2");
           	((ImageView)rootView.findViewById(R.id.image)).setImageResource(R.drawable.lenovo);
           	 ((TextView) rootView.findViewById(android.R.id.text2)).setText("Custom content");
           }else
           	if(mPageNumber == 2){
              	 ((TextView) rootView.findViewById(android.R.id.text1)).setText("Page 3");
              	((ImageView)rootView.findViewById(R.id.image)).setImageResource(R.drawable.ultrabook);
              	 ((TextView) rootView.findViewById(android.R.id.text2)).setText("Custom content 2");
              }
           	else
           	 	if(mPageNumber == 3){
                     	 ((TextView) rootView.findViewById(android.R.id.text1)).setText("Page 4");
                     	((ImageView)rootView.findViewById(R.id.image)).setImageResource(R.drawable.ultrabook);
                     	 ((TextView) rootView.findViewById(android.R.id.text2)).setText("Custom content 2");
                     }
           	 	else
           	 	 	if(mPageNumber == 4){
           	           	 ((TextView) rootView.findViewById(android.R.id.text1)).setText("Page 5");
           	           	((ImageView)rootView.findViewById(R.id.image)).setImageResource(R.drawable.ultrabook);
           	           	 ((TextView) rootView.findViewById(android.R.id.text2)).setText("very last page");
           	           }


        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}

package com.checkedin.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.utility.Utility;
import com.checkedin.volley.WebServiceCall;

public class TutorialFrg extends Fragment {


    private WebView wvTutorial;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_tutorial, container, false);


        ((MainActivity) getActivity()).toggleActionBarIcon(1, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_tutorial));

        wvTutorial = (WebView) view.findViewById(R.id.wv_tutorial);
        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String frameVideo = "<html><body bgcolor=\"#000000\"><iframe width=100%\"\" height=\"90%\" src=\"" + WebServiceCall.TUTORIAL_LINK + "\" frameborder=\"0\" allowfullscreen></iframe></html></body>";
        wvTutorial.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Utility.logUtils("Url" + url);
                return false;
            }
        });
        WebSettings webSettings = wvTutorial.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wvTutorial.loadData(frameVideo, "text/html", "utf-8");

    }


    @Override
    public void onDestroyView() {
        wvTutorial.destroy();
        super.onDestroyView();

    }
}

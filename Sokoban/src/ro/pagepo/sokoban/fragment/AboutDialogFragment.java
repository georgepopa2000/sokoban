package ro.pagepo.sokoban.fragment;


import java.io.IOException;
import java.io.InputStream;

import ro.pagepo.sokoban.R;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

public class AboutDialogFragment extends DialogFragment {


	public static AboutDialogFragment newInstance(String title, String file) {
		AboutDialogFragment fragment = new AboutDialogFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("file", file);
		fragment.setArguments(args);
		return fragment;
	}

	public AboutDialogFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view =  inflater.inflate(R.layout.fragment_about_dialog, container,
				false);
		getDialog().setTitle(getArguments().getString("title","Dialog"));
		WebView wv = (WebView) view.findViewById(R.id.aboutWebview);
		wv.setMinimumHeight(200);
        try {
			InputStream fin = getActivity().getAssets().open(getArguments().getString("file", "index.html"));
			byte[] buffer = new byte[fin.available()];
			fin.read(buffer);
			fin.close();
			wv.loadData(new String(buffer), "text/html", "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        Button but = (Button) view.findViewById(R.id.buttonClose);
        but.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});
		return view;
	}

}

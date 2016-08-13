package hu.ait.android.weatherreport.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import hu.ait.android.weatherreport.R;

/**
 * Created by nathanmajumder on 5/3/16.
 */
public class AddCityFragment extends DialogFragment {

    public static final String TAG = "FragmentAddCity";

    public AddCityFragment() {}

    Button btnCancel;
    Button btnSave;
    EditText etCityName;

    private OnCompleteListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_add_city, container);

        etCityName = (EditText) rootView.findViewById(R.id.etAddCity);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onComplete(etCityName.getText().toString().trim());
                dismiss();
            }
        });

        getDialog().setTitle(R.string.addNewCityPrompt);

        return rootView;
    }

    public static interface OnCompleteListener {
        public abstract void onComplete(String city);
    }

    // make sure the Activity implemented it
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + getResources().getString(R.string.errorOnCompleteListenerMsg));
        }
    }
}

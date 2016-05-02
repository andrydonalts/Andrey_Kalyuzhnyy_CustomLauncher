package com.andry.andrey_kalyuzhnyy_homework3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by andry on 28.04.2016.
 */
public class AppDeleteFragmentDialog extends DialogFragment implements View.OnClickListener {

    private AppsDetail appsDetail;
    private ImageView icon;
    private TextView label;
    private TextView name;
    private Button deleteButton;
    private Button cancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        appsDetail = (AppsDetail) bundle.getParcelable(Adapter.APP_DETAIL_EXTRA);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_delete_app, null, false);

        icon = (ImageView) dialogView.findViewById(R.id.dialog_delete_app_image);
        label = (TextView) dialogView.findViewById(R.id.dialog_delete_app_label);
        name = (TextView) dialogView.findViewById(R.id.dialog_delete_app_name);
        deleteButton = (Button) dialogView.findViewById(R.id.dialog_delete_app_deleteButton);
        cancelButton = (Button) dialogView.findViewById(R.id.dialog_delete_app_cancelButton);

        icon.setImageDrawable(appsDetail.getIcon());
        label.setText(appsDetail.getLabel());
        name.setText(appsDetail.getName());
        deleteButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .show();

        return dialog;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.dialog_delete_app_deleteButton) {

            ((DeleteAppInterface) getActivity()).deleteApp(appsDetail.getName());
            dismiss();
        } else if (id == R.id.dialog_delete_app_cancelButton) {
            dismiss();
        }
    }

    public interface DeleteAppInterface{
        void deleteApp(String packageName);
    }
}
